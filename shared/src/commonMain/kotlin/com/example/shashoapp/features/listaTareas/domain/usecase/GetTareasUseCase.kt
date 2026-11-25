package com.example.shashoapp.features.listaTareas.domain.usecase

import com.example.shashoapp.features.listaTareas.domain.model.Tarea
import com.example.shashoapp.features.listaTareas.domain.repository.TareaRepository

class GetTareasUseCase(
    private val repository: TareaRepository
) {
    suspend operator fun invoke(idLista: Int): List<Tarea> =
        repository.getTareas(idLista)
}