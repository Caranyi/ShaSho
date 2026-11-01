package com.example.shashoapp.features.listaProductos.presentation

import com.example.shashoapp.features.listaProductos.domain.model.Producto

data class ProductosState(
    val productosLista: List<Producto> = emptyList(),
    val productosDisponibles: List<Producto> = emptyList(),
    val showCreateMenu: Boolean = false
)