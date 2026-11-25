package com.example.shashoapp.features.listaListas.domain.repository

import com.example.shashoapp.features.listaListas.domain.model.Lista

interface ListaRepository {
    suspend fun getListas(idUsuario: Int?): List<Lista>
    suspend fun compartirLista(idLista: Int, correo: String): Result<Unit>
    suspend fun crearLista(nombre: String, idCreador: Int?, tipo: String, imagenBytes: ByteArray? = null)
    suspend fun eliminarLista(idLista: Int)
    suspend fun sincronizarPendientes(idUsuario: Int?)
}