package com.example.shashoapp.features.listaListas.data.model

import com.example.shashoapp.features.listaListas.domain.model.Lista
import kotlinx.serialization.Serializable
import kotlin.io.encoding.Base64

@Serializable
data class ListaDto(
    val id: Int,
    val nombre: String,
    val idCreador: Int,
    val cantidadProductos: Int,
    val compartida: Boolean = false,
    val tipo: String = "productos",
    val idsUsuarios: List<Int> = emptyList(),
    val imagen: String = ""
)

fun ListaDto.toDomain(): Lista = Lista(
    id = id,
    nombre = nombre,
    id_creador = idCreador,
    cantidadProductos = cantidadProductos,
    compartida = compartida,
    tipo = tipo,
    ids_usuarios = idsUsuarios.toIntArray(),
    imagenBytes = if (imagen.isBlank()) {
        null //si no se añade ninguna imagen
    } else {
        runCatching { Base64.decode(imagen) }
            .onFailure { error ->
                println("Error decodificando la imagen: ${error.message}")
            }
            .getOrNull()
    })
