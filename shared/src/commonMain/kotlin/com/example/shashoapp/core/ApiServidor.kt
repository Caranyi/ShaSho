package com.example.shashoapp.core

import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.http.parameters
import kotlin.coroutines.cancellation.CancellationException
//para las listas
import io.ktor.client.request.parameter

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

    //LISTAS DEL USUARIO (LISTAREPOSITORYIMPL)
    suspend fun getListas(idUsuario: Int): String {
        val respuesta = httpClient.get("$baseUrl/listas") {
            parameter("idUsuario", idUsuario)
        }
        return respuesta.bodyAsText()
    }

}
