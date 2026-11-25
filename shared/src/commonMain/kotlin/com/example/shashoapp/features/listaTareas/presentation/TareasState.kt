package com.example.shashoapp.features.listaTareas.presentation

import com.example.shashoapp.features.listaTareas.domain.model.Tarea

data class TareasState(
    val tareas: List<Tarea> = emptyList(),
    val cargando: Boolean = false,
    val mostrarCrear: Boolean = false,
    val error: String? = null,

    val mostrarCompartir: Boolean = false,
    val correoCompartir: String = "",
    val mensajeCompartir: String? = null
)