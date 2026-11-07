package com.example.shashoapp.features.listaListas.data.model

import com.example.shashoapp.features.listaListas.domain.model.Lista
import kotlinx.serialization.Serializable

@Serializable
data class ListaDto(
    val id: Int,
    val nombre: String,
    val idCreador: Int,
    val cantidadProductos: Int
)

fun ListaDto.toDomain(): Lista = Lista(
    id = id,
    nombre = nombre,
    id_creador = idCreador,
    cantidadProductos = cantidadProductos
)
