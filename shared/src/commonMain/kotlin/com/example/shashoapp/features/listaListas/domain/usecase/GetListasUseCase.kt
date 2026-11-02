package com.example.shashoapp.features.listaListas.domain.usecase

import com.example.shashoapp.features.listaListas.domain.model.Lista
import com.example.shashoapp.features.listaListas.domain.repository.ListaRepository

class GetListasUseCase (
    private val repository: ListaRepository
){
    suspend operator fun invoke(): List<Lista>{
        return repository.getListas();
    }
}