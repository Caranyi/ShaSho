package com.example.shashoapp.features.auth.domain.usecase

import com.example.shashoapp.features.auth.domain.model.Usuario
import com.example.shashoapp.features.auth.domain.repository.AuthRepository


class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(nombre: String, correo: String, contrasenya: String) : Result<Usuario> {
        if (nombre.isBlank() || correo.isBlank() || contrasenya.isBlank()) {
            return Result.failure(IllegalArgumentException("Todos los campos son obligatorios"))
        }
        return authRepository.register(nombre.trim(), correo.trim(), contrasenya)
    }
}