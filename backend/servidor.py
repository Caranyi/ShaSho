

import psycopg2
from flask import Flask, jsonify, request


DB_CONFIG = {
    "host": "localhost",
    "port": 5432,
    "dbname": "postgres",
    "user": "postgres",
    "password": "lurmesSQL",
}
PUERTO = 5000

app = Flask(__name__)


# COMPROBAR CONEXION **************************************************************************************************
def comprobar_conexion():

    try:
        con = psycopg2.connect(**DB_CONFIG)
        try:
            with con.cursor() as cur:
                cur.execute("SELECT 1")
            return True
        finally:
            con.close()
    except Exception as e:
        print("ERROR conectando a la base de datos:", e)
        return False

@app.get("/ping")
def ping():
    if comprobar_conexion():
        return "", 200
    return "", 503


# BUSCAR ID USUARIO **************************************************************************************************

def buscar_usuario_login(correo, contrasena):
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                "SELECT id, nombre FROM usuario WHERE correo = %s AND contrasenya = %s",
                (correo, contrasena),
            )
            fila = cur.fetchone()
        if fila:
            return f"{fila[0]}|{fila[1]}"
        return "-1"
    finally:
        con.close()

@app.post("/login")
def login():
    correo = request.form.get("correo", "").strip()
    contrasena = request.form.get("contrasena", "")
    return buscar_usuario_login(correo, contrasena)

# BUSCAR LISTAS USUARIO **************************************************************************************************

def buscar_listas_usuario(id_usuario):

    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                "SELECT l.id, l.nombre, l.id_creador, l.cantidad_productos, l.compartida "
                "FROM lista l "
                "JOIN union_lista_usuario u ON u.id_lista = l.id "
                "WHERE u.id_usuario = %s "
                "ORDER BY l.id",
                (id_usuario,),
            )
            filas = cur.fetchall()
    finally:
        con.close()

    return [
        {
            "id": fila[0],
            "nombre": fila[1],
            "idCreador": fila[2],
            "cantidadProductos": fila[3],
            "compartida": fila[4],
        }
        for fila in filas
    ]

# BUSCAR PRODUCTOS LISTA **************************************************************************************************
def buscar_productos_lista(id_lista):

    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                "SELECT id, nombre, precio, tienda, tipo, cantidad, checked, id_usuario_mark "
                "FROM producto "
                "WHERE id_lista = %s "
                "ORDER BY id",
                (id_lista,),
            )
            filas = cur.fetchall()
    finally:
        con.close()

    return [
        {
            "id": fila[0],
            "nombre": fila[1],
            "precio": float(fila[2]),
            "tienda": fila[3],
            "tipo": fila[4],
            "cantidad": fila[5],
            "checked": fila[6],
            "idUsuarioMark": fila[7],
        }
        for fila in filas
    ]

# COMPARTIR LISTA **************************************************************************************************

def compartir_lista(id_lista, correo):

    con = psycopg2.connect(**DB_CONFIG)
    try:

        correo_limpio = correo.strip().lower()

        with con.cursor() as cur:

            cur.execute("SELECT id FROM usuario WHERE LOWER(TRIM(correo)) = %s", (correo_limpio,))
            fila = cur.fetchone()
            if fila is None:
                return -1
            id_usuario = fila[0]

            cur.execute(
                "SELECT 1 FROM union_lista_usuario WHERE id_lista = %s AND id_usuario = %s",
                (id_lista, id_usuario),
            )
            if cur.fetchone() is not None:
                return 0

            cur.execute(
                "INSERT INTO union_lista_usuario (id_lista, id_usuario) VALUES (%s, %s)",
                (id_lista, id_usuario),
            )
            cur.execute(
                "UPDATE lista SET compartida = TRUE WHERE id = %s",
                (id_lista,),
            )
        con.commit()
        return 1

    except Exception as e:
        print("Error al compartir la lista postgresql", e)
        return -2
    finally:
        con.close()

@app.post("/compartir")
def compartir():
    id_lista = request.form.get("idLista", type=int)
    correo = request.form.get("correo", "").strip()
    if not id_lista or id_lista <= 0 or not correo:
        return "-1"
    return str(compartir_lista(id_lista, correo))

# CREAR LISTA **************************************************************************************************
def crear_lista(nombre, id_creador):
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                "INSERT INTO lista (id_creador, nombre, cantidad_productos) VALUES (%s, %s, 0) RETURNING id",
                (id_creador, nombre),
            )
            fila = cur.fetchone()
            if fila is None:
                return -1

            nuevo_id = fila[0]  

            cur.execute(
                "INSERT INTO union_lista_usuario (id_lista, id_usuario) VALUES (%s, %s)",
                (nuevo_id, id_creador),
            )
        con.commit()
        return nuevo_id
    except Exception as e:
        print("Error creando la lista en postgresql:", e)
        return -1
    finally:
        con.close()

@app.post("/listas")
def crear_lista_db():
    nombre = request.form.get("nombre", "").strip()
    id_creador = request.form.get("idCreador", type=int)

    if not nombre or id_creador is None:
        return "-1"

    id_generado = crear_lista(nombre, id_creador)
    return str(id_generado)


@app.get("/listas")
def listas():
    id_usuario = request.args.get("idUsuario", type=int)
    if id_usuario is None:
        return jsonify([])
    return jsonify(buscar_listas_usuario(id_usuario))

# ELIMINAR LISTA **************************************************************************************************
def eliminar_lista(id_lista):
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute("DELETE FROM lista WHERE id = %s", (id_lista,))
            con.commit()
            return True
    except Exception as e:
        print("Error al eliminar lista en postgresql", e)
        return False
    finally: 
        con.close()

@app.post("/listas/eliminar")
def eliminar_lista_db():
    id_lista = request.form.get("idLista", type=int)

    if not id_lista or id_lista <= 0:
        return "false"

    exito = eliminar_lista(id_lista)
    return "true" if exito else "false"


# CREAR PRODUCTOS **************************************************************************************************
def crear_producto(id_lista, nombre, precio, tienda, tipo, cantidad):
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                """
                INSERT INTO producto (id_lista, nombre, precio, tienda, tipo, cantidad, comprado)
                VALUES (%s, %s, %s, %s, %s, %s, FALSE)
                RETURNING id
                """,
                (id_lista, nombre, precio, tienda, tipo, cantidad),
            )
            fila = cur.fetchone()
            nuevo_id = fila[0] if fila else -1

            cur.execute(
                "UPDATE lista SET cantidad_productos = cantidad_productos + 1 WHERE id = %s",
                (id_lista,),
            )
        con.commit()
        return nuevo_id
    except Exception as e:
        print("Error al crear producto en postgresql:", e)
        return -1
    finally:
        con.close()

@app.post("/productos")
def crear_producto_db():
    id_lista = request.form.get("idLista", type=int)
    nombre = request.form.get("nombre", "").strip()
    precio = request.form.get("precio", type=float, default=0.0)
    tienda = request.form.get("tienda", "").strip()
    tipo = request.form.get("tipo", "").strip()
    cantidad = request.form.get("cantidad", type=int, default=1)

    if not id_lista or id_lista <= 0 or not nombre:
        return "-1"

    id_generado = crear_producto(id_lista, nombre, precio, tienda, tipo, cantidad)
    return str(id_generado)


@app.get("/productos")
def productos():
    id_lista = request.args.get("idLista", type=int)
    if id_lista is None:
        return jsonify([])
    return jsonify(buscar_productos_lista(id_lista))





# ELIMINAR PRODUCTOS **************************************************************************************************
def eliminar_producto(id_lista, id_producto):
    con = psycopg2.connect(**DB_CONFIG)

    try:
        with con.cursor() as cur:
            cur.execute("DELETE FROM producto WHERE id = %s AND id_lista = %s",
                        (id_producto, id_lista)
            )

            cur.execute("UPDATE lista SET cantidad_productos = GREATEST(0, cantidad_productos-1) WHERE id = %s",
                        (id_lista))

            con.commit()
            return True
    except Exception as e:
        print("Error al elimina el producto en postgresql")
        return False
    finally:
        con.close()

@app.post("/productos/eliminar")
def eliminar_producto_db():
    id_lista = request.form.get("idLista", type=int)
    id_producto = request.form.get("idProducto", type=int)

    if not id_lista or not id_producto:
        return "false"

    exito = eliminar_producto(id_lista, id_producto)
    return "true" if exito else "false"


# REGISTRAR USUARIO **************************************************************************************************
def registrar_usuario(nombre, correo, constrasenya):
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute("SELECT id FROM usuario WHERE correo = %s", (correo,))
            if cur.fetchone() is not None:
                return -1 #correo ya registrado

            cur.execute(
                "INSERT INTO usuario (nombre, correo, contrasenya) VALUES (%s, %s, %s) RETURNING id",
                (nombre, correo, constrasenya)
            )

            nuevo_id = cur.fetchone()[0]

        con.commit()
        return nuevo_id
    except Exception as e:
        print("Error registrando usuario:", e)
        return -2
    finally:
        con.close()


@app.post("/registro")
def registro():
    nombre = request.form.get("nombre", "").strip()
    correo = request.form.get("correo", "").strip()
    contrasenya = request.form.get("contrasenya", "")
    return str(registrar_usuario(nombre, correo, contrasenya))

# ACTUALIZAR NOMBRE DE USUARIO ****************************************************************************************
def actualizar_nombre_usuario(id_usuario, nuevo_nombre):
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                "UPDATE usuario SET nombre = %s WHERE id = %s",
                (nuevo_nombre, id_usuario),
            )
            actualizado = cur.rowcount > 0
        con.commit()
        return actualizado
    except Exception as e:
        print("Error actualizando nombre de usuario:", e)
        return False
    finally:
        con.close()

@app.post("/usuario/nombre")
def actualizar_nombre_db():
    id_usuario = request.form.get("idUsuario", type=int)
    nombre = request.form.get("nombre", "").strip()

    if not id_usuario or id_usuario <= 0 or not nombre:
        return "false"

    exito = actualizar_nombre_usuario(id_usuario, nombre)
    return "true" if exito else "false"


# ACTUALIZAR FOTO DE PERFIL *******************************************************************************************
def actualizar_foto_usuario(id_usuario, foto_b64):
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                "UPDATE usuario SET foto_perfil = %s WHERE id = %s",
                (foto_b64, id_usuario),
            )
            actualizado = cur.rowcount > 0
        con.commit()
        return actualizado
    except Exception as e:
        print("Error actualizando foto de usuario:", e)
        return False
    finally:
        con.close()

@app.post("/usuario/foto")
def actualizar_foto_db():
    id_usuario = request.form.get("idUsuario", type=int)
    foto = request.form.get("foto", "").strip()

    if not id_usuario or id_usuario <= 0:
        return "false"

    exito = actualizar_foto_usuario(id_usuario, foto if foto else None)
    return "true" if exito else "false"


# OBTENER NOMBRES Y FOTOS DE USUARIOS POR IDS (PARA REPARTO Y PERFIL) *************************************************
def obtener_info_usuarios(ids):
    if not ids:
        return {}
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                "SELECT id, nombre, foto_perfil FROM usuario WHERE id = ANY(%s)",
                (ids,),
            )
            filas = cur.fetchall()
        return {
            str(fila[0]): {
                "nombre": fila[1] or "",
                "fotoPerfil": fila[2] or "",
            }
            for fila in filas
        }
    except Exception as e:
        print("Error obteniendo info de usuarios:", e)
        return {}
    finally:
        con.close()

@app.get("/usuarios/info")
def usuarios_info():
    ids_raw = request.args.get("ids", "").strip()
    if not ids_raw:
        return jsonify({})
    ids = []
    for parte in ids_raw.split(","):
        parte = parte.strip()
        if parte.lstrip("-").isdigit():
            val = int(parte)
            if val > 0:
                ids.append(val)
    return jsonify(obtener_info_usuarios(ids))










if __name__ == "__main__":

    app.run(host="0.0.0.0", port=PUERTO)
