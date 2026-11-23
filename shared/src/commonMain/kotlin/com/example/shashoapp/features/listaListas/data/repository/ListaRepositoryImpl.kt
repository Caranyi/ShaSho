package com.example.shashoapp.features.listaListas.data.repository

import androidx.compose.ui.text.toLowerCase
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

    //Necesitamos una función que: no habiendo conexión, me guarde las listas en local
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

        //Leemos las pendientes
        val pendientes = mutexPendientes.withLock {
            Json.decodeFromString<List<ListaDto>>(
                leerArchivo(listasPendientes(usuario)) ?: "[]"
            )
        }

        return (guardadas + pendientes).map {it.toDomain()}
    }

    override suspend fun compartirLista(idLista: Int, correo: String): Result<Unit> {
        val correoLimpio = correo.trim().lowercase()

        //CONEXION
        var codigo: Int = try {
            ApiServidor.compartirLista(idLista, correoLimpio)
        } catch (e: Exception) {
            return Result.failure(Exception("Error al conectar con el servidor: ${e.message}"))
        }

        //RESPUESTAS
        if(codigo == 1)
            return Result.success(Unit)
        else if (codigo == -1) {
            return Result.failure(IllegalArgumentException("No existe ningún usuario con ese correo"))
        }
        else if (codigo == 0) {
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

    override suspend fun eliminarLista(idLista: Int) {
        if (EstadoConexion.getConexion() && idLista > 0) {
            try {
                ApiServidor.eliminarLista(idLista)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {

            }
        }

        mutexPendientes.withLock {
            val idUsuario = SesionUsuario.getIdUsuario() ?: -1

            val archivos = listOf(
                "listas_$idUsuario.json",
                "listas_pendientes_$idUsuario.json",
                "listas_-1.json",
                "listas_pendientes_-1.json"
            )

            for (archivo in archivos) {
                val texto = leerArchivo(archivo) ?: continue

                try {
                    val listas = Json.decodeFromString<List<ListaDto>>(texto)

                    if (listas.any { it.id == idLista }) {
                        val filtradas = listas.filterNot { it.id == idLista }
                        guardarArchivo(archivo, Json.encodeToString(filtradas))
                    }
                } catch (e: Exception) {
                    println("Error al procesar el borrado")
                }
            }
        }
    }

    override  suspend fun sincronizarPendientes(idUsuario: Int?){
        val archivo = archivo_listas(idUsuario)

        //devolvemos null -> porque sino, no se puede crear
        if(idUsuario == null)
            return

        if(idUsuario <= 0)
            return
        if(!EstadoConexion.getConexion())
            return

        //cogemos del id usuario y de idusuario -1
        val archivoUsuario = listasPendientes(idUsuario)
        val archivoSinSesion = listasPendientes(-1)

        val pendientesUsuario = Json.decodeFromString<List<ListaDto>>(leerArchivo(archivoUsuario) ?: "[]")
        val pendientesSinSesion = Json.decodeFromString<List<ListaDto>>(leerArchivo(archivoSinSesion) ?: "[]")

        //union de ambos pendientes
        val pendientes = pendientesUsuario + pendientesSinSesion

        if(pendientes.isEmpty()) return

        for(lista in pendientes) {
            //se manda con idUsuario (el usuario conectado), aunque la lista se creara con -1
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

            //se quita la lista de las pendientes
            val archivoLocal: String
            if (pendientesUsuario.contains(lista)) {
                archivoLocal = archivoUsuario
            } else {
                archivoLocal = archivoSinSesion
            }

            val restantes = Json.decodeFromString<List<ListaDto>>(leerArchivo(archivoLocal) ?: "[]").filterNot {it.id == lista.id}

            guardarArchivo(
                archivoLocal,
                Json.encodeToString(restantes)
            )
        }


    }



}