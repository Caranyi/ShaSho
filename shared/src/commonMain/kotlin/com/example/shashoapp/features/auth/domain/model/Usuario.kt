package com.example.shashoapp.features.auth.domain.model


data class Usuario(
    val id : Int,
    val nombre : String,
    val contrasenya : String,
    val correo : String
) {
    val getUserId : Int
        get() = id
}