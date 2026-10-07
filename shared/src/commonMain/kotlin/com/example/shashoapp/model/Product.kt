package com.example.shashoapp.model

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val imagen: String = "",
    var isChecked: Boolean = false
){
    val precioFormateado: String
        get() = "${precio.toString()}€"

    val dameChecked: Boolean
        get() = isChecked
}