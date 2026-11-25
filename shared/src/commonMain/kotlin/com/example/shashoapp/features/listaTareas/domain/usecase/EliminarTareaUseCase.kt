package com.example.shashoapp.features.listaTareas.domain.usecase

import com.example.shashoapp.features.listaTareas.domain.repository.TareaRepository

class EliminarTareaUseCase(
    private val repository: TareaRepository
) {
    suspend operator fun invoke(idLista: Int, idTarea: Int) {
        repository.eliminarTarea(idLista, idTarea)
    }
}