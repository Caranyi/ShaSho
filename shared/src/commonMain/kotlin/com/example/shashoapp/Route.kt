package com.example.shashoapp

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Bienvenida : Route
    @Serializable
    data object Home : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Register : Route

    @Serializable
    data class ListaProductos(val id: Int, val nombre: String) : Route

    @Serializable
    data class ListaTareas(val id: Int, val nombre: String) : Route

    @Serializable
    data object Ajustes : Route

    @Serializable
    data class Reparto(val listaId: Int) : Route
}