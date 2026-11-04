package com.example.shashoapp.features.auth.presentation.login

sealed interface LoginAction {
    data class OnCorreoChanged(val correo: String): LoginAction
    data class OnContrasenyaChanged(val contrasenya: String) : LoginAction
    data object OnLoginClick : LoginAction
    data object OnIrARegistroClick : LoginAction
}