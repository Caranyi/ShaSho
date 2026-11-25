package com.example.shashoapp.features.listaTareas.domain.usecase

import com.example.shashoapp.features.listaTareas.domain.repository.TareaRepository

class CrearTareaUseCase(
    private val repository: TareaRepository
) {
    suspend operator fun invoke(idLista: Int, nombre: String) {
        require(idLista != 0) { "La lista no es válida" }
        require(nombre.isNotBlank()) { "El nombre de la tarea no puede estar vacío" }

        repository.crearTarea(idLista, nombre.trim())
    }
}