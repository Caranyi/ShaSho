package com.example.shashoapp.features.Reparto.presentation

import com.example.shashoapp.features.Reparto.domain.model.Participante

data class RepartoState(
    val participantes: List<Participante> = emptyList(),
    val isLoading: Boolean = false,
    val totalLista: Double = 0.0

){
   val totalPagado: Double
       get() = participantes.sumOf { it.pagado }

}