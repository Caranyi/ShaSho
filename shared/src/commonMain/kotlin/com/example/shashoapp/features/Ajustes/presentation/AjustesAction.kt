package com.example.shashoapp.features.Ajustes.presentation

sealed interface AjustesAction {
    data object onCerrarSesionClick: AjustesAction
    data class onActivarNotificaciones(val activadas: Boolean) : AjustesAction
    data object onToggleMenuIdioma : AjustesAction
    data object onCerrarMenuIdioma : AjustesAction
    data class onCambiarIdioma(val codigoIdioma: String) : AjustesAction
}