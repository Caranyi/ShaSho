package com.example.shashoapp.features.listaTareas.domain.usecase

import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.TextosApp
import com.example.shashoapp.features.listaTareas.domain.repository.TareaRepository

class CrearTareaUseCase(
    private val repository: TareaRepository
) {
    suspend operator fun invoke(
        idLista: Int,
        nombre: String,
        imagenBytes: ByteArray? = null
    ) {
        val textos = TextosApp.get(AppPreferences.idioma.value)
        require(idLista != 0) { textos.errorListaNoValida }
        require(nombre.isNotBlank()) { textos.errorNombreTareaVacio }

        repository.crearTarea(idLista, nombre.trim(), imagenBytes)
    }
}