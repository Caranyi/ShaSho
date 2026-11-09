package com.example.shashoapp.features.Reparto.data.repository

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.shashoapp.features.Reparto.domain.model.Participante
import com.example.shashoapp.features.Reparto.domain.repository.RepartoRepository

class RepartoRepositorioImpl : RepartoRepository{
    override suspend fun getParticipantes(idsUsuarios: IntArray): List<Participante> {
        var porcentajeBase: Int
        if(idsUsuarios.isNotEmpty()){
            porcentajeBase = 100/idsUsuarios.size
        }

        else 
            porcentajeBase = 0


        val listaParticipantes = mutableStateListOf<Participante>()

        for(id in idsUsuarios){
            listaParticipantes.add(Participante(usuarioId = id, nombre = "U $id", porcentaje = porcentajeBase))
        }

        return listaParticipantes
    }
}