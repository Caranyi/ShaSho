package com.example.shashoapp.features.listaTareas.data.repository

import com.example.shashoapp.core.ApiServidor
import com.example.shashoapp.core.EstadoConexion
import com.example.shashoapp.core.database.guardarArchivo
import com.example.shashoapp.core.database.leerArchivo
import com.example.shashoapp.features.listaTareas.data.model.TareaDto
import com.example.shashoapp.features.listaTareas.data.model.toDomain
import com.example.shashoapp.features.listaTareas.domain.model.Tarea
import com.example.shashoapp.features.listaTareas.domain.repository.TareaRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TareaRepositoryImpl : TareaRepository {

    private val mutexPendientes = Mutex()

    private fun archivoTareas(idLista: Int) =
        "tareas_$idLista.json"

    private fun tareasPendientes(idLista: Int) =
        "tareas_pendientes_$idLista.json"

    override suspend fun getTareas(idLista: Int): List<Tarea> {
        val archivo = archivoTareas(idLista)

        if (EstadoConexion.getConexion()) {
            try {
                val respuesta = ApiServidor.getTareas(idLista)
                guardarArchivo(archivo, respuesta)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Si falla el servidor, usamos la caché local
            }
        }

        val guardadas = try {
            Json.decodeFromString<List<TareaDto>>(
                leerArchivo(archivo) ?: "[]"
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            emptyList()
        }

        val pendientes = mutexPendientes.withLock {
            try {
                Json.decodeFromString<List<TareaDto>>(
                    leerArchivo(tareasPendientes(idLista)) ?: "[]"
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                emptyList()
            }
        }

        return (guardadas + pendientes).map { it.toDomain() }
    }

    override suspend fun crearTarea(idLista: Int, nombre: String) {
        if (EstadoConexion.getConexion()) {
            try {
                val nuevoId = ApiServidor.crearTarea(idLista, nombre)

                if (nuevoId > 0) {
                    try {
                        val respuesta = ApiServidor.getTareas(idLista)
                        guardarArchivo(archivoTareas(idLista), respuesta)
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        // se crea, la caché se actualizará al volver a cargar
                    }
                    return
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Si falla el servidor, guardamos la tarea como pendiente
            }
        }

        mutexPendientes.withLock {
            val archivo = tareasPendientes(idLista)

            val pendientes = try {
                Json.decodeFromString<List<TareaDto>>(
                    leerArchivo(archivo) ?: "[]"
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                emptyList()
            }

            val nuevoId = minOf(
                0,
                pendientes.minOfOrNull { it.id } ?: 0
            ) - 1

            val nuevaTarea = TareaDto(
                id = nuevoId,
                nombre = nombre.trim(),
                completada = false
            )

            guardarArchivo(
                archivo,
                Json.encodeToString(pendientes + nuevaTarea)
            )
        }
    }

    override suspend fun actualizarCompletada(
        idLista: Int,
        idTarea: Int,
        completada: Boolean
    ) {
        if (EstadoConexion.getConexion() && idTarea > 0) {
            try {
                ApiServidor.actualizarTareaCompletada(
                    idLista,
                    idTarea,
                    completada
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Aplicamos el cambio local si no se puede contactar al servidor
            }
        }

        mutexPendientes.withLock {
            val archivos = listOf(
                archivoTareas(idLista),
                tareasPendientes(idLista)
            )

            for (archivo in archivos) {
                val texto = leerArchivo(archivo) ?: continue

                try {
                    val tareas = Json.decodeFromString<List<TareaDto>>(texto)
                    if (tareas.none { it.id == idTarea }) continue

                    val actualizadas = tareas.map { tarea ->
                        if (tarea.id == idTarea) {
                            tarea.copy(completada = completada)
                        } else {
                            tarea
                        }
                    }

                    guardarArchivo(
                        archivo,
                        Json.encodeToString(actualizadas)
                    )
                    return@withLock
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    // Prueba el otro archivo si este no se puede leer
                }
            }
        }
    }

    override suspend fun eliminarTarea(idLista: Int, idTarea: Int) {
        if (EstadoConexion.getConexion() && idTarea > 0) {
            try {
                ApiServidor.eliminarTarea(idLista, idTarea)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Quitamos la tarea localmente si no se puede contactar al servidor
            }
        }

        mutexPendientes.withLock {
            val archivo = if (idTarea < 0) {
                tareasPendientes(idLista)
            } else {
                archivoTareas(idLista)
            }

            val tareas = try {
                Json.decodeFromString<List<TareaDto>>(
                    leerArchivo(archivo) ?: "[]"
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                emptyList()
            }

            val restantes = tareas.filterNot { it.id == idTarea }

            guardarArchivo(
                archivo,
                Json.encodeToString(restantes)
            )
        }
    }
}