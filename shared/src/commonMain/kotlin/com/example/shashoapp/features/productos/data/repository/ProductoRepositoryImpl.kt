package com.example.shashoapp.features.productos.data.repository import com.example.shashoapp.features.productos.domain.model.Producto
import com.example.shashoapp.features.productos.domain.repository.ProductoRepository

class ProductoRepositoryImpl : ProductoRepository {
    override suspend fun getProductosDisponibles(): List<Producto>{
        return listOf(
            Producto(1, "Leche entera", 1.10),
            Producto(2, "Pan de molde", 1.45),
            Producto(3, "Docena de huevos", 2.30)
            )
    }
}