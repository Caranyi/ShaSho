package com.example.shashoapp.features.Ajustes.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.example.shashoapp.core.EstadoConexion
import com.example.shashoapp.core.SesionUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AjustesViewModel : ViewModel() {

    private val _state = MutableStateFlow(AjustesState())
    val state: StateFlow<AjustesState> = _state.asStateFlow()

    init{
        _state.update {
            it.copy(
                idUsuario = SesionUsuario.getIdUsuario(),
                enLinea = EstadoConexion.getConexion()
            )
        }
    }

    fun onAction(action : AjustesAction){
        when(action){
            is AjustesAction.onActivarModoOscuro -> {
                _state.update { it.copy(modoOscuro = action.activado) }
            }
            is AjustesAction.onActivarNotificaciones -> {
                _state.update {it.copy(notificacionesActivas = action.activadas)}
            }
            is AjustesAction.onCerrarSesionClick -> {
                SesionUsuario.cerrarSesion();
            }
        }
    }

}