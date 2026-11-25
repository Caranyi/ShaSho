package com.example.shashoapp.features.listaProductos.data.model

import com.example.shashoapp.features.auth.domain.model.Usuario
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
    val checked: Boolean,
    val idUsuarioMark: Int? = null,
    val imagen: String = ""
)

fun ProductoDto.toDomain(): Producto = Producto(
    id = id,
    nombre = nombre,
    precio = precio,
    isChecked = checked,
    cantidad = cantidad,
    tienda = tienda,
    tipo = tipo,
    idUsuarioMark = idUsuarioMark,
    imagenBytes = if (imagen.isNotBlank()) runCatching { kotlin.io.encoding.Base64.decode(imagen) }.getOrNull() else null
)
