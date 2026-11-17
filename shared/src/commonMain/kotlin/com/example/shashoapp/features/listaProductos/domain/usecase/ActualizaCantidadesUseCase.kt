package com.example.shashoapp.features.listaProductos.domain.usecase

import com.example.shashoapp.features.listaProductos.domain.repository.ProductoRepository

class ActualizaCantidadesUseCase(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke(
        idLista: Int,
        idProducto: Int,
        nuevaCantidad: Int
    ) {
        repository.actualizarCantidad(idLista, idProducto, nuevaCantidad)
    }
}