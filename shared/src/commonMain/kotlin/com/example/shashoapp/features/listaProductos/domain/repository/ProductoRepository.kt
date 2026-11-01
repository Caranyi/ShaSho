package com.example.shashoapp.features.listaProductos.domain.repository

import com.example.shashoapp.features.listaProductos.domain.model.Producto

interface ProductoRepository {
    suspend fun getProductosDisponibles(): List<Producto>
}