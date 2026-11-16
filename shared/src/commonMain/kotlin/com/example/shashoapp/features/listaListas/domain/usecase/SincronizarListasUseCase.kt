package com.example.shashoapp.features.listaListas.domain.usecase

import com.example.shashoapp.features.listaListas.domain.repository.ListaRepository

class SincronizarListasUseCase(
    private val repository: ListaRepository
) {
    suspend operator fun invoke(idUsuario: Int?){
        repository.sincronizarPendientes(idUsuario)
    }
}