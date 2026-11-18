package com.example.shashoapp.features.listaListas.domain.usecase

import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.TextosApp
import com.example.shashoapp.features.listaListas.domain.repository.ListaRepository

class CrearListaUseCase(
    private val repository: ListaRepository
) {
    suspend operator fun invoke(
        nombre: String,
        idCreador: Int?
    ){
        val textos = TextosApp.get(AppPreferences.idioma.value)

        require(nombre.isNotBlank()){
            textos.errorNombreListaVacio
        }

        requireNotNull(idCreador) {
            textos.errorUsuarioNoEncontrado
        }

        repository.crearLista(nombre.trim(), idCreador)
    }
}