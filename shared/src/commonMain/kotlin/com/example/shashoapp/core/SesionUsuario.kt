package com.example.shashoapp.core


object SesionUsuario {
    private var idUsuario: Int? = null

    fun getIdUsuario(): Int? = idUsuario

    fun setIdUsuario(id: Int?) {
        idUsuario = id
    }
}
