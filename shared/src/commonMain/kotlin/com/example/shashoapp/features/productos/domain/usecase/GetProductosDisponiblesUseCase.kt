package com.example.shashoapp.features.productos.domain.usecase import com.example.shashoapp.features.productos.domain.model.Producto
import com.example.shashoapp.features.productos.domain.repository.ProductoRepository

class GetProductosDisponiblesUseCase(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke(): List<Producto>{
        return repository.getProductosDisponibles();
    }
}