package com.example.shashoapp.features.listaListas.domain.usecase

import com.example.shashoapp.features.listaListas.domain.repository.ListaRepository

class EliminarListaUseCase(
    private val listaRepository: ListaRepository
) {
    suspend operator fun invoke(idLista:Int){
        listaRepository.eliminarLista(idLista)
    }
}