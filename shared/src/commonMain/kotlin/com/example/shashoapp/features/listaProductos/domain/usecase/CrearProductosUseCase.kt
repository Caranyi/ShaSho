package com.example.shashoapp.features.listaProductos.domain.usecase

import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.TextosApp
import com.example.shashoapp.features.listaListas.domain.repository.ListaRepository
import com.example.shashoapp.features.listaProductos.domain.repository.ProductoRepository

class CrearProductosUseCase(
    private val repository: ProductoRepository
){
    suspend operator fun invoke(
        idLista: Int,
        nombre: String,
        tipo: String,
        cantidad: Int,
        precio: Double,
        tienda: String,
        imagenBytes: ByteArray? = null
    ) {
        val textos = TextosApp.get(AppPreferences.idioma.value)
        require(nombre.isNotBlank()) { textos.errorNombreProductoVacio }
        require(cantidad > 0) { textos.errorCantidadMayorCero }
        require(precio >= 0) { textos.errorPrecioNegativo }

        repository.crearProducto(
            idLista,
            nombre.trim(),
            tipo,
            cantidad,
            precio,
            tienda,
            imagenBytes
        )
    }
}