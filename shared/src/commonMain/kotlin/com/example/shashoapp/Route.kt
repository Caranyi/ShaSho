package com.example.shashoapp

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    data object Home : Route

    @Serializable
    data object Settings : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Register : Route

    @Serializable
    data class Lista(val id: Int) : Route

    @Serializable
    data class Reparto(val listaId: Int) : Route
}