package com.example.shashoapp.features.listaListas.data.repository

import com.example.shashoapp.core.ApiServidor
import com.example.shashoapp.core.EstadoConexion
import com.example.shashoapp.features.listaListas.data.model.ListaDto
import com.example.shashoapp.features.listaListas.data.model.toDomain
import com.example.shashoapp.features.listaListas.domain.model.Lista
import com.example.shashoapp.features.listaListas.domain.repository.ListaRepository
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException
import com.example.shashoapp.core.database.guardarArchivo
import com.example.shashoapp.core.database.leerArchivo

class ListaRepositoryImpl : ListaRepository{

    //archivo de las listas
    private val archivo = "listas.json"

    override suspend fun getListas(idUsuario: Int?): List<Lista>{

        //ONLINE: actualiza el archivo con lo que diga el servidor
        if (EstadoConexion.getConexion()) {
            try {
                var texto = "[]"
                //se revisa que haya un idUsuario
                if (idUsuario != null) {
                    texto = ApiServidor.getListas(idUsuario)
                }
                //actualiza el archivo
                guardarArchivo(archivo, texto)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                //No pasa nada
            }
        }

        //SIEMPRE: lee el archivo
        //(lista vacia si no existe o esta mal)
        val texto = leerArchivo(archivo)
        if (texto == null) {
            return emptyList()
        }
        try {
            val listasDto = Json.decodeFromString<List<ListaDto>>(texto)
            return listasDto.map { it.toDomain() }
        } catch (e: Exception) {
            return emptyList()
        }

    }
}