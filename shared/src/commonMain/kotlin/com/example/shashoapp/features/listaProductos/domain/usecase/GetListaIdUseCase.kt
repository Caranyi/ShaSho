package com.example.shashoapp.features.listaProductos.domain.usecase

import com.example.shashoapp.Route
import com.example.shashoapp.features.listaListas.data.repository.ListaRepositoryImpl
import com.example.shashoapp.features.listaListas.domain.repository.ListaRepository
import com.example.shashoapp.features.listaListas.domain.model.Lista

class GetListaIdUseCase(
    private val listaRepository: ListaRepository
){
    suspend operator fun invoke(idLista: Int): Lista {
        return listaRepository.getListas(idLista).first { it.id == idLista }
    }
}