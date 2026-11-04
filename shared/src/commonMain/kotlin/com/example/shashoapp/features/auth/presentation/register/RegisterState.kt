package com.example.shashoapp.features.auth.presentation.register

data class RegisterState(
    val nombre: String = "",
    val correo: String = "",
    val contrasenya: String = "",
    val cargando: Boolean = false,
    val error: String? = null,
    val registroExitoso: Boolean = false
)