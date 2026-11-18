package com.example.shashoapp.features.auth.domain.usecase

import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.TextosApp
import com.example.shashoapp.features.auth.domain.model.Usuario
import com.example.shashoapp.features.auth.domain.repository.AuthRepository


class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(correo: String, contrasenya: String) : Result<Usuario> {
        if (correo.isBlank() || contrasenya.isBlank()) {
            val textos = TextosApp.get(AppPreferences.idioma.value)
            return Result.failure(IllegalArgumentException(textos.errorCamposLoginObligatorios))
        }
        return authRepository.login(correo.trim(), contrasenya)
    }
}