package com.example.shashoapp.features.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shashoapp.features.auth.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnNombreChanged -> _state.update { it.copy(nombre = action.nombre) }
            is RegisterAction.OnCorreoChanged -> _state.update { it.copy(correo = action.correo) }
            is RegisterAction.OnContrasenyaChanged -> _state.update { it.copy(contrasenya = action.contrasenya) }
            is RegisterAction.OnRegisterClick -> ejecutarRegister()
            RegisterAction.OnIrALoginClick -> Unit
        }
    }

    private fun ejecutarRegister() {
        val currentNombre = _state.value.nombre
        val currentCorreo = _state.value.correo
        val currentContrasenya = _state.value.contrasenya

        viewModelScope.launch {
            _state.update { it.copy(cargando = true, error = null) }

            registerUseCase(currentNombre, currentCorreo, currentContrasenya)
                .onSuccess {
                    _state.update { state -> state.copy(cargando = false, registroExitoso = true) }
                }
                .onFailure {
                    exception -> _state.update { state -> state.copy(cargando = false, error = exception.message ?: "Error desconocido") }
                }
        }
    }
}