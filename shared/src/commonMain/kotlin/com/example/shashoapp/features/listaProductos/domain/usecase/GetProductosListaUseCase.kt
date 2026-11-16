package com.example.shashoapp.features.listaProductos.domain.usecase

import com.example.shashoapp.features.listaProductos.domain.model.Producto
import com.example.shashoapp.features.listaProductos.domain.repository.ProductoRepository

//productos que hay dentro de una lista
class GetProductosListaUseCase(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke(idLista: Int): List<Producto> {
        return repository.getProductosDeLista(idLista)
    }
}
