package com.example.shashoapp.features.Ajustes.presentation

data class AjustesState(
    val idUsuario: Int? = null,
    val nombreUsuario: String = "",
    val fotoPerfilBytes: ByteArray? = null,
    val enLinea: Boolean = true,
    val notificacionesActivas: Boolean = true,
    val modoOscuro: Boolean = false,
    val idiomaActual: String = "es",
    val menuIdiomaAbierto: Boolean = false,
    val mostrarDialogoNombre: Boolean = false,
    val nombreTemporal: String = "",
    val guardandoNombre: Boolean = false,
    val errorNombre: String? = null
)