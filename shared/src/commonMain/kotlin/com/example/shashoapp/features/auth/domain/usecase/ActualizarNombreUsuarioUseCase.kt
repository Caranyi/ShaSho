package com.example.shashoapp.features.auth.domain.usecase

import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.TextosApp
import com.example.shashoapp.features.auth.domain.repository.AuthRepository

class ActualizarNombreUsuarioUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(idUsuario: Int?, nuevoNombre: String): Result<String> {
        val textos = TextosApp.get(AppPreferences.idioma.value)
        val limpio = nuevoNombre.trim()
        if (limpio.isBlank()) {
            return Result.failure(IllegalArgumentException(textos.errorNombreUsuarioVacio))
        }
        if (idUsuario == null || idUsuario <= 0) {
            AppPreferences.setNombreUsuario(limpio)
            return Result.success(limpio)
        }
        return repository.actualizarNombre(idUsuario, limpio)
    }
}