package com.example.shashoapp.features.listaProductos.data.model

import com.example.shashoapp.features.listaProductos.domain.model.Producto
import kotlinx.serialization.Serializable

@Serializable
data class ProductoDto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val tienda: String,
    val tipo: String,
    val cantidad: Int,
    val checked: Boolean
)

//json a objeto
fun ProductoDto.toDomain(): Producto = Producto(
    id = id,
    nombre = nombre,
    precio = precio,
    isChecked = checked,
    cantidad = cantidad,
    tienda = tienda,
    tipo = tipo
)
