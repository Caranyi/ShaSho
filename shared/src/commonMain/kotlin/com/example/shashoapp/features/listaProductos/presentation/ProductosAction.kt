package com.example.shashoapp.features.listaProductos.presentation

import com.example.shashoapp.features.listaProductos.domain.model.Producto

sealed interface ProductosAction{
    data object AbrirMenuCrear: ProductosAction
    data object CerrarMenuCrear: ProductosAction
    data class ToggleChecked(val index:Int) : ProductosAction
    data class IncrementarCantidad(val index:Int) : ProductosAction
    data class DecrementarCantidad(val index:Int) : ProductosAction
    data class ConfirmarProductos(val nuevosProductos: List<Producto>) : ProductosAction
}
