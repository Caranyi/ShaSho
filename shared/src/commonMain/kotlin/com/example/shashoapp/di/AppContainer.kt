package com.example.shashoapp.di
import com.example.shashoapp.features.Reparto.data.repository.RepartoRepositorioImpl
import com.example.shashoapp.features.Reparto.domain.repository.RepartoRepository
import com.example.shashoapp.features.Reparto.domain.usecase.GetRepartoUseCase
import com.example.shashoapp.features.auth.data.repository.AuthRepositoryImpl
import com.example.shashoapp.features.auth.domain.repository.AuthRepository
import com.example.shashoapp.features.auth.domain.usecase.LoginUseCase
import com.example.shashoapp.features.auth.domain.usecase.RegisterUseCase
import com.example.shashoapp.features.listaListas.data.repository.ListaRepositoryImpl
import com.example.shashoapp.features.listaListas.domain.repository.ListaRepository
import com.example.shashoapp.features.listaListas.domain.usecase.GetListasUseCase
import com.example.shashoapp.features.listaProductos.data.repository.ProductoRepositoryImpl
import com.example.shashoapp.features.listaProductos.domain.repository.ProductoRepository
import com.example.shashoapp.features.listaProductos.domain.usecase.GetProductosListaUseCase
import com.example.shashoapp.features.listaListas.domain.usecase.CompartirListaUseCase
import com.example.shashoapp.features.listaListas.domain.usecase.CrearListaUseCase
import com.example.shashoapp.features.listaListas.domain.usecase.EliminarListaUseCase
import com.example.shashoapp.features.listaListas.domain.usecase.SincronizarListasUseCase
import com.example.shashoapp.features.listaProductos.domain.usecase.ActualizaCantidadesUseCase
import com.example.shashoapp.features.listaProductos.domain.usecase.ActualizarMarcadosUseCase
import com.example.shashoapp.features.listaProductos.domain.usecase.CrearProductosUseCase
import com.example.shashoapp.features.listaProductos.domain.usecase.EliminarProductoUseCase

interface AppContainer {
    val getProductosListaUseCase: GetProductosListaUseCase
    val getListasUseCase: GetListasUseCase
    val compartirListaUseCase: CompartirListaUseCase
    val loginUseCase : LoginUseCase
    val registerUseCase : RegisterUseCase
    val crearListaUseCase: CrearListaUseCase

    val eliminarListaUseCase: EliminarListaUseCase
    val sincronizarListasUseCase: SincronizarListasUseCase
    val crearProductosUseCase: CrearProductosUseCase
    val actualizarMarcadosUseCase: ActualizarMarcadosUseCase
    val getRepartoUSeCase: GetRepartoUseCase
    val eliminarProductoUseCase: EliminarProductoUseCase
    val actualizaCantidadesUseCase: ActualizaCantidadesUseCase

}

class DefaultAppContainer : AppContainer {
    private val productoRepository: ProductoRepository by lazy {
        ProductoRepositoryImpl()
    }

    override val getProductosListaUseCase: GetProductosListaUseCase by lazy {
        GetProductosListaUseCase(productoRepository)
    }

    private val listaRepository: ListaRepository by lazy{
        ListaRepositoryImpl()
    }

    override val getListasUseCase: GetListasUseCase by lazy{
        GetListasUseCase(listaRepository)
    }

    override val compartirListaUseCase: CompartirListaUseCase by lazy {
        CompartirListaUseCase(listaRepository)
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

    override val crearListaUseCase: CrearListaUseCase by lazy {
        CrearListaUseCase(listaRepository)
    }

    override val eliminarListaUseCase: EliminarListaUseCase by lazy{
        EliminarListaUseCase(listaRepository)
    }

    override val sincronizarListasUseCase: SincronizarListasUseCase by lazy{
        SincronizarListasUseCase(listaRepository)
    }

    override val crearProductosUseCase: CrearProductosUseCase by lazy {
        CrearProductosUseCase(productoRepository)
    }

    override val actualizarMarcadosUseCase by lazy {
        ActualizarMarcadosUseCase(productoRepository)
    }

    private val repartoRepository: RepartoRepository by lazy{
        RepartoRepositorioImpl()
    }

    override val getRepartoUSeCase: GetRepartoUseCase by lazy {
        GetRepartoUseCase(repository = repartoRepository,
            listaRepository = listaRepository,
            getProductosListaUseCase)
    }

    override val eliminarProductoUseCase by lazy {
        EliminarProductoUseCase(productoRepository)
    }

    override val actualizaCantidadesUseCase by lazy {
        ActualizaCantidadesUseCase(productoRepository)
    }
}