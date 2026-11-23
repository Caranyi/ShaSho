package com.example.shashoapp.features.auth.data.repository

import com.example.shashoapp.core.ApiServidor
import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.EstadoConexion
import com.example.shashoapp.core.SesionUsuario
import com.example.shashoapp.core.TextosApp
import com.example.shashoapp.features.auth.domain.model.Usuario
import com.example.shashoapp.features.auth.domain.repository.AuthRepository
import kotlin.coroutines.cancellation.CancellationException

class AuthRepositoryImpl : AuthRepository {

    // LOGIN (MIRA EN BASE DE DATOS)
    override suspend fun login(correo: String, contrasenya: String): Result<Usuario> {
        val id = try {
            ApiServidor.login(correo, contrasenya)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            return Result.failure(Exception("Error al iniciar sesion"))
        }
        if (id == -1) {
            return Result.failure(IllegalArgumentException("Usuario o Clave incorrectas"))
        }
        // GUARDA EL ID Y NOMBRE DEL USUARIO
        SesionUsuario.setIdUsuario(id)
        AppPreferences.setIdUsuario(id)
        val nombreServidor = SesionUsuario.getNombreUsuario()
        if (nombreServidor.isNotBlank()) {
            AppPreferences.setNombreUsuario(nombreServidor)
        }

        return Result.success(Usuario(id = id, nombre = nombreServidor, correo = correo, contrasenya = contrasenya))
    }

    // REGISTER
    override suspend fun register(nombre: String, correo: String, contrasenya: String): Result<Usuario> {
        val id = try {
            ApiServidor.registro(nombre, correo, contrasenya)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            return Result.failure(Exception("Error al conectar con el servidor"))
        }
        if (id == -1) {
            return Result.failure(IllegalArgumentException("El correo ya está registrado"))
        }
        if (id <= 0) {
            return Result.failure(Exception("No se pudo crear la cuenta"))
        }

        // GUARDA EL ID Y NOMBRE DEL USUARIO
        SesionUsuario.setIdUsuario(id)
        AppPreferences.setIdUsuario(id)
        AppPreferences.setNombreUsuario(nombre)

        return Result.success(Usuario(id = id, nombre = nombre, correo = correo, contrasenya = contrasenya))
    }

    // ACTUALIZAR EL NOMBRE
    override suspend fun actualizarNombre(idUsuario: Int, nuevoNombre: String): Result<String> {
        val textos = TextosApp.get(AppPreferences.idioma.value)
        if (EstadoConexion.getConexion()) {
            val ok = try {
                ApiServidor.actualizarNombreUsuario(idUsuario, nuevoNombre)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                return Result.failure(Exception(textos.errorActualizarNombre))
            }
            if (!ok) {
                return Result.failure(Exception(textos.errorActualizarNombre))
            }
        }
        AppPreferences.setNombreUsuario(nuevoNombre)
        return Result.success(nuevoNombre)
    }
}