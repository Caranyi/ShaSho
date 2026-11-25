package com.example.shashoapp.features.listaTareas.presentation

sealed interface TareasAction {
    data object AbrirCrear : TareasAction

    data object CerrarCrear : TareasAction

    data class GuardarTarea(val nombre: String) : TareasAction

    data class CambiarCompletada(val idTarea: Int) : TareasAction

    data class EliminarTarea(val idTarea: Int) : TareasAction

    data object AbrirCompartir : TareasAction
    data object CerrarCompartir : TareasAction
    data class OnCorreoChange(val correo: String) : TareasAction
    data object ConfirmarCompartir : TareasAction
}