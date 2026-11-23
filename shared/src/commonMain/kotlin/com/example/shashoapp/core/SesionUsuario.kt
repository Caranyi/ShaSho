package com.example.shashoapp.core

object SesionUsuario {
    private var idUsuario: Int? = null
    private var nombreUsuario: String = ""

    fun getIdUsuario(): Int? = idUsuario

    fun setIdUsuario(id: Int?) {
        idUsuario = id
    }

    fun getNombreUsuario(): String = nombreUsuario

    fun setNombreUsuario(nombre: String) {
        nombreUsuario = nombre
    }

    fun cerrarSesion() {
        idUsuario = null
        nombreUsuario = ""
    }
}