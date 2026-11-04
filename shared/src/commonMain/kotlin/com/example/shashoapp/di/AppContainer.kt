package com.example.shashoapp.di
import com.example.shashoapp.Route
import com.example.shashoapp.features.auth.data.repository.AuthRepositoryImpl
import com.example.shashoapp.features.auth.domain.repository.AuthRepository
import com.example.shashoapp.features.auth.domain.usecase.LoginUseCase
import com.example.shashoapp.features.auth.domain.usecase.RegisterUseCase
import com.example.shashoapp.features.listaListas.data.repository.ListaRepositoryImpl
import com.example.shashoapp.features.listaListas.domain.repository.ListaRepository
import com.example.shashoapp.features.listaListas.domain.usecase.GetListasUseCase
import com.example.shashoapp.features.listaProductos.data.repository.ProductoRepositoryImpl
import com.example.shashoapp.features.listaProductos.domain.repository.ProductoRepository
import com.example.shashoapp.features.listaProductos.domain.usecase.GetProductosDisponiblesUseCase

interface AppContainer {
    val getProductosDisponiblesUseCase: GetProductosDisponiblesUseCase
    val getListasUseCase: GetListasUseCase

    val loginUseCase : LoginUseCase

    val registerUseCase : RegisterUseCase
}

class DefaultAppContainer : AppContainer {
    private val productoRepository: ProductoRepository by lazy {
        ProductoRepositoryImpl()
    }

    override val getProductosDisponiblesUseCase: GetProductosDisponiblesUseCase by lazy {
        GetProductosDisponiblesUseCase(productoRepository)
    }

    private val listaRepository: ListaRepository by lazy{
        ListaRepositoryImpl()
    }

    override val getListasUseCase: GetListasUseCase by lazy{
        GetListasUseCase(listaRepository)
    }

    private val authRepository: AuthRepository by lazy{
        AuthRepositoryImpl()
    }

    override val loginUseCase: LoginUseCase by lazy{
        LoginUseCase(authRepository)
    }

    override val registerUseCase: RegisterUseCase by lazy{
        RegisterUseCase(authRepository)
    }
}