package com.example.shashoapp.features.listaListas.data.repository

import com.example.shashoapp.core.ApiServidor
import com.example.shashoapp.core.EstadoConexion
import com.example.shashoapp.core.SesionUsuario
import com.example.shashoapp.features.listaListas.data.model.ListaDto
import com.example.shashoapp.features.listaListas.data.model.toDomain
import com.example.shashoapp.features.listaListas.domain.model.Lista
import com.example.shashoapp.features.listaListas.domain.repository.ListaRepository
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException
import com.example.shashoapp.core.database.guardarArchivo
import com.example.shashoapp.core.database.leerArchivo
import kotlinx.serialization.encodeToString
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.decodeFromString

class ListaRepositoryImpl : ListaRepository{

    //archivo de las listas
    private fun archivo_listas(idUsuario:Int?) = "listas_${idUsuario ?: 0}.json"
    private val mutexPendientes = Mutex()

    //Necesitamos una frunción que: no habiendo conexión, me guarde las listas en local
        //para subirlas luego al server
    private fun listasPendientes(idUsuario: Int) = "listas_pendientes_$idUsuario.json"

    override suspend fun getListas(idUsuario: Int?): List<Lista>{

        val usuario = idUsuario ?: return emptyList()
        val archivo = archivo_listas(usuario)

        //ONLINE: actualiza el archivo con lo que diga el servidor
        if (EstadoConexion.getConexion()) {
            try {
                val texto = ApiServidor.getListas(usuario)
                guardarArchivo(archivo, texto)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                //No pasa nada, continua con lo guardado en local si falla la red
            }
        }

        //leemos las listas guardadas
        val guardadas = try {
            Json.decodeFromString<List<ListaDto>>(leerArchivo(archivo) ?: "[]")
        }catch (e: CancellationException){
            throw e
        } catch (e: Exception){
            emptyList()
        }

        //Leemos las pendientes y...
        val pendientes = mutexPendientes.withLock {
            Json.decodeFromString<List<ListaDto>>(
                leerArchivo(listasPendientes(usuario)) ?: "[]"
            )
        }

        return (guardadas + pendientes).map {it.toDomain()}
    }

    override suspend fun compartirLista(idLista: Int, correo: String): Result<Unit> {

        //CONEXION
        var codigo: Int = try {
            ApiServidor.compartirLista(idLista, correo)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            return Result.failure(Exception("Error al conectar con la API: ${e.message}"))
        }

        //RESPUESTAS
        if(codigo == 1)
            return Result.success(Unit)
        if (codigo == -1) {
            return Result.failure(IllegalArgumentException("No existe ningún usuario con ese correo"))
        }
        if (codigo == 0) {
            return Result.failure(IllegalArgumentException("Ese usuario ya está en la lista"))
        }
        else{
            return Result.failure(Exception("Error en el servidor al compartir la lista"))
        }
    }

    override suspend fun crearLista(nombre: String, idCreador: Int?) {

        //Necesitamos esos datos
       val usuario = requireNotNull(idCreador){
           "No hay usuario identificado"
       }

        require(nombre.isNotBlank()){
            "El nombre no puede estar vacío"
        }


        mutexPendientes.withLock {
            //leemos las listas pendientes y las almacenamos en pendientes si las hay
            val archivo = listasPendientes(usuario)
            val texto = leerArchivo(archivo) ?: "[]"
            val pendientes = Json.decodeFromString<List<ListaDto>>(texto)

            //valor temporal para id de lista, de momento
            val nuevoId = minOf(0, pendientes.minOfOrNull { it.id } ?: 0) - 1

            //creamos lista nueva, temporal, solo para sincronizar más tarde
            val nuevaLista = ListaDto(
                id = nuevoId,
                nombre = nombre.trim(),
                idCreador = usuario,
                cantidadProductos = 0,
                compartida = false
            )

            guardarArchivo(
                archivo,
                Json.encodeToString(pendientes + nuevaLista)
            )

        }
    }

    override suspend fun eliminarLista(idLista:Int){
        if(EstadoConexion.getConexion() && idLista > 0){
            try{
                ApiServidor.eliminarLista(idLista)
            }catch(e : CancellationException){
                throw e
            }catch(e:Exception){

            }
        }

        mutexPendientes.withLock {
            val idUsuario = SesionUsuario.getIdUsuario() ?: return
            val archivo = "listas_$idUsuario.json"

            val texto = leerArchivo(archivo) ?: return
            val listas = Json.decodeFromString<List<ListaDto>>(texto)
            val filtradas = listas.filterNot { it.id == idLista }

            guardarArchivo(archivo, Json.encodeToString(filtradas))
        }
    }

    override  suspend fun sincronizarPendientes(idUsuario: Int?){
        val archivo = archivo_listas(idUsuario)

        //devolvemos null -> porque sino, no se puede crear
        if(idUsuario == null)
            return
        if(!EstadoConexion.getConexion())
            return
        //sacamos el archivo de pendientes
        val archivoLocal = listasPendientes(idUsuario)

        //si no hay, no lo tocamos
        val t = leerArchivo(archivoLocal) ?: return

        val pendientes = Json.decodeFromString<List<ListaDto>>(t)

        if(pendientes.isEmpty()) return

        for(lista in pendientes) {
            val idDef = ApiServidor.crearLista(lista.nombre, idUsuario)

            check(idDef > 0) {
                "El servidor no devuelve un valor válido"
            }

            val listaCreada = lista.copy(id = idDef)

            val guardadas = Json.decodeFromString<List<ListaDto>>(leerArchivo(archivo) ?: "[]")

            guardarArchivo(
                archivo,
                Json.encodeToString(
                    guardadas.filterNot {it.id == idDef} + listaCreada))

            val restantes = Json.decodeFromString<List<ListaDto>>(leerArchivo(archivoLocal) ?: "[]").filterNot {it.id == lista.id}

            guardarArchivo(
                archivoLocal,
                Json.encodeToString(restantes)
            )
        }


    }



}
