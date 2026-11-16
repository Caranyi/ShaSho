package com.example.shashoapp.features.Reparto.data.repository

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.shashoapp.features.Reparto.domain.model.Participante
import com.example.shashoapp.features.Reparto.domain.repository.RepartoRepository

class RepartoRepositorioImpl : RepartoRepository{
    override suspend fun getParticipantes(idsUsuarios: IntArray): List<Participante> {
        return idsUsuarios.map {
            id->
            Participante(usuarioId = id, nombre = "U $id", pagado = 0.0)
        }
    }
}