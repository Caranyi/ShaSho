package com.example.shashoapp.features.Reparto.data.repository

import com.example.shashoapp.core.ApiServidor
import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.EstadoConexion
import com.example.shashoapp.core.SesionUsuario
import com.example.shashoapp.core.UsuarioInfoDto
import com.example.shashoapp.core.database.guardarArchivo
import com.example.shashoapp.core.database.leerArchivo
import com.example.shashoapp.features.Reparto.domain.model.Participante
import com.example.shashoapp.features.Reparto.domain.repository.RepartoRepository
import kotlin.coroutines.cancellation.CancellationException
import kotlin.io.encoding.Base64

class RepartoRepositorioImpl : RepartoRepository {
    override suspend fun getParticipantes(idsUsuarios: IntArray): List<Participante> {
        val infoRemota: Map<Int, UsuarioInfoDto> = if (EstadoConexion.getConexion()) {
            try {
                ApiServidor.getInfoUsuarios(idsUsuarios) // o ApiServidor.getInfoUsuarios(idsUsuarios)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                emptyMap()
            }
        } else {
            emptyMap()
        }

        val idActual = SesionUsuario.getIdUsuario() ?: -1
        val nombreLocalActual = AppPreferences.nombreUsuario.value
            .ifBlank { SesionUsuario.getNombreUsuario() }
            .ifBlank { leerArchivo("pref_nombre_usuario_offline.txt").orEmpty() }

        return idsUsuarios.map { id ->
            val remoto = infoRemota[id]
            val sufijo = if (id > 0) "_$id" else "_offline"
            val nombreEnArchivo = leerArchivo("pref_nombre_usuario$sufijo.txt").orEmpty().trim()

            val nombre = when {
                remoto != null && remoto.nombre.isNotBlank() -> {
                    guardarArchivo("pref_nombre_usuario$sufijo.txt", remoto.nombre)
                    remoto.nombre
                }
                nombreEnArchivo.isNotBlank() -> nombreEnArchivo
                (id == idActual || id <= 0) && nombreLocalActual.isNotBlank() -> nombreLocalActual
                else -> "U $id"
            }

            val fotoBytes: ByteArray? = when {
                remoto != null && remoto.fotoPerfil.isNotBlank() -> {
                    guardarArchivo("pref_foto_perfil$sufijo.b64", remoto.fotoPerfil)
                    runCatching { Base64.decode(remoto.fotoPerfil) }.getOrNull()
                }
                (id == idActual || id <= 0) && AppPreferences.fotoPerfilBytes.value != null -> {
                    AppPreferences.fotoPerfilBytes.value
                }
                else -> {
                    val b64 = leerArchivo("pref_foto_perfil$sufijo.b64").orEmpty()
                    if (b64.isNotBlank()) {
                        runCatching { Base64.decode(b64) }.getOrNull()
                    } else {
                        null
                    }
                }
            }

            Participante(
                usuarioId = id,
                nombre = nombre,
                pagado = 0.0,
                fotoPerfilBytes = fotoBytes
            )
        }
    }
}