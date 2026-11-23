package com.example.shashoapp.features.Ajustes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.EstadoConexion
import com.example.shashoapp.core.SesionUsuario
import com.example.shashoapp.features.auth.domain.usecase.ActualizarNombreUsuarioUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AjustesViewModel(
    private val actualizarNombreUsuarioUseCase: ActualizarNombreUsuarioUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(
        AjustesState(
            idUsuario = SesionUsuario.getIdUsuario(),
            nombreUsuario = AppPreferences.nombreUsuario.value.ifBlank { SesionUsuario.getNombreUsuario() },
            fotoPerfilBytes = AppPreferences.fotoPerfilBytes.value,
            enLinea = EstadoConexion.getConexion(),
            notificacionesActivas = if (EstadoConexion.getConexion()) AppPreferences.notificacionesActivas.value else false,
            modoOscuro = AppPreferences.modoOscuro.value,
            idiomaActual = AppPreferences.idioma.value
        )
    )
    val state: StateFlow<AjustesState> = _state.asStateFlow()

    fun onAction(action: AjustesAction) {
        when (action) {
            is AjustesAction.onActivarNotificaciones -> {
                _state.update { it.copy(notificacionesActivas = action.activadas) }
                viewModelScope.launch {
                    AppPreferences.setNotificaciones(action.activadas)
                }
            }
            is AjustesAction.onActivarModoOscuro -> {
                _state.update { it.copy(modoOscuro = action.activado) }
                viewModelScope.launch {
                    AppPreferences.setModoOscuro(action.activado)
                }
            }
            is AjustesAction.onToggleMenuIdioma -> {
                _state.update { it.copy(menuIdiomaAbierto = !it.menuIdiomaAbierto) }
            }
            is AjustesAction.onCerrarMenuIdioma -> {
                _state.update { it.copy(menuIdiomaAbierto = false) }
            }
            is AjustesAction.onCambiarIdioma -> {
                _state.update {
                    it.copy(
                        idiomaActual = action.codigoIdioma,
                        menuIdiomaAbierto = false
                    )
                }
                viewModelScope.launch {
                    AppPreferences.setIdioma(action.codigoIdioma)
                }
            }
            is AjustesAction.onFotoPerfilSeleccionada -> {
                _state.update { it.copy(fotoPerfilBytes = action.bytes) }
                viewModelScope.launch {
                    AppPreferences.setFotoPerfil(action.bytes)
                }
            }
            is AjustesAction.onAbrirDialogoNombre -> {
                _state.update {
                    it.copy(
                        mostrarDialogoNombre = true,
                        nombreTemporal = it.nombreUsuario,
                        errorNombre = null
                    )
                }
            }
            is AjustesAction.onCerrarDialogoNombre -> {
                _state.update {
                    it.copy(
                        mostrarDialogoNombre = false,
                        errorNombre = null
                    )
                }
            }
            is AjustesAction.onNombreTemporalChanged -> {
                _state.update {
                    it.copy(
                        nombreTemporal = action.texto,
                        errorNombre = null
                    )
                }
            }
            is AjustesAction.onConfirmarCambioNombre -> ejecutarCambioNombre()
            is AjustesAction.onCerrarSesionClick -> {
                SesionUsuario.cerrarSesion()
                viewModelScope.launch {
                    AppPreferences.setIdUsuario(null)
                }
                _state.update { it.copy(idUsuario = null) }
            }
        }
    }

    private fun ejecutarCambioNombre() {
        val id = _state.value.idUsuario
        val nuevoNombre = _state.value.nombreTemporal
        viewModelScope.launch {
            _state.update { it.copy(guardandoNombre = true, errorNombre = null) }
            actualizarNombreUsuarioUseCase(id, nuevoNombre)
                .onSuccess { nombreGuardado ->
                    _state.update {
                        it.copy(
                            nombreUsuario = nombreGuardado,
                            mostrarDialogoNombre = false,
                            guardandoNombre = false,
                            errorNombre = null
                        )
                    }
                }
                .onFailure { err ->
                    _state.update {
                        it.copy(
                            guardandoNombre = false,
                            errorNombre = err.message
                        )
                    }
                }
        }
    }
}