package com.example.shashoapp.features.productos.domain.repository

import com.example.shashoapp.features.productos.domain.model.Producto

interface ProductoRepository {
    suspend fun getProductosDisponibles(): List<Producto>
}