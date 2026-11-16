package com.example.shashoapp.features.listaProductos.domain.usecase

import com.example.shashoapp.features.listaProductos.domain.repository.ProductoRepository

class ActualizarMarcadosUseCase(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke(
        idLista: Int,
        idProducto: Int,
        marcado: Boolean,
        idUsuarioMark: Int?
    ){
        repository.actualizaMarcado(
            idLista, idProducto, marcado, idUsuarioMark
        )
    }

}