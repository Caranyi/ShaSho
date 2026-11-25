package com.example.shashoapp.features.listaTareas.domain.repository

import com.example.shashoapp.features.listaTareas.domain.model.Tarea

interface TareaRepository {
    suspend fun getTareas(idLista: Int): List<Tarea>

    suspend fun crearTarea(
        idLista: Int,
        nombre: String,
        imagenBytes: ByteArray? = null
    )

    suspend fun actualizarCompletada(
        idLista: Int,
        idTarea: Int,
        completada: Boolean
    )

    suspend fun eliminarTarea(
        idLista: Int,
        idTarea: Int
    )
}