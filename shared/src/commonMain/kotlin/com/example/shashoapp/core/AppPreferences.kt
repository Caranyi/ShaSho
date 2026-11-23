package com.example.shashoapp.core

import com.example.shashoapp.core.database.guardarArchivo
import com.example.shashoapp.core.database.leerArchivo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.io.encoding.Base64

object AppPreferences {
    private val _idioma = MutableStateFlow("es")
    val idioma: StateFlow<String> = _idioma.asStateFlow()

    private val _notificacionesActivas = MutableStateFlow(true)
    val notificacionesActivas: StateFlow<Boolean> = _notificacionesActivas.asStateFlow()

    private val _idUsuario = MutableStateFlow<Int?>(null)
    val idUsuario: StateFlow<Int?> = _idUsuario.asStateFlow()

    private val _modoOscuro = MutableStateFlow(false)
    val modoOscuro: StateFlow<Boolean> = _modoOscuro.asStateFlow()

    private val _nombreUsuario = MutableStateFlow("")
    val nombreUsuario: StateFlow<String> = _nombreUsuario.asStateFlow()

    private val _fotoPerfilBytes = MutableStateFlow<ByteArray?>(null)
    val fotoPerfilBytes: StateFlow<ByteArray?> = _fotoPerfilBytes.asStateFlow()

    suspend fun cargarPreferencias() {
        leerArchivo("pref_idioma.txt")?.let { guardado ->
            if (guardado == "es" || guardado == "en") {
                _idioma.value = guardado
            }
        }
        leerArchivo("pref_notificaciones.txt")?.let { guardado ->
            _notificacionesActivas.value = guardado.toBooleanStrictOrNull() ?: true
        }
        leerArchivo("pref_modo_oscuro.txt")?.let { guardado ->
            _modoOscuro.value = guardado.toBooleanStrictOrNull() ?: false
        }
        leerArchivo("pref_id_usuario.txt")?.let { guardado ->
            _idUsuario.value = guardado.toIntOrNull()
        }
        cargarDatosUsuario(_idUsuario.value)
    }

    suspend fun cargarDatosUsuario(id: Int?) {
        val sufijo = if (id != null && id > 0) "_$id" else "_offline"

        val nombreGuardado = leerArchivo("pref_nombre_usuario$sufijo.txt").orEmpty()
        _nombreUsuario.value = nombreGuardado
        if (nombreGuardado.isNotBlank()) {
            SesionUsuario.setNombreUsuario(nombreGuardado)
        }

        val b64 = leerArchivo("pref_foto_perfil$sufijo.b64").orEmpty()
        _fotoPerfilBytes.value = if (b64.isNotBlank()) {
            runCatching { Base64.decode(b64) }.getOrNull()
        } else {
            null
        }
    }

    suspend fun setIdioma(nuevoIdioma: String) {
        _idioma.value = nuevoIdioma
        guardarArchivo("pref_idioma.txt", nuevoIdioma)
    }

    suspend fun setNotificaciones(activas: Boolean) {
        _notificacionesActivas.value = activas
        guardarArchivo("pref_notificaciones.txt", activas.toString())
    }

    suspend fun setIdUsuario(id: Int?) {
        _idUsuario.value = id
        if (id != null) {
            guardarArchivo("pref_id_usuario.txt", id.toString())
            cargarDatosUsuario(id)
        } else {
            guardarArchivo("pref_id_usuario.txt", "")
            _nombreUsuario.value = ""
            _fotoPerfilBytes.value = null
        }
    }

    suspend fun setModoOscuro(activado: Boolean) {
        _modoOscuro.value = activado
        guardarArchivo("pref_modo_oscuro.txt", activado.toString())
    }

    suspend fun setNombreUsuario(nombre: String) {
        _nombreUsuario.value = nombre
        SesionUsuario.setNombreUsuario(nombre)
        val id = _idUsuario.value ?: SesionUsuario.getIdUsuario()
        val sufijo = if (id != null && id > 0) "_$id" else "_offline"
        guardarArchivo("pref_nombre_usuario$sufijo.txt", nombre)
    }
    suspend fun setFotoPerfil(bytes: ByteArray?) {
        _fotoPerfilBytes.value = bytes
        val id = _idUsuario.value ?: SesionUsuario.getIdUsuario()
        val sufijo = if (id != null && id > 0) "_$id" else "_offline"
        val encoded = if (bytes != null) Base64.encode(bytes) else ""
        guardarArchivo("pref_foto_perfil$sufijo.b64", encoded)
    }
}