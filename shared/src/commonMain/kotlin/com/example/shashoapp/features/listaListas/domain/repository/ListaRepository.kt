package com.example.shashoapp.features.listaListas.domain.repository

import com.example.shashoapp.features.listaListas.domain.model.Lista

interface ListaRepository {
    suspend fun getListas(): List<Lista>
}