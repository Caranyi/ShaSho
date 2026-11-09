package com.example.shashoapp.features.Reparto.domain.repository

import com.example.shashoapp.features.Reparto.domain.model.Participante

interface RepartoRepository {
    suspend fun getParticipantes(idsUsuarios: IntArray): List<Participante>
}