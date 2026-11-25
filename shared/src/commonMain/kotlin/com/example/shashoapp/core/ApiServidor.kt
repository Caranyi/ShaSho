package com.example.shashoapp.core

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.http.parameters
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException
import kotlin.io.encoding.Base64

val httpClient: HttpClient = HttpClient {
    install(HttpTimeout) {
        connectTimeoutMillis = 5_000
        requestTimeoutMillis = 10_000
    }
}

object ApiServidor {

    private val baseUrl: String get() = ServidorConfig.BASE_URL

    // COMPROBAR CONEXION
    suspend fun ping(): Boolean = try {
        httpClient.get("$baseUrl/ping").status.isSuccess()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        false
    }

    // USUARIO Y CONTRASENYA (AUTHREPOSITORYIMPL)
    suspend fun login(correo: String, contrasenya: String): Int {
        val respuesta = httpClient.submitForm(
            url = "$baseUrl/login",
            formParameters = parameters {
                append("correo", correo)
                append("contrasenya", contrasenya)
            }
        )
        val texto = respuesta.bodyAsText().trim()
        if (texto == "-1") return -1
        val partes = texto.split("|", limit = 2)
        val id = partes[0].toIntOrNull() ?: return -1
        if (partes.size > 1 && partes[1].isNotBlank()) {
            SesionUsuario.setNombreUsuario(partes[1])
        }
        return id
    }

    suspend fun registro(nombre: String, correo: String, contrasenya: String): Int {
        val respuesta = httpClient.submitForm(
            url = "$baseUrl/registro",
            formParameters = parameters {
                append("nombre", nombre)
                append("correo", correo)
                append("contrasenya", contrasenya)
            }
        )
        return respuesta.bodyAsText().trim().toInt()
    }

    suspend fun actualizarNombreUsuario(idUsuario: Int, nuevoNombre: String): Boolean {
        val respuesta = httpClient.submitForm(
            url = "$baseUrl/usuario/nombre",
            formParameters = parameters {
                append("idUsuario", idUsuario.toString())
                append("nombre", nuevoNombre)
            }
        )
        return respuesta.bodyAsText().trim() == "true"
    }

    suspend fun actualizarFotoUsuario(idUsuario: Int, bytes: ByteArray?): Boolean {
        val fotoB64 = if (bytes != null) Base64.encode(bytes) else ""
        val respuesta = httpClient.submitForm(
            url = "$baseUrl/usuario/foto",
            formParameters = parameters {
                append("idUsuario", idUsuario.toString())
                append("foto", fotoB64)
            }
        )
        return respuesta.bodyAsText().trim() == "true"
    }

    suspend fun getInfoUsuarios(idsUsuarios: IntArray): Map<Int, UsuarioInfoDto> {
        val idsValidos = idsUsuarios.filter { it > 0 }.distinct()
        if (idsValidos.isEmpty()) return emptyMap()

        val respuesta = httpClient.get("$baseUrl/usuarios/info") {
            parameter("ids", idsValidos.joinToString(","))
        }
        val texto = respuesta.bodyAsText()
        val mapaString = Json.decodeFromString<Map<String, UsuarioInfoDto>>(texto)
        return mapaString.mapNotNull { (k, v) ->
            val id = k.toIntOrNull()
            if (id != null) id to v else null
        }.toMap()
    }

    // LISTAS DEL USUARIO (LISTAREPOSITORYIMPL)
    suspend fun getListas(idUsuario: Int): String {
        val respuesta = httpClient.get("$baseUrl/listas") {
            parameter("idUsuario", idUsuario)
        }
        return respuesta.bodyAsText()
    }

    // PRODUCTOS PARA UNA LISTA (PRODUCTOREPOSITORYIMPL)
    suspend fun getProductos(idLista: Int): String {
        val respuesta = httpClient.get("$baseUrl/productos") {
            parameter("idLista", idLista)
        }
        return respuesta.bodyAsText()
    }


    // TAREAS DE UNA LISTA
    suspend fun getTareas(idLista: Int): String {
        val respuesta = httpClient.get("$baseUrl/tareas") {
            parameter("idLista", idLista)
        }
        return respuesta.bodyAsText()
    }

    suspend fun crearTarea(idLista: Int, nombre: String): Int {
        val respuesta = httpClient.submitForm(
            url = "$baseUrl/tareas",
            formParameters = parameters {
                append("idLista", idLista.toString())
                append("nombre", nombre)
            }
        )
        return respuesta.bodyAsText().trim().toIntOrNull() ?: -1
    }

    suspend fun actualizarTareaCompletada(
        idLista: Int,
        idTarea: Int,
        completada: Boolean
    ): Boolean {
        val respuesta = httpClient.submitForm(
            url = "$baseUrl/tareas/completada",
            formParameters = parameters {
                append("idLista", idLista.toString())
                append("idTarea", idTarea.toString())
                append("completada", completada.toString())
            }
        )
        return respuesta.bodyAsText().trim() == "true"
    }

    suspend fun eliminarTarea(idLista: Int, idTarea: Int): Boolean {
        val respuesta = httpClient.submitForm(
            url = "$baseUrl/tareas/eliminar",
            formParameters = parameters {
                append("idLista", idLista.toString())
                append("idTarea", idTarea.toString())
            }
        )
        return respuesta.bodyAsText().trim() == "true"
    }

    // COMPARTIR LISTA (LISTAREPOSITORYIMPL)
    suspend fun compartirLista(idLista: Int, correo: String): Int {
        val respuesta = httpClient.submitForm(
            url = "$baseUrl/compartir",
            formParameters = parameters {
                append("idLista", idLista.toString())
                append("correo", correo)
            }
        )
        val texto = respuesta.bodyAsText().trim()
        return texto.toIntOrNull() ?: -2
    }

    suspend fun crearLista(
        nombre: String,
        idCreador: Int,
        tipo: String
    ): Int {
        val respuesta = httpClient.submitForm(
            url = "$baseUrl/listas",
            formParameters = parameters {
                append("nombre", nombre)
                append("idCreador", idCreador.toString())
                append("tipo", tipo)
            }
        )

        if (!respuesta.status.isSuccess()) {
            throw Exception("no se ha podido crear lista")
        }
        return respuesta.bodyAsText().toInt()
    }

    suspend fun eliminarLista(idLista: Int): Boolean {
        val respuesta = httpClient.submitForm(
            url = "$baseUrl/listas/eliminar",
            formParameters = parameters {
                append("idLista", idLista.toString())
            }
        )
        return respuesta.bodyAsText().trim() == "true"
    }

    suspend fun crearProducto(
        idLista: Int,
        nombre: String,
        precio: Double,
        tienda: String,
        tipo: String,
        cantidad: Int
    ): Int {
        val respuesta = httpClient.submitForm(
            url = "$baseUrl/productos",
            formParameters = parameters {
                append("idLista", idLista.toString())
                append("nombre", nombre)
                append("precio", precio.toString())
                append("tienda", tienda)
                append("tipo", tipo)
                append("cantidad", cantidad.toString())
            }
        )

        if (!respuesta.status.isSuccess()) {
            throw Exception("No se ha podido crear el producto")
        }

        val texto = respuesta.bodyAsText().trim()
        return texto.toIntOrNull() ?: -1
    }

    suspend fun eliminarProducto(idLista: Int, idProducto: Int): Boolean {
        val respuesta = httpClient.submitForm(
            url = "$baseUrl/productos/eliminar",
            formParameters = parameters {
                append("idLista", idLista.toString())
                append("idProducto", idProducto.toString())
            }
        )
        return respuesta.bodyAsText().trim() == "true"
    }

    suspend fun actualizarMarcado(
        idLista: Int,
        idProducto: Int,
        marked: Boolean,
        idUsuarioMark: Int?
    ) {
        val respuesta = httpClient.submitForm(
            url = "$baseUrl/productos/marcado",
            formParameters = parameters {
                append("idLista", idLista.toString())
                append("idProducto", idProducto.toString())
                append("marcado", marked.toString())
                append("idUsuarioMark", idUsuarioMark.toString())
            }
        )

        if (!respuesta.status.isSuccess()) {
            throw Exception("no se ha podido actualizar")
        }
    }

    // actualizar cantidad de un producto en una lista
    suspend fun actualizarCantidad(cantidad: Int, idLista: Int, idProducto: Int): Boolean {
        val respuesta = httpClient.submitForm(
            url = "$baseUrl/productos/cantidad",
            formParameters = parameters {
                append("cantidad", cantidad.toString())
                append("idLista", idLista.toString())
                append("idProducto", idProducto.toString())
            }
        )
        return respuesta.bodyAsText().trim() == "true"
    }
}

@Serializable
data class UsuarioInfoDto(
    val nombre: String = "",
    val fotoPerfil: String = ""
)