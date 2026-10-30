package com.example.shashoapp.features.productos.presentation

import com.example.shashoapp.features.productos.domain.model.Producto
import com.example.shashoapp.features.productos.domain.usecase.GetProductosDisponiblesUseCase

data class ProductosState(
    val productosLista: List<Producto> = emptyList(),
    val productosDisponibles: List<Producto> = emptyList(),
    val showCreateMenu: Boolean = false
)