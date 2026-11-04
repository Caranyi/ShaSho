package com.example.shashoapp.features.auth.data.repository

import com.example.shashoapp.features.auth.domain.model.Usuario
import com.example.shashoapp.features.auth.domain.repository.AuthRepository

class AuthRepositoryImpl : AuthRepository {
    override suspend fun login(correo: String, contrasenya: String): Result<Usuario> {
        return runCatching {
            Usuario(id = 1, nombre = "Usuario ShaSho", correo = correo, contraseya = contrasenya)
        }
    }

    override suspend fun register(nombre: String, correo: String, contrasenya: String): Result<Usuario> {
        return runCatching {
            Usuario(id = 2, nombre = nombre, correo = correo, contraseya = contrasenya)
        }
    }
}