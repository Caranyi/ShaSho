package com.example.shashoapp.features.listaProductos.domain.repository

import com.example.shashoapp.Route
import com.example.shashoapp.features.auth.domain.model.Usuario
import com.example.shashoapp.features.listaProductos.domain.model.Producto

interface ProductoRepository {
    suspend fun getProductosDisponibles(): List<Producto>
    //productos de la lista
    suspend fun getProductosDeLista(idLista: Int): List<Producto>

    suspend fun crearProducto(idLista: Int, nombre: String, tipo: String,
                              cantidad: Int, precio: Double, tienda: String)

    suspend fun actualizaMarcado(idLista: Int, idProducto: Int, marcado: Boolean, idUsuarioMark: Int?)

    suspend fun actualizarCantidad(idLista: Int, idProducto: Int, nuevaCantidad: Int)

    suspend fun eliminarProducto(idLista: Int, idProducto: Int)
}
