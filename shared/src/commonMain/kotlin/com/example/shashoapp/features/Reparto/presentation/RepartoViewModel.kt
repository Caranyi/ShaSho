package com.example.shashoapp.features.Reparto.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shashoapp.features.Reparto.domain.usecase.GetRepartoUseCase
import com.example.shashoapp.features.listaListas.domain.repository.ListaRepository
import com.example.shashoapp.features.listaProductos.presentation.ProductosState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RepartoViewModel(
    private val getRepartoUseCase: GetRepartoUseCase,
    private val listaId: Int
) : ViewModel() {

    private val _state = MutableStateFlow(RepartoState())
    val state: StateFlow<RepartoState> = _state.asStateFlow()

    init{
        cargarReparto()
    }

    fun onAction(action: RepartoAction){
        when(action){
            is RepartoAction.CargarReparto -> cargarReparto()
            is RepartoAction.GuardarReparto -> guardarReparto()
            is RepartoAction.OnPorcentajeChanged -> actualizarPorcentaje(action.usuarioId, action.nuevoPorcentaje)
        }
    }

    private fun actualizarPorcentaje(usuarioId: Int, nuevoValor: Int){
        val participantes = _state.value.participantes

        val porcentajeAct = participantes.find{it.usuarioId == usuarioId}?.porcentaje ?:0

        val maximm = _state.value.porcentajeRestante + porcentajeAct

        val fin = nuevoValor.coerceIn(0, maximm)

        val lMod = _state.value.participantes.toMutableList()

        val i = lMod.indexOfFirst { it.usuarioId == usuarioId }
        if(i != -1){
            lMod[i] = lMod[i].copy(porcentaje = fin)
        }

        _state.update { it.copy(participantes = lMod) }

    }

    private fun guardarReparto(){
        viewModelScope.launch {
            val participantesActuales = _state.value.participantes
        }
    }

    private fun cargarReparto(){
        viewModelScope.launch {

            val listaParticipantes = getRepartoUseCase(listaId)
            _state.update {it.copy(participantes = listaParticipantes)}
        }

    }
}