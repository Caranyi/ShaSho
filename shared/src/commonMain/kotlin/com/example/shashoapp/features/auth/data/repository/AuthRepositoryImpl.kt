package com.example.shashoapp.features.auth.data.repository

import com.example.shashoapp.core.ApiServidor
import com.example.shashoapp.core.SesionUsuario
import com.example.shashoapp.features.auth.domain.model.Usuario
import com.example.shashoapp.features.auth.domain.repository.AuthRepository
import kotlin.coroutines.cancellation.CancellationException

class AuthRepositoryImpl : AuthRepository {


    //LOGIN (MIRA EN BASE DE DATOS)
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
        //GUARDA EL ID DEL USUARIO
        SesionUsuario.setIdUsuario(id)

        return Result.success(Usuario(id = id, nombre = "", correo = correo, contraseya = contrasenya))
    }


    //REGISTER (PENDIENTE)
    override suspend fun register(nombre: String, correo: String, contrasenya: String): Result<Usuario> {
        return runCatching {
            Usuario(id = 2, nombre = nombre, correo = correo, contraseya = contrasenya)
        }
    }
}