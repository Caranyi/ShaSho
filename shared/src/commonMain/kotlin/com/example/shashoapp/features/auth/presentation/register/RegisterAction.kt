package com.example.shashoapp.features.auth.presentation.register

sealed interface RegisterAction {
    data class OnNombreChanged(val nombre: String) : RegisterAction
    data class OnCorreoChanged(val correo: String) : RegisterAction
    data class OnContrasenyaChanged(val contrasenya: String) : RegisterAction
    data object OnRegisterClick : RegisterAction
    data object OnIrALoginClick : RegisterAction
}