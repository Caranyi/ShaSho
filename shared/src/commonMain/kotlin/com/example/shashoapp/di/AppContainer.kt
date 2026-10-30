package com.example.shashoapp.di
import com.example.shashoapp.features.productos.data.repository.ProductoRepositoryImpl
import com.example.shashoapp.features.productos.domain.repository.ProductoRepository
import com.example.shashoapp.features.productos.domain.usecase.GetProductosDisponiblesUseCase

interface AppContainer {
    val getProductosDisponiblesUseCase: GetProductosDisponiblesUseCase
}

class DefaultAppContainer : AppContainer {
    private val productoRepository: ProductoRepository by lazy {
        ProductoRepositoryImpl()
    }

    override val getProductosDisponiblesUseCase: GetProductosDisponiblesUseCase by
    lazy {
        GetProductosDisponiblesUseCase(productoRepository)
    }
}