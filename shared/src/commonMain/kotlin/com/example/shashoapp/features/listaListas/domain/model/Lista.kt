package com.example.shashoapp.features.listaListas.domain.model

data class Lista (
    val id:Int,
    val nombre: String,
    val id_creador: Int,
    val ids_usuarios: IntArray = intArrayOf(),
    val cantidadProductos: Int,
    val compartida: Boolean = false
    ){

    val dameNumUsuarios: Int
        get() = ids_usuarios.size

    val dameCantidadProductosLista: Int
        get() = cantidadProductos
}