package com.example.shashoapp.features.listaProductos.data.repository

import com.example.shashoapp.core.ApiServidor
import com.example.shashoapp.core.EstadoConexion
import com.example.shashoapp.core.database.guardarArchivo
import com.example.shashoapp.core.database.leerArchivo
import com.example.shashoapp.features.listaProductos.data.model.ProductoDto
import com.example.shashoapp.features.listaProductos.data.model.toDomain
import com.example.shashoapp.features.listaProductos.domain.model.Producto
import com.example.shashoapp.features.listaProductos.domain.repository.ProductoRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json

class ProductoRepositoryImpl : ProductoRepository {
    private val mutexPendientes = Mutex()

    private fun productosPendientes(idLista: Int) = "productos_pendientes_$idLista.json"

    // se va a pedir al servidor (modo online)
    override suspend fun getProductosDeLista(idLista: Int): List<Producto> {
        val archivo = "productos_$idLista.json"

        // ONLINE: actualiza el archivo con lo que tenga el servidor
        if (EstadoConexion.getConexion()) {
            try {
                val texto = ApiServidor.getProductos(idLista)
                guardarArchivo(archivo, texto)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
            }
        }

        val guardados = try {
            Json.decodeFromString<List<ProductoDto>>(
                leerArchivo(archivo) ?: "[]"
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            emptyList()
        }

        val pendientes = mutexPendientes.withLock {
            Json.decodeFromString<List<ProductoDto>>(
                leerArchivo(productosPendientes(idLista)) ?: "[]"
            )
        }

        return (guardados + pendientes).map { it.toDomain() }
    }

    override suspend fun crearProducto(
        idLista: Int,
        nombre: String,
        tipo: String,
        cantidad: Int,
        precio: Double,
        tienda: String
    ) {
        if (EstadoConexion.getConexion()) {
            try {
                val nuevoId = ApiServidor.crearProducto(idLista, nombre, precio, tienda, tipo, cantidad)
                if (nuevoId > 0) {
                    val texto = ApiServidor.getProductos(idLista)
                    guardarArchivo("productos_$idLista.json", texto)
                    return
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
            }
        }
        mutexPendientes.withLock {
            val archivo = productosPendientes(idLista)
            val texto = leerArchivo(archivo) ?: "[]"
            val pendientes = Json.decodeFromString<List<ProductoDto>>(texto)
            val nuevoId = minOf(0, pendientes.minOfOrNull { it.id } ?: 0) - 1

            val nuevoproducto = ProductoDto(
                id = nuevoId,
                nombre = nombre,
                precio = precio,
                tipo = tipo,
                tienda = tienda,
                cantidad = cantidad,
                checked = false
            )
            guardarArchivo(
                archivo,
                Json.encodeToString(pendientes + nuevoproducto)
            )
        }
    }

    override suspend fun actualizaMarcado(
        idLista: Int,
        idProducto: Int,
        marcado: Boolean,
        idUsuarioMark: Int?
    ) {
        if (EstadoConexion.getConexion() && idProducto > 0) {
            try {
                ApiServidor.actualizarMarcado(idLista, idProducto, marcado, idUsuarioMark)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                println("Offline / servidor no disponible al actualizar marcado: ${e.message}")
            }
        }
        mutexPendientes.withLock {
            val archivos = listOf(
                "productos_$idLista.json",
                productosPendientes(idLista)
            )

            for (a in archivos) {
                val texto = leerArchivo(a) ?: continue

                try {
                    val productos = Json.decodeFromString<List<ProductoDto>>(texto)
                    if (productos.none { it.id == idProducto }) continue

                    val actualizados = productos.map { producto ->
                        if (producto.id == idProducto) {
                            producto.copy(
                                checked = marcado,
                                idUsuarioMark = idUsuarioMark
                            )
                        } else {
                            producto
                        }
                    }

                    guardarArchivo(a, Json.encodeToString(actualizados))
                    return@withLock
                } catch (e: Exception) {
                    println("Error al deserializar")
                }
            }
        }
    }

    override suspend fun actualizarCantidad(idLista: Int, idProducto: Int, nuevaCantidad: Int) {
        if (nuevaCantidad < 1)
            return

        if (EstadoConexion.getConexion() && idProducto > 0) {
            try {
                ApiServidor.actualizarCantidad(nuevaCantidad, idLista, idProducto)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
            }
        }

        mutexPendientes.withLock {
            val archivo =
                if (idProducto < 0) {
                    productosPendientes(idLista)
                } else {
                    "productos_$idLista.json"
                }

            val productos = Json.decodeFromString<List<ProductoDto>>(leerArchivo(archivo) ?: "[]")

            val actualizados = productos.map { producto ->
                if (producto.id == idProducto) {
                    producto.copy(cantidad = nuevaCantidad)
                } else {
                    producto
                }
            }

            guardarArchivo(
                archivo,
                Json.encodeToString(actualizados)
            )
        }
    }

    override suspend fun eliminarProducto(idLista: Int, idProducto: Int) {
        if (EstadoConexion.getConexion() && idProducto > 0) {
            try {
                ApiServidor.eliminarProducto(idLista, idProducto)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
            }
        }

        mutexPendientes.withLock {
            val archivo =
                if (idProducto < 0) {
                    productosPendientes(idLista)
                } else {
                    "productos_$idLista.json"
                }

            val productos = Json.decodeFromString<List<ProductoDto>>(leerArchivo(archivo) ?: "[]")
            val elim = productos.filterNot { it.id == idProducto }

            guardarArchivo(archivo, Json.encodeToString(elim))
        }
    }
}