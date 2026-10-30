package com.example.shashoapp

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    data object Home : Route

    @Serializable
    data class Detail(val id: String) : Route
}