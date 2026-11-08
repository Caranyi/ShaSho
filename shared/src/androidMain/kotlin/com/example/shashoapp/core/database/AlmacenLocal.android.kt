package com.example.shashoapp.core.database

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File


private lateinit var contexto: Context

fun iniciarAlmacenLocal(ctx: Context) {
    contexto = ctx.applicationContext
}

//se guarda en la carpeta privada de la app (filesDir)
actual suspend fun guardarArchivo(nombre: String, contenido: String) {
    withContext(Dispatchers.IO) {
        File(contexto.filesDir, nombre).writeText(contenido)
    }
}

actual suspend fun leerArchivo(nombre: String): String? {
    return withContext(Dispatchers.IO) {
        val archivo = File(contexto.filesDir, nombre)
        if (archivo.exists()) archivo.readText() else null
    }
}
