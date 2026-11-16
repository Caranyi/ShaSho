package com.example.shashoapp.features.listaListas.domain.usecase

import com.example.shashoapp.features.listaListas.domain.repository.ListaRepository

class CrearListaUseCase(
    private val repository: ListaRepository
) {
    suspend operator fun invoke(
        nombre: String,
        idCreador: Int?
    ){
        require(nombre.isNotBlank()){
            "´Nombre´ no puede estar en blanco"
        }

        requireNotNull(idCreador) {
            "´IdUsuario no se encuentra"
        }

        repository.crearLista(nombre.trim(), idCreador)
    }
}