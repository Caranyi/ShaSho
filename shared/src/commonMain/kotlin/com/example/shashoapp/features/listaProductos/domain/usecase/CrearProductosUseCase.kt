package com.example.shashoapp.features.listaProductos.domain.usecase

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
        tienda: String
    ){
        require(nombre.isNotBlank()){
            "nombre no puede estar vacío"
        }
        require(cantidad > 0){
            "La cantidad debe ser mayor que 0"
        }
        require(precio >= 0){
            "El precio non puede ser negativo"
        }

        repository.crearProducto(
            idLista,
            nombre.trim(),
            tipo,
            cantidad,
            precio,
            tienda
        )
    }
}