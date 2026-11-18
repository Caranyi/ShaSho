package com.example.shashoapp.core

import com.example.shashoapp.features.listaProductos.domain.model.Producto
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.http.parameters
import kotlin.coroutines.cancellation.CancellationException
//para las listas
import io.ktor.client.request.parameter
import io.ktor.http.append

object ApiServidor {

    //la url (servidor)
    private val baseUrl: String get() = ServidorConfig.BASE_URL

    //COMPROBAR CONEXION (aAPP)
    suspend fun ping(): Boolean = try {
        httpClient.get("$baseUrl/ping").status.isSuccess()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        false
    }

    //USUARIO Y CONTRASENYA (AUTREPOSITORYIMPL)
    suspend fun login(correo: String, contrasena: String): Int {
        val respuesta = httpClient.submitForm(
            url = "$baseUrl/login",
            formParameters = parameters {
                append("correo", correo)
                append("contrasena", contrasena)
            }
        )
        return respuesta.bodyAsText().toInt()
    }

    suspend fun registro(nombre: String, correo: String, contrasenya: String): Int{
        val respuesta = httpClient.submitForm(
            url = "$baseUrl/registro",
            formParameters = parameters{
                append("nombre", nombre)
                append("correo", correo)
                append("contrasenya", contrasenya)
            }
        )
        return respuesta.bodyAsText().toInt()
    }

    //LISTAS DEL USUARIO (LISTAREPOSITORYIMPL)
    suspend fun getListas(idUsuario: Int): String {
        val respuesta = httpClient.get("$baseUrl/listas") {
            parameter("idUsuario", idUsuario)
        }
        return respuesta.bodyAsText()
    }


    //PRODUCTOS PARA UNA LISTA (PRODUCTOREPOSITORYIMPL)
    suspend fun getProductos(idLista: Int): String {
        val respuesta = httpClient.get("$baseUrl/productos") {
            parameter("idLista", idLista)
        }
        return respuesta.bodyAsText()
    }

    //COMPARTIR LISTA (LISTAREPOSITORYIMPL)
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
        idCreador: Int
    ): Int {
        val respuesta = httpClient.submitForm(
            url = "$baseUrl/listas",
            formParameters = parameters{
                append("nombre", nombre)
                append("idCreador", idCreador.toString())
            }
        )

        if(!respuesta.status.isSuccess()){
            throw Exception("no se ha podido crear lista")
        }
        return respuesta.bodyAsText().toInt()
    }

    suspend fun eliminarLista(idLista: Int): Boolean{
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

    suspend fun eliminarProducto(idLista: Int, idProducto: Int) : Boolean{
        val respuesta = httpClient.submitForm(
            url = "$baseUrl/productos/eliminar",
            formParameters = parameters{
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

        if(!respuesta.status.isSuccess()){
            throw Exception("no se ha podido actualizar")
        }
    }

}
