package com.example.shashoapp.features.auth.domain.usecase

import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.TextosApp
import com.example.shashoapp.features.auth.domain.model.Usuario
import com.example.shashoapp.features.auth.domain.repository.AuthRepository


class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(nombre: String, correo: String, contrasenya: String) : Result<Usuario> {
        if (nombre.isBlank() || correo.isBlank() || contrasenya.isBlank()) {
            val textos = TextosApp.get(AppPreferences.idioma.value)
            return Result.failure(IllegalArgumentException(textos.errorTodosCamposObligatorios))
        }
        return authRepository.register(nombre.trim(), correo.trim(), contrasenya)
    }
}