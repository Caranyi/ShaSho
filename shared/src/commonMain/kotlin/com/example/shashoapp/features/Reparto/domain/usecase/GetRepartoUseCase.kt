package com.example.shashoapp.features.Reparto.domain.usecase

import com.example.shashoapp.features.Reparto.domain.model.Participante
import com.example.shashoapp.features.Reparto.domain.repository.RepartoRepository
import com.example.shashoapp.features.listaListas.domain.repository.ListaRepository
import com.example.shashoapp.core.SesionUsuario
import com.example.shashoapp.features.listaProductos.domain.usecase.GetProductosListaUseCase

class GetRepartoUseCase(
    private val repository: RepartoRepository,
    private val listaRepository: ListaRepository,
    private val getProductosListaUseCase: GetProductosListaUseCase
){
    suspend operator fun invoke(listaId: Int): List<Participante>{
        val idUsuario = SesionUsuario.getIdUsuario()

        val listas = listaRepository.getListas(idUsuario)
        val lista = listas.find { it.id == listaId }



        val productos = getProductosListaUseCase(listaId)

        val idsUsuarios = (
                lista?.ids_usuarios?.toList().orEmpty() +
                listOfNotNull(lista?.id_creador) +
                productos.mapNotNull { it.idUsuarioMark }
                ).distinct().toIntArray()

        val participantes = repository.getParticipantes(idsUsuarios)

        return participantes.map {
            participante ->
            val total = productos.
            filter {
                it.isChecked && it.idUsuarioMark == participante.usuarioId
            }.sumOf { it.precio * it.cantidad }

            participante.copy(pagado = total)
        }
    }



}