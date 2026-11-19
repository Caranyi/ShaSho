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
    val errorTodosCamposObligatorios: String
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
        errorTodosCamposObligatorios = "Todos los campos son obligatorios"
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
        errorTodosCamposObligatorios = "All fields are required"
    )

    fun get(codigoIdioma: String): Texts = if (codigoIdioma == "en") en else es
}
