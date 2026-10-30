package com.example.shashoapp.features.productos.domain.model

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val imagen: String = "",
    val isChecked: Boolean = false,
    val cantidad: Int = 1
){
    val precioFormateado: String
        get() = "${precio.toString()}€"

    val dameChecked: Boolean
        get() = isChecked

    val dameCantidad: Int
        get() = cantidad;
}