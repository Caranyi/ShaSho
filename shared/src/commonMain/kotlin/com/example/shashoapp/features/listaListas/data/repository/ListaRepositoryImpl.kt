package com.example.shashoapp.features.listaListas.data.repository

import com.example.shashoapp.features.listaListas.domain.model.Lista
import com.example.shashoapp.features.listaListas.domain.repository.ListaRepository

class ListaRepositoryImpl : ListaRepository{
    override suspend fun getListas(): List<Lista>{
        return listOf(
            Lista(id = 1, nombre = "Piso", 1, intArrayOf(1, 2, 3), 0),
            Lista(id = 2, nombre = "Viaje", 2, intArrayOf(1, 2, 3), 0),
            Lista(id = 3, nombre = "Compra", 3, intArrayOf(1, 2, 3), 0)
        )
    }
}