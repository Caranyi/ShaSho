

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

def buscar_usuario_login(correo, contrasenya):
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                "SELECT id, nombre FROM usuario WHERE correo = %s AND contrasenya = %s",
                (correo, contrasenya),
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
    contrasenya = request.form.get("contrasenya", "")
    return buscar_usuario_login(correo, contrasenya)

# BUSCAR LISTAS USUARIO **************************************************************************************************

def buscar_listas_usuario(id_usuario):
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                "SELECT l.id, l.nombre, l.id_creador, l.cantidad_productos, l.compartida, l.tipo, l.imagen "
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
            "tipo": fila[5],
            "imagen": fila[6] or "",
        }
        for fila in filas
    ]

# BUSCAR TAREAS LISTA *****************************************************************************************************

def buscar_tareas_lista(id_lista):
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                """
                SELECT id, nombre, completada, imagen
                FROM tarea
                WHERE id_lista = %s
                ORDER BY id
                """,
                (id_lista,),
            )
            filas = cur.fetchall()

        return [
            {
                "id": fila[0],
                "nombre": fila[1],
                "completada": fila[2],
                "imagen": fila[3] or "",
            }
            for fila in filas
        ]
    finally:
        con.close()

# BUSCAR PRODUCTOS LISTA **************************************************************************************************
def buscar_productos_lista(id_lista):
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                "SELECT id, nombre, precio, tienda, tipo, cantidad, checked, id_usuario_mark, imagen "
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
            "imagen": fila[8] or "",
        }
        for fila in filas
    ]

# COMPARTIR LISTA **************************************************************************************************

def compartir_lista(id_lista, correo):

    correo_limpio = correo.strip().lower()
    con = psycopg2.connect(**DB_CONFIG)
    try:
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
def crear_lista(nombre, id_creador, tipo, imagen=None):
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                "INSERT INTO lista (id_creador, nombre, tipo, cantidad_productos, imagen) "
                "VALUES (%s, %s, %s, 0, %s) RETURNING id",
                (id_creador, nombre, tipo, imagen),
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
    tipo = request.form.get("tipo", "").strip().lower()
    imagen = request.form.get("imagen", "").strip() or None

    if not nombre or id_creador is None or tipo not in ("productos", "tareas"):
        return "-1"

    id_generado = crear_lista(nombre, id_creador, tipo, imagen)
    return str(id_generado)

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

#CREAR TAREAS ******************************************************************************************************
def crear_tarea(id_lista, nombre, imagen=None):
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                """
                INSERT INTO tarea (id_lista, nombre, imagen)
                VALUES (%s, %s, %s)
                RETURNING id
                """,
                (id_lista, nombre, imagen),
            )
            fila = cur.fetchone()

        con.commit()
        return fila[0] if fila else -1
    except Exception as e:
        con.rollback()
        print("Error al crear tarea:", e)
        return -1
    finally:
        con.close()

@app.post("/tareas")
def crear_tarea_db():
    id_lista = request.form.get("idLista", type=int)
    nombre = request.form.get("nombre", "").strip()
    imagen = request.form.get("imagen", "").strip() or None

    if id_lista is None or id_lista <= 0 or not nombre or not lista_tiene_tipo(id_lista, "tareas"):
        return "-1"

    return str(crear_tarea(id_lista, nombre, imagen))

# CREAR PRODUCTOS **************************************************************************************************
def crear_producto(id_lista, nombre, precio, tienda, tipo, cantidad, imagen=None):
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                """
                INSERT INTO producto (id_lista, nombre, precio, tienda, tipo, cantidad, checked, imagen)
                VALUES (%s, %s, %s, %s, %s, %s, FALSE, %s)
                RETURNING id
                """,
                (id_lista, nombre, precio, tienda, tipo, cantidad, imagen),
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
    imagen = request.form.get("imagen", "").strip() or None

    if not id_lista or id_lista <= 0 or not nombre or cantidad < 1 or precio < 0 or not lista_tiene_tipo(id_lista, "productos"):
        return "-1"

    id_generado = crear_producto(id_lista, nombre, precio, tienda, tipo, cantidad, imagen)
    return str(id_generado)

# ELIMINAR TAREAS ****************************************************************************************************
def eliminar_tarea(id_lista, id_tarea):
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                "DELETE FROM tarea WHERE id = %s AND id_lista = %s",
                (id_tarea, id_lista),
            )
            eliminada = cur.rowcount > 0

        con.commit()
        return eliminada
    except Exception as e:
        con.rollback()
        print("Error al eliminar tarea:", e)
        return False
    finally:
        con.close()


@app.post("/tareas/eliminar")
def eliminar_tarea_db():
    id_lista = request.form.get("idLista", type=int)
    id_tarea = request.form.get("idTarea", type=int)

    if not id_lista or id_lista <= 0 or not id_tarea or id_tarea <= 0 or not lista_tiene_tipo(id_lista, "tareas"):
        return "false"

    return "true" if eliminar_tarea(id_lista, id_tarea) else "false"

# ELIMINAR PRODUCTOS **************************************************************************************************
def eliminar_producto(id_lista, id_producto):
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                "DELETE FROM producto WHERE id = %s AND id_lista = %s",
                (id_producto, id_lista),
            )
            eliminado = cur.rowcount > 0

            if eliminado:
                cur.execute(
                    """
                    UPDATE lista
                    SET cantidad_productos = GREATEST(0, cantidad_productos - 1)
                    WHERE id = %s
                    """,
                    (id_lista,),
                )

        con.commit()
        return eliminado
    except Exception as e:
        con.rollback()
        print("Error al eliminar producto:", e)
        return False
    finally:
        con.close()

@app.post("/productos/eliminar")
def eliminar_producto_db():
    id_lista = request.form.get("idLista", type=int)
    id_producto = request.form.get("idProducto", type=int)

    if id_lista is None or id_lista <= 0 or id_producto is None or id_producto <= 0 or not lista_tiene_tipo(id_lista, "productos"):
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

#  VER TIPO LISTA ****************************************************************************************************
def lista_tiene_tipo(id_lista, tipo_esperado):
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                "SELECT 1 FROM lista WHERE id = %s AND tipo = %s",
                (id_lista, tipo_esperado),
            )
            return cur.fetchone() is not None
    finally:
        con.close()

# MARCADO PRODUCTOS ********************************************************************************************
def actualizar_marcado_producto(
    id_lista,
    id_producto,
    marcado,
    id_usuario_mark
):
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                """
                UPDATE producto
                SET checked = %s,
                    id_usuario_mark = %s
                WHERE id = %s AND id_lista = %s
                """,
                (marcado, id_usuario_mark, id_producto, id_lista),
            )
            actualizado = cur.rowcount > 0

        con.commit()
        return actualizado
    except Exception as e:
        con.rollback()
        print("Error al actualizar el marcado del producto:", e)
        return False
    finally:
        con.close()


@app.post("/productos/marcado")
def actualizar_marcado_producto_db():
    id_lista = request.form.get("idLista", type=int)
    id_producto = request.form.get("idProducto", type=int)
    marcado_texto = request.form.get("marcado", "").strip().lower()
    usuario_texto = request.form.get("idUsuarioMark", "null").strip().lower()

    if (
        id_lista is None or id_lista <= 0
        or id_producto is None or id_producto <= 0
        or marcado_texto not in ("true", "false")
        or not lista_tiene_tipo(id_lista, "productos")
    ):
        return "false"

    if usuario_texto in ("", "null"):
        id_usuario_mark = None
    else:
        try:
            id_usuario_mark = int(usuario_texto)
        except ValueError:
            return "false"

        # Los IDs no positivos no corresponden a usuarios reales.
        if id_usuario_mark <= 0:
            id_usuario_mark = None

    marcado = marcado_texto == "true"

    if not marcado:
        id_usuario_mark = None

    actualizado = actualizar_marcado_producto(
        id_lista,
        id_producto,
        marcado,
        id_usuario_mark
    )

    return "true" if actualizado else "false"

#COMPLETADO DE TAREAS **************************************************************************************************************
def actualizar_tarea_completada(id_lista, id_tarea, completada):
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                """
                UPDATE tarea
                SET completada = %s
                WHERE id = %s AND id_lista = %s
                """,
                (completada, id_tarea, id_lista),
            )
            actualizada = cur.rowcount > 0

        con.commit()
        return actualizada
    except Exception as e:
        con.rollback()
        print("Error al actualizar la tarea:", e)
        return False
    finally:
        con.close()


@app.post("/tareas/completada")
def actualizar_tarea_completada_db():
    id_lista = request.form.get("idLista", type=int)
    id_tarea = request.form.get("idTarea", type=int)
    completada_texto = request.form.get("completada", "").strip().lower()

    if (
        id_lista is None or id_lista <= 0
        or id_tarea is None or id_tarea <= 0
        or completada_texto not in ("true", "false")
        or not lista_tiene_tipo(id_lista, "tareas")
    ):
        return "false"

    completada = completada_texto == "true"
    actualizada = actualizar_tarea_completada(
        id_lista, id_tarea, completada
    )

    return "true" if actualizada else "false"

# ACTUALIZAR CANTIDAD DE PRODUCTOS *************************************************************************************************
def actualizar_cantidad_producto(id_lista, id_producto, cantidad):
    con = psycopg2.connect(**DB_CONFIG)
    try:
        with con.cursor() as cur:
            cur.execute(
                """
                UPDATE producto
                SET cantidad = %s
                WHERE id = %s AND id_lista = %s
                """,
                (cantidad, id_producto, id_lista),
            )
            actualizado = cur.rowcount > 0

        con.commit()
        return actualizado
    except Exception as e:
        con.rollback()
        print("Error al actualizar la cantidad del producto:", e)
        return False
    finally:
        con.close()


@app.post("/productos/cantidad")
def actualizar_cantidad_producto_db():
    id_lista = request.form.get("idLista", type=int)
    id_producto = request.form.get("idProducto", type=int)
    cantidad = request.form.get("cantidad", type=int)

    if (
        id_lista is None or id_lista <= 0
        or id_producto is None or id_producto <= 0
        or cantidad is None or cantidad < 1
        or not lista_tiene_tipo(id_lista, "productos")
    ):
        return "false"

    actualizado = actualizar_cantidad_producto(
        id_lista, id_producto, cantidad
    )

    return "true" if actualizado else "false"


if __name__ == "__main__":

    app.run(host="0.0.0.0", port=PUERTO)
