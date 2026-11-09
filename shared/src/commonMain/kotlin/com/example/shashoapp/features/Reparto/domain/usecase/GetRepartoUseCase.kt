package com.example.shashoapp.features.Reparto.domain.usecase

import com.example.shashoapp.features.Reparto.domain.model.Participante
import com.example.shashoapp.features.Reparto.domain.repository.RepartoRepository
import com.example.shashoapp.features.listaListas.domain.repository.ListaRepository

class GetRepartoUseCase(
    private val repository: RepartoRepository,
    private val listaRepository: ListaRepository
){
    suspend operator fun invoke(listaId: Int): List<Participante>{
        val listas = listaRepository.getListas()
        val lista = listas.find { it.id == listaId }

        val idsUsuarios = lista?.ids_usuarios ?: intArrayOf()

        return repository.getParticipantes(idsUsuarios)
    }

}