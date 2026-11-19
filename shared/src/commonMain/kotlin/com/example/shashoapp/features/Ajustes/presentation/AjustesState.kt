package com.example.shashoapp.features.Ajustes.presentation

data class AjustesState(
    val idUsuario: Int? = null,
    val enLinea: Boolean = true,
    val notificacionesActivas: Boolean = true,
    val idiomaActual: String = "es",
    val menuIdiomaAbierto: Boolean = false
){

}