package com.example.shashoapp.core

object EstadoConexion {
    private var conexion: Boolean = false

    fun getConexion(): Boolean = conexion

    fun setConexion(valor: Boolean) {
        conexion = valor
    }
}