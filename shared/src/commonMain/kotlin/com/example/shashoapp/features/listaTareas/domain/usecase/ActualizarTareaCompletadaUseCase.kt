package com.example.shashoapp.features.listaTareas.domain.usecase

import com.example.shashoapp.features.listaTareas.domain.repository.TareaRepository

class ActualizarTareaCompletadaUseCase(
    private val repository: TareaRepository
) {
    suspend operator fun invoke(
        idLista: Int,
        idTarea: Int,
        completada: Boolean
    ) {
        repository.actualizarCompletada(idLista, idTarea, completada)
    }
}