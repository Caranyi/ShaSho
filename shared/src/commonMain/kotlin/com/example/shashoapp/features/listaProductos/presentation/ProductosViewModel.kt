package com.example.shashoapp.features.listaProductos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shashoapp.features.listaProductos.domain.model.Producto
import com.example.shashoapp.features.listaProductos.domain.usecase.GetProductosDisponiblesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ProductosViewModel(
    private val getProductosDisponiblesUseCase: GetProductosDisponiblesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductosState())
    val state: StateFlow<ProductosState> = _state.asStateFlow()

    init {
        cargarProductosDisponibles()
    }

    fun onAction(action:ProductosAction){
        when(action){
            ProductosAction.AbrirMenuCrear -> _state.update { it.copy(showCreateMenu = true) }
            ProductosAction.CerrarMenuCrear -> _state.update { it.copy(showCreateMenu = false) }

            is ProductosAction.ToggleChecked -> toggleChecked(action.index)
            is ProductosAction.IncrementarCantidad -> incrementarCantidad(action.index)
            is ProductosAction.DecrementarCantidad -> decrementarCantidad(action.index)
            is ProductosAction.ConfirmarProductos -> confirmarProductos(action.nuevosProductos)
        }
    }

    private fun cargarProductosDisponibles() {
        viewModelScope.launch {
            val disponibles = getProductosDisponiblesUseCase()
            _state.update { it.copy(productosDisponibles = disponibles) }
        }
    }

    private fun toggleChecked(i: Int) {
        _state.update { actual ->
            val lista = actual.productosLista.toMutableList()
            if (i in lista.indices) {
                val prod = lista[i]
                lista[i] = prod.copy(isChecked = !prod.isChecked)
            }
            actual.copy(productosLista = lista)
        }
    }

    private fun incrementarCantidad(i: Int) {
        _state.update { actual ->
            val lista = actual.productosLista.toMutableList()
            if (i in lista.indices) {
                val prod = lista[i]
                lista[i] = prod.copy(cantidad = prod.dameCantidad + 1)
            }
            actual.copy(productosLista = lista)
        }
    }

    private fun decrementarCantidad(i: Int) {
        _state.update { actual ->
            val lista = actual.productosLista.toMutableList()
            if (i in lista.indices) {
                val prod = lista[i]
                if (prod.dameCantidad > 1) {
                    lista[i] = prod.copy(cantidad = prod.dameCantidad - 1)
                } else {
                    lista.removeAt(i)
                }
            }
            actual.copy(productosLista = lista)
        }
    }

    private fun confirmarProductos(nuevos: List<Producto>) {
        _state.update { actual ->
            val lista = actual.productosLista.toMutableList()
            nuevos.forEach { nuevoProd ->
                val i = lista.indexOfFirst { it.id == nuevoProd.id }
                if (i != -1) {
                    val existente = lista[i]
                    lista[i] = existente.copy(cantidad = existente.dameCantidad + nuevoProd.dameCantidad)
                } else {
                    lista.add(nuevoProd.copy(isChecked = false))
                }
            }
            actual.copy(productosLista = lista, showCreateMenu = false)
        }
    }
}