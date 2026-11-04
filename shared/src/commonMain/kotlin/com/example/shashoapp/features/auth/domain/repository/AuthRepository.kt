package com.example.shashoapp.features.auth.domain.repository

import com.example.shashoapp.features.auth.domain.model.Usuario

interface AuthRepository {
    suspend fun login(correo: String, contrasenya: String) : Result<Usuario>
    suspend fun register(nombre: String, correo: String, contrasenya: String) : Result<Usuario>
}