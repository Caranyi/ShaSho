package com.example.shashoapp.features.listaListas.domain.repository

import com.example.shashoapp.features.listaListas.domain.model.Lista

interface ListaRepository {
    //pasa el id del usuario que hay guardado (o no)
    suspend fun getListas(idUsuario: Int?): List<Lista>
}