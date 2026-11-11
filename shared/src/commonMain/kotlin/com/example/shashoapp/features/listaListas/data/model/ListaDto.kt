package com.example.shashoapp.features.listaListas.data.model

import com.example.shashoapp.features.listaListas.domain.model.Lista
import kotlinx.serialization.Serializable

@Serializable
data class ListaDto(
    val id: Int,
    val nombre: String,
    val idCreador: Int,
    val cantidadProductos: Int,
    //si una lista es compartida (falso por defecto porque al crearla solo hay un user)
    val compartida: Boolean = false
)

fun ListaDto.toDomain(): Lista = Lista(
    id = id,
    nombre = nombre,
    id_creador = idCreador,
    cantidadProductos = cantidadProductos,
    compartida = compartida
)
