package com.example.shashoapp.features.Ajustes.presentation

sealed interface AjustesAction {
    data object onCerrarSesionClick: AjustesAction
    data class onActivarModoOscuro(val activado: Boolean) : AjustesAction
    data class onActivarNotificaciones(val activadas: Boolean) : AjustesAction
}