package com.example.shashoapp.features.Ajustes.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.EstadoConexion
import com.example.shashoapp.core.SesionUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AjustesViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        AjustesState(
            idUsuario = SesionUsuario.getIdUsuario(),
            enLinea = EstadoConexion.getConexion(),
            notificacionesActivas = AppPreferences.notificacionesActivas.value,
            idiomaActual = AppPreferences.idioma.value
        ))
    val state: StateFlow<AjustesState> = _state.asStateFlow()

    fun onAction(action : AjustesAction){
        when(action){
            is AjustesAction.onActivarNotificaciones -> {
                _state.update {it.copy(notificacionesActivas = action.activadas)}
                viewModelScope.launch {
                    AppPreferences.setNotificaciones(action.activadas)
                }
            }
            is AjustesAction.onToggleMenuIdioma -> {
                _state.update { it.copy(menuIdiomaAbierto = !it.menuIdiomaAbierto) }
            }
            is AjustesAction.onCerrarMenuIdioma -> {
                _state.update { it.copy(menuIdiomaAbierto = false) }
            }
            is AjustesAction.onCambiarIdioma -> {
                _state.update { it.copy(
                    idiomaActual = action.codigoIdioma,
                    menuIdiomaAbierto = false
                ) }
                viewModelScope.launch {
                    AppPreferences.setIdioma(action.codigoIdioma)
                }
            }
            is AjustesAction.onCerrarSesionClick -> {
                SesionUsuario.cerrarSesion();
            }
        }
    }

}