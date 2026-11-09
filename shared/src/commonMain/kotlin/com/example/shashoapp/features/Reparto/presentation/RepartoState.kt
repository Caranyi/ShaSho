package com.example.shashoapp.features.Reparto.presentation

import com.example.shashoapp.features.Reparto.domain.model.Participante

data class RepartoState(
    val participantes: List<Participante> = emptyList(),
    val isLoading: Boolean = false,

){
    val getTotalPorcentaje: Int
        get() = participantes.sumOf { it.porcentaje}
    val porcentajeRestante: Int
        get() = 100 - getTotalPorcentaje

}