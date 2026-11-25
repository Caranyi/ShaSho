package com.example.shashoapp.features.listaProductos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.SesionUsuario
import com.example.shashoapp.core.TextosApp
import com.example.shashoapp.features.listaProductos.domain.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.shashoapp.features.listaProductos.domain.usecase.GetProductosListaUseCase
import com.example.shashoapp.features.listaListas.domain.usecase.CompartirListaUseCase
import com.example.shashoapp.features.listaProductos.domain.usecase.ActualizaCantidadesUseCase
import com.example.shashoapp.features.listaProductos.domain.usecase.ActualizarMarcadosUseCase
import com.example.shashoapp.features.listaProductos.domain.usecase.CrearProductosUseCase
import com.example.shashoapp.features.listaProductos.domain.usecase.EliminarProductoUseCase
import kotlin.coroutines.cancellation.CancellationException


class ProductosViewModel(
    private val getProductosListaUseCase: GetProductosListaUseCase,
    private val compartirListaUseCase: CompartirListaUseCase,
    private val idLista: Int,
    private val crearProductosUseCase: CrearProductosUseCase,
    private val actualizarMarcadosUseCase: ActualizarMarcadosUseCase,
    private val eliminarProductoUseCase: EliminarProductoUseCase,
    private val actualizaCantidadesUseCase: ActualizaCantidadesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductosState())
    val state: StateFlow<ProductosState> = _state.asStateFlow()

    init {
        cargarProductosLista()
    }

    fun onAction(action:ProductosAction){
        when(action){
            ProductosAction.AbrirMenuCrear -> _state.update { it.copy(showCreateMenu = true) }
            ProductosAction.CerrarMenuCrear -> _state.update { it.copy(showCreateMenu = false) }

            is ProductosAction.ToggleChecked -> toggleChecked(action.index)
            is ProductosAction.IncrementarCantidad -> incrementarCantidad(action.index)
            is ProductosAction.DecrementarCantidad -> decrementarCantidad(action.index)
            is ProductosAction.ConfirmarProductos -> confirmarProductos(action.nuevosProductos)

            //COMPARTIR LISTA
            ProductosAction.AbrirCompartir -> _state.update { it.copy(mostrarCompartir = true) }
            ProductosAction.CerrarCompartir -> _state.update {
                it.copy(mostrarCompartir = false, correoCompartir = "", mensajeCompartir = null)
            }
            is ProductosAction.OnCorreoChange -> _state.update {
                it.copy(correoCompartir = action.correo, mensajeCompartir = null)
            }
            ProductosAction.ConfirmarCompartir -> confirmarCompartir()

            is ProductosAction.GuardarProducto -> {guardarProducto(action)}
            is ProductosAction.EliminarProducto -> eliminarProducto(action.idProducto)

        }
    }

    private fun guardarProducto(
        producto: ProductosAction.GuardarProducto
    ){
       viewModelScope.launch {
           try {
               crearProductosUseCase(
                   idLista = idLista,
                   nombre = producto.nombre,
                   tipo = producto.tipo,
                   cantidad = producto.cantidad,
                   precio = producto.precio,
                   tienda = producto.tienda,
                   imagenBytes = producto.imagenBytes
               )

               val productos = getProductosListaUseCase(idLista)

               _state.update { it.copy(productosLista = productos, showCreateMenu = false) }
           } catch (e: CancellationException){
               throw  e
           } catch (e: Exception){
               println ("Error")
           }
       }
    }


    //cargar productos
    private fun cargarProductosLista() {
        viewModelScope.launch {
            val productos = getProductosListaUseCase(idLista)
            _state.update { it.copy(productosLista = productos) }
        }
    }

    private fun toggleChecked(i: Int) {
        val producto = _state.value.productosLista.getOrNull(i) ?: return
        val marcado = !producto.isChecked
        val usuarioActual = SesionUsuario.getIdUsuario()

        _state.update { actual ->
            val lista = actual.productosLista.toMutableList()
            if (i in lista.indices) {
                lista[i] = lista[i].copy(
                    isChecked = marcado,
                    idUsuarioMark = if(marcado) usuarioActual else null
                )
            }
            actual.copy(productosLista = lista)
        }


        viewModelScope.launch {
            try {
                actualizarMarcadosUseCase(
                    idLista, producto.id, marcado, if(marcado) usuarioActual else null
                )
            } catch (e: CancellationException){
                throw e
            } catch(e: Exception){}
        }
    }

    private fun incrementarCantidad(i: Int) {
        val producto = _state.value.productosLista.getOrNull(i) ?: return
        val nuevaCantidad = producto.cantidad + 1

        _state.update { actual ->
           actual.copy(
               productosLista = actual.productosLista.map {
                   if(it.id == producto.id){
                       it.copy(cantidad = nuevaCantidad)
                   }
                   else {
                       it
                   }
               }
           )
        }

        viewModelScope.launch {
            try {
                actualizaCantidadesUseCase (idLista, producto.id, nuevaCantidad)
            } catch (e: CancellationException){
                throw e
            } catch (e: Exception){

            }
        }
    }

    private fun confirmarProductos(nuevos: List<Producto>) {
        _state.update { actual ->
            val lista = actual.productosLista.toMutableList()
            nuevos.forEach { nuevoProd ->
                val i = lista.indexOfFirst { it.id == nuevoProd.id }
                if (i != -1) {
                    val existente = lista[i]
                    lista[i] = existente.copy(cantidad = existente.dameCantidad + nuevoProd.dameCantidad)
                } else {
                    lista.add(nuevoProd.copy(isChecked = false))
                }
            }
            actual.copy(productosLista = lista, showCreateMenu = false)
        }
    }

    //compartir lista
    private fun confirmarCompartir() {
        val correo = _state.value.correoCompartir
        if (correo.isBlank()) {
            val textos = TextosApp.get(AppPreferences.idioma.value)
            _state.update { it.copy(mensajeCompartir = textos.errorIntroduceCorreo) }
            return
        }
        val correo_limpio = correo.trim().lowercase()
        viewModelScope.launch {
            compartirListaUseCase(idLista, correo_limpio)
                .onSuccess {
                    _state.update { it.copy(mostrarCompartir = false, correoCompartir = "", mensajeCompartir = null) }
                }
                .onFailure { error ->
                    _state.update { it.copy(mensajeCompartir = error.message) }
                }
        }
    }

    private fun decrementarCantidad(i: Int){
        val producto = _state.value.productosLista.getOrNull(i) ?: return

        if(producto.cantidad > 1){
            val nuevaCantidad = producto.cantidad - 1
            _state.update {
                actual ->
                actual.copy(
                    productosLista = actual.productosLista.map{
                        if(it.id == producto.id){
                            it.copy(cantidad = nuevaCantidad)
                        } else {
                            it
                        }
                    }
                )
            }

            viewModelScope.launch {
                try {
                    actualizaCantidadesUseCase(idLista, producto.id, nuevaCantidad)
                } catch (e: CancellationException){
                    throw e
                } catch (e: Exception){

                }
            }

        } else {
           eliminarProducto(producto.id)
        }
     }


    private fun eliminarProducto(idProducto: Int) {
        viewModelScope.launch {
            eliminarProductoUseCase(idLista, idProducto)

            _state.update {
                actual ->
                actual.copy(
                    productosLista = actual.productosLista.filterNot { it.id == idProducto}
                )
            }
        }
    }
}
