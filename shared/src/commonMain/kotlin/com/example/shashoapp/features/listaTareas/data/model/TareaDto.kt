package com.example.shashoapp.features.listaTareas.data.model

import com.example.shashoapp.features.listaTareas.domain.model.Tarea
import kotlinx.serialization.Serializable

@Serializable
data class TareaDto(
    val id: Int,
    val nombre: String,
    val completada: Boolean
)

// json a objeto
fun TareaDto.toDomain(): Tarea = Tarea(
    id = id,
    nombre = nombre,
    completada = completada
)