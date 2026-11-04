package com.example.shashoapp.features.auth.presentation.login

data class LoginState(
    val correo: String = "",
    val contrasenya: String = "",
    val cargando: Boolean = false,
    val error: String? = null,
    val loginExitoso: Boolean = false
)