package com.example.shashoapp.features.listaProductos.domain.model

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val isChecked: Boolean = false,
    val cantidad: Int = 1,
    val tienda: String = "",
    val tipo: String = ""
){
    val precioFormateado: String
        get() = "${precio.toString()}€"

    val dameChecked: Boolean
        get() = isChecked

    val dameCantidad: Int
        get() = cantidad;
}