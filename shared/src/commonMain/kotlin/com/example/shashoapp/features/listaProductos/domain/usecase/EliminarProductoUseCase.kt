package com.example.shashoapp.features.listaProductos.domain.usecase

import com.example.shashoapp.features.listaProductos.domain.repository.ProductoRepository

class EliminarProductoUseCase(
    private val repositry: ProductoRepository
) {
    suspend operator fun invoke(idLista: Int, idProducto: Int) {
        repositry.eliminarProducto(idLista, idProducto)
    }
}

