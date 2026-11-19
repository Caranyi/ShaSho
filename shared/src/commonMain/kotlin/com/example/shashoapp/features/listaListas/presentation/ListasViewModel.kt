package com.example.shashoapp.features.listaListas.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shashoapp.core.ApiServidor.eliminarLista
import com.example.shashoapp.features.listaListas.domain.model.Lista
import com.example.shashoapp.features.listaListas.domain.usecase.CompartirListaUseCase
import com.example.shashoapp.features.listaListas.domain.usecase.CrearListaUseCase
import com.example.shashoapp.features.listaListas.domain.usecase.EliminarListaUseCase
import com.example.shashoapp.features.listaListas.domain.usecase.GetListasUseCase
import com.example.shashoapp.features.listaListas.domain.usecase.SincronizarListasUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.shashoapp.features.listaProductos.domain.usecase.GetProductosListaUseCase

class ListasViewModel (
    private val compartirListaUseCase: CompartirListaUseCase,
    private val getListasUseCase: GetListasUseCase,
    private val idUsuario: Int?,
    private val crearListaUseCase: CrearListaUseCase,
    private val sincronizarListasUseCase: SincronizarListasUseCase,
    private val getProductosListaUseCase: GetProductosListaUseCase,
    private val eliminarListaUseCase: EliminarListaUseCase

): ViewModel(){
    private val _state = MutableStateFlow(ListasState())

    val state: StateFlow<ListasState> = _state.asStateFlow()

    init{
        cargarListas()
    }

    fun onAction(action:ListasAction){
        when(action){
            is ListasAction.OnListaClick -> {}
            is ListasAction.AbrirCrearLista -> {
                _state.update { it.copy(mostrarCrear = true) }
            }
            is ListasAction.CerrarCrearLista -> {
                println("ACCIÓN GUARDAR RECIBIDA")
                _state.update { it.copy(mostrarCrear = false) }
            }
            is ListasAction.GuardarLista -> { guardarLista(action.nombre)}
            is ListasAction.EliminarLista -> {eliminarLista(action.idLista)}
        }

    }

    private fun compartirLista(idLista: Int, correo: String) {
        viewModelScope.launch {
            compartirListaUseCase(idLista, correo)
        }
    }

    private fun getProductosLista(idLista: Int) {
        viewModelScope.launch {
            getProductosListaUseCase(idLista)
        }
    }

    private fun eliminarLista(idLista: Int){
        viewModelScope.launch{
            eliminarListaUseCase(idLista)

            _state.update { estadoActual ->
                estadoActual.copy(
                    listas = estadoActual.listas.filterNot{it.id == idLista}
                )
            }
        }
    }

    private fun guardarLista(nombre: String){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true)}

            try {
                println("CREANDO LISTA: $nombre")
                println("USUARIO: $idUsuario")

                crearListaUseCase(nombre, idUsuario)

                println("LISTA GUARDADA EN LOCAL")

                val listas = getListasUseCase(idUsuario)

                println("LISTAS RECUPERADAS: ${listas.size}")


                _state.update { it.copy(listas = listas, isLoading = false, mostrarCrear = false) }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception){
                _state.update { it.copy(isLoading = false)}
            }
        }
    }

    private fun cargarListas(){
        viewModelScope.launch{
            _state.update { it.copy(isLoading = true) }

            try{
                val listasLocales = getListasUseCase(idUsuario)
                _state.update { it.copy(listas = listasLocales, isLoading = false)}
            }catch(e:CancellationException){
                throw e
            }catch (e: Exception){
                _state.update{it.copy(isLoading = false)}
            }

            try{
                sincronizarListasUseCase(idUsuario)
                val listasActualizadas = getListasUseCase(idUsuario)
                _state.update { it.copy(listas = listasActualizadas) }
            }  catch(e:Exception){
                println("No se pudieron sincronizar las listas: ${e.message}")
            }


        }
    }
}