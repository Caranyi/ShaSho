package com.example.shashoapp.features.listaListas.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shashoapp.features.listaListas.domain.usecase.GetListasUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListasViewModel (
    private val getListasUseCase: GetListasUseCase,
    private val idUsuario: Int?
): ViewModel(){
    private val _state = MutableStateFlow(ListasState())

    val state: StateFlow<ListasState> = _state.asStateFlow()

    init{
        cargarListas()
    }

    fun onAction(action:ListasAction){
        when(action){
            is ListasAction.OnListaClick -> { }
            is ListasAction.AbrirCrearLista -> {/**/}
        }

    }


    private fun cargarListas(){
        viewModelScope.launch{
            _state.update { it.copy(isLoading = true) }
            val listas = getListasUseCase(idUsuario)
            _state.update { it.copy(listas = listas, isLoading = false)}
        }
    }
}