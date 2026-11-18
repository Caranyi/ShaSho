package com.example.shashoapp.features.Ajustes.presentation

sealed interface AjustesAction {
    data object onCerrarSesionClick: AjustesAction
    data class onActivarNotificaciones(val activadas: Boolean) : AjustesAction
}