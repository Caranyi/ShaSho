package com.example.shashoapp.features.listaTareas.data.model

import com.example.shashoapp.features.listaTareas.domain.model.Tarea
import kotlinx.serialization.Serializable
import kotlin.io.encoding.Base64

@Serializable
data class TareaDto(
    val id: Int,
    val nombre: String,
    val completada: Boolean,
    val imagen: String = ""
)

fun TareaDto.toDomain(): Tarea = Tarea(
    id = id,
    nombre = nombre,
    completada = completada,
    imagenBytes = if (imagen.isNotBlank()) {
        runCatching { Base64.decode(imagen) }
            .onFailure { error ->
                println("Error decodificando la imagen: ${error.message}")
            }
            .getOrNull()
    }
    else null
)