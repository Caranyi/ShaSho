package com.example.shashoapp.features.auth.domain.usecase

import com.example.shashoapp.features.auth.domain.model.Usuario
import com.example.shashoapp.features.auth.domain.repository.AuthRepository


class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(correo: String, contrasenya: String) : Result<Usuario> {
        if (correo.isBlank() || contrasenya.isBlank()) {
            return Result.failure(IllegalArgumentException("El correo y la contraseña son obligatorios"))
        }
        return authRepository.login(correo.trim(), contrasenya)
    }
}