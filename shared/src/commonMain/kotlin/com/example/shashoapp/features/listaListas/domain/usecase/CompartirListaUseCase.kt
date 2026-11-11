package com.example.shashoapp.features.listaListas.domain.usecase

import com.example.shashoapp.features.listaListas.domain.repository.ListaRepository


class CompartirListaUseCase(
    private val repository: ListaRepository
) {
    suspend operator fun invoke(idLista: Int, correo: String): Result<Unit> {
        return repository.compartirLista(idLista, correo.trim())
    }
}
