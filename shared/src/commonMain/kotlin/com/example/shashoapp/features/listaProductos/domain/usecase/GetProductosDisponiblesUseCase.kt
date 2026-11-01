package com.example.shashoapp.features.listaProductos.domain.usecase import com.example.shashoapp.features.listaProductos.domain.model.Producto
import com.example.shashoapp.features.listaProductos.domain.repository.ProductoRepository

class GetProductosDisponiblesUseCase(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke(): List<Producto>{
        return repository.getProductosDisponibles();
    }
}