package com.example.shashoapp.features.listaTareas.domain.model

data class Tarea(
    val id: Int,
    val nombre: String,
    val completada: Boolean = false
)