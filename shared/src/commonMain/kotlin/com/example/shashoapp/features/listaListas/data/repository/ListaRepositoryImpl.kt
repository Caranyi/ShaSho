package com.example.shashoapp.features.listaListas.data.repository

import com.example.shashoapp.core.ApiServidor
import com.example.shashoapp.core.EstadoConexion
import com.example.shashoapp.features.listaListas.data.model.ListaDto
import com.example.shashoapp.features.listaListas.data.model.toDomain
import com.example.shashoapp.features.listaListas.domain.model.Lista
import com.example.shashoapp.features.listaListas.domain.repository.ListaRepository
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

class ListaRepositoryImpl : ListaRepository{
    /*
    override suspend fun getListas(idUsuario: Int?): List<Lista>{
        return listOf(
            Lista(id = 1, nombre = "Piso", 1, intArrayOf(1, 2, 3), 0),
            Lista(id = 2, nombre = "Viaje", 2, intArrayOf(1, 2, 3), 0),
            Lista(id = 3, nombre = "Compra", 3, intArrayOf(1, 2, 3), 0)
            )
    }

     */
    override suspend fun getListas(idUsuario: Int?): List<Lista>{

        //OFFLINE/USUARIO NULL
        if (!EstadoConexion.getConexion() || idUsuario == null) {
            return emptyList()
        }


        //ONLINE
        //(lista vacia si hay cualquier error)
        val texto = try {
            ApiServidor.getListas(idUsuario)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            return emptyList()
        }
        //convierte en lista de listas la respuesta del servidor
        return Json.decodeFromString<List<ListaDto>>(texto).map { it.toDomain() }
    }
}