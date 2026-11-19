package com.example.shashoapp.core

import com.example.shashoapp.core.database.guardarArchivo
import com.example.shashoapp.core.database.leerArchivo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AppPreferences {
    private val _idioma = MutableStateFlow("es")
    val idioma: StateFlow<String> = _idioma.asStateFlow()

    private val _notificacionesActivas = MutableStateFlow(true)
    val notificacionesActivas: StateFlow<Boolean> = _notificacionesActivas.asStateFlow()

    suspend fun cargarPreferencias() {
        leerArchivo("pref_idioma.txt")?.let {
                guardado -> if (guardado == "es" || guardado == "en") {
            _idioma.value = guardado }
        }
        leerArchivo("pref_notificaciones.txt")?.let {
                guardado -> _notificacionesActivas.value = guardado.toBooleanStrictOrNull() ?: true
        }
    }

    suspend fun setIdioma(nuevoIdioma: String){
        _idioma.value = nuevoIdioma
        guardarArchivo("pref_idioma.txt", nuevoIdioma)
    }

    suspend fun setNotificaciones(activas: Boolean) {
        _notificacionesActivas.value = activas
        guardarArchivo("pref_notificaciones.txt", activas.toString())
    }

}