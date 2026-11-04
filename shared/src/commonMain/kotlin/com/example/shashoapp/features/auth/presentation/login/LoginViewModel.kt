package com.example.shashoapp.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shashoapp.features.auth.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()
    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnCorreoChanged -> _state.update { it.copy(correo = action.correo) }
            is LoginAction.OnContrasenyaChanged -> _state.update { it.copy(contrasenya = action.contrasenya)}
            is LoginAction.OnLoginClick -> ejecutarLogin()
            LoginAction.OnIrARegistroClick -> Unit
        }
    }

    private fun ejecutarLogin() {
        val currentCorreo = _state.value.correo
        val currentContrasenya = _state.value.contrasenya

        viewModelScope.launch {
            _state.update { it.copy(cargando = true, error = null) }

            loginUseCase(currentCorreo, currentContrasenya)
                .onSuccess {
                    _state.update { state -> state.copy(cargando = false, loginExitoso = true) }
                }
                .onFailure {
                    exception -> _state.update { state -> state.copy(cargando = false, error = exception.message ?: "Error desconocido") }
                }
        }
    }
}