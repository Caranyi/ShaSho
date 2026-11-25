package com.example.shashoapp.core

import kotlin.String

data class Texts(
    // para la pantalla de bienvenida
    val bienvenidaTitulo: String,
    val conectandoServidor: String,
    val conectadoBd: String,
    val sinConexionServidor: String,
    val iniciarSesion: String,
    val registrarse: String,

    // para Auth (Login y Register)
    val correoElectronico: String,
    val contrasenya: String,
    val entrar: String,
    val noTienesCuenta: String,
    val crearCuenta: String,
    val nombreCompleto: String,
    val yaTienesCuenta: String,

    // para Barra navegación y listas
    val inicio: String,
    val anadirLista: String,
    val ajustes: String,
    val misListas: String,
    val compartida: String,
    val crearLista: String,
    val datos: String,
    val nombreLista: String,
    val guardar: String,
    val cancelar: String,
    val eliminarListaTitulo: String,
    val quieresEliminar: String,
    val eliminar: String,
    val tipo_lista_productos: String,
    val tipo_lista_tareas: String,
    val tipo_de_lista: String,

    //para Tareas
    val anadirTarea: String,
    val nombreTarea: String,
    val tareasVacias: String,
    val eliminarTareaTitulo: String,


    // para Productos
    val listaCabecera: String,
    val anadirPersona: String,
    val listaVacia: String,
    val anadirProducto: String,
    val nombreProducto: String,
    val seleccionarTipo: String,
    val alimentacion: String,
    val otros: String,
    val cantidad: String,
    val precio: String,
    val tienda: String,
    val anadir: String,
    val correo: String,
    val eliminarProductoTitulo: String,
    val anterior: String,

    // para Reparto
    val repartoTitulo: String,

    // para Ajustes
    val usuarioPrefix: String,
    val modoOnline: String,
    val modoOffline: String,
    val modoOscuro: String,
    val cambiarFotoPerfil: String,
    val cambiarNombreUsuario: String,
    val nuevoNombreUsuario: String,
    val notificaciones: String,
    val idiomaLabel: String,
    val espanol: String,
    val ingles: String,
    val cerrarSesion: String,

    // para Notificación
    val notifTitulo: String,
    val notifMensaje: String,

    // para errores
    val errorNombreListaVacio: String,
    val errorUsuarioNoEncontrado: String,
    val errorIntroduceCorreo: String,
    val errorNombreProductoVacio: String,
    val errorCantidadMayorCero: String,
    val errorPrecioNegativo: String,
    val errorCamposLoginObligatorios: String,
    val errorTodosCamposObligatorios: String,
    val errorNombreUsuarioVacio: String,
    val errorActualizarNombre: String
)

object TextosApp {
    private val es = Texts(
        bienvenidaTitulo = "¡BIENVENIDO A SHASHO!",
        conectandoServidor = "Conectando con el servidor...",
        conectadoBd = "Conectado a la base de datos",
        sinConexionServidor = "Sin conexión con el servidor",
        iniciarSesion = "Iniciar sesión",
        registrarse = "Registrarse",

        correoElectronico = "Correo electrónico",
        contrasenya = "Contraseña",
        entrar = "Entrar",
        noTienesCuenta = "¿No tienes cuenta? Regístrate aquí",
        crearCuenta = "Crear Cuenta",
        nombreCompleto = "Nombre completo",
        yaTienesCuenta = "¿Ya tienes cuenta? Inicia sesión",

        inicio = "Inicio",
        anadirLista = "Añadir lista",
        ajustes = "Ajustes",
        misListas = "MIS LISTAS",
        compartida = "Compartida",
        crearLista = "Crear lista",
        datos = "Datos",
        nombreLista = "Nombre de la lista",
        guardar = "Guardar",
        cancelar = "Cancelar",
        eliminarListaTitulo = "Eliminar lista",
        quieresEliminar = "¿Quieres eliminar",
        eliminar = "Eliminar",
        tipo_lista_productos =  "Productos",
        tipo_lista_tareas = "Tareas",
        tipo_de_lista = "Tipo de lista",


        anadirTarea =  "Añadir tarea",
        nombreTarea =  "Nombre de la tarea",
        tareasVacias = "Todavía no hay tareas. Añade una con +",
        eliminarTareaTitulo = "Eliminar tarea",

        listaCabecera = "LISTA",
        anadirPersona = "+ Personas",
        listaVacia = "La lista está vacía.\nAñade un producto con +",
        anadirProducto = "Añadir producto",
        nombreProducto = "Nombre del producto",
        seleccionarTipo = "Seleccionar tipo",
        alimentacion = "alimentación",
        otros = "otros",
        cantidad = "Cantidad",
        precio = "Precio",
        tienda = "Tienda",
        anadir = "Añadir",
        correo = "Correo",
        eliminarProductoTitulo = "Eliminar producto",
        anterior = "Anterior",

        repartoTitulo = "REPARTO",

        usuarioPrefix = "Usuario #",
        modoOnline = "Modo en línea activado",
        modoOffline = "Modo sin conexión",
        modoOscuro = "Modo oscuro",
        cambiarFotoPerfil = "Cambiar foto",
        cambiarNombreUsuario = "Cambiar nombre",
        nuevoNombreUsuario = "Nuevo nombre de usuario",
        notificaciones = "Notificaciones",
        idiomaLabel = "Idioma",
        espanol = "Español",
        ingles = "English",
        cerrarSesion = "Cerrar sesión",

        notifTitulo = "ShaSho",
        notifMensaje = "¡Vuelve que tus listas se sienten solas!",

        errorNombreListaVacio = "El nombre no puede estar en blanco",
        errorUsuarioNoEncontrado = "Usuario no encontrado",
        errorIntroduceCorreo = "Introduce un correo",
        errorNombreProductoVacio = "El nombre no puede estar vacío",
        errorCantidadMayorCero = "La cantidad debe ser mayor que 0",
        errorPrecioNegativo = "El precio no puede ser negativo",
        errorCamposLoginObligatorios = "El correo y la contraseña son obligatorios",
        errorTodosCamposObligatorios = "Todos los campos son obligatorios",
        errorNombreUsuarioVacio = "El nombre de usuario no puede estar vacío",
        errorActualizarNombre = "No se pudo actualizar el nombre de usuario"
    )

    private val en = Texts(
        bienvenidaTitulo = "WELCOME TO SHASHO!",
        conectandoServidor = "Connecting to server...",
        conectadoBd = "Connected to database",
        sinConexionServidor = "No server connection",
        iniciarSesion = "Log in",
        registrarse = "Sign up",

        correoElectronico = "Email",
        contrasenya = "Password",
        entrar = "Sign in",
        noTienesCuenta = "Don't have an account? Sign up here",
        crearCuenta = "Create Account",
        nombreCompleto = "Full name",
        yaTienesCuenta = "Already have an account? Log in",

        inicio = "Home",
        anadirLista = "Add list",
        ajustes = "Settings",
        misListas = "MY LISTS",
        compartida = "Shared",
        crearLista = "Create list",
        datos = "Details",
        nombreLista = "List name",
        guardar = "Save",
        cancelar = "Cancel",
        eliminarListaTitulo = "Delete list",
        quieresEliminar = "Do you want to delete",
        eliminar = "Delete",
        tipo_lista_productos =  "Products",
        tipo_lista_tareas = "Tasks",
        tipo_de_lista = "List type",

        anadirTarea =  "Add task",
        nombreTarea =  "Task name",
        tareasVacias = "There are no tasks yet. Add one with +",
        eliminarTareaTitulo = "Delete task",

        listaCabecera = "LIST",
        anadirPersona = "+ Person",
        listaVacia = "The list is empty.\nAdd a product with +",
        anadirProducto = "Add product",
        nombreProducto = "Product name",
        seleccionarTipo = "Select type",
        alimentacion = "food",
        otros = "others",
        cantidad = "Quantity",
        precio = "Price",
        tienda = "Store",
        anadir = "Add",
        correo = "Email",
        eliminarProductoTitulo = "Delete product",
        anterior = "Back",

        repartoTitulo = "SPLIT",

        usuarioPrefix = "User #",
        modoOnline = "Online mode enabled",
        modoOffline = "Offline mode",
        modoOscuro = "Dark mode",
        cambiarFotoPerfil = "Change photo",
        cambiarNombreUsuario = "Change name",
        nuevoNombreUsuario = "New username",
        notificaciones = "Notifications",
        idiomaLabel = "Language",
        espanol = "Español",
        ingles = "English",
        cerrarSesion = "Log out",

        notifTitulo = "ShaSho",
        notifMensaje = "Come back, your lists feel lonely!",

        errorNombreListaVacio = "Name cannot be blank",
        errorUsuarioNoEncontrado = "User not found",
        errorIntroduceCorreo = "Enter an email",
        errorNombreProductoVacio = "Name cannot be empty",
        errorCantidadMayorCero = "Quantity must be greater than 0",
        errorPrecioNegativo = "Price cannot be negative",
        errorCamposLoginObligatorios = "Email and password are required",
        errorTodosCamposObligatorios = "All fields are required",
        errorNombreUsuarioVacio = "Username cannot be empty",
        errorActualizarNombre = "Could not update username"
    )

    fun get(codigoIdioma: String): Texts = if (codigoIdioma == "en") en else es
}