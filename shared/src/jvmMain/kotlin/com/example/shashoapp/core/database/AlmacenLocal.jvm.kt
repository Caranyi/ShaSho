package com.example.shashoapp.core.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

//Version de escritorio
private val carpeta = File(System.getProperty("user.home"), ".shasho")

actual suspend fun guardarArchivo(nombre: String, contenido: String) {
    withContext(Dispatchers.IO) {
        carpeta.mkdirs()
        File(carpeta, nombre).writeText(contenido)
    }
}

actual suspend fun leerArchivo(nombre: String): String? {
    return withContext(Dispatchers.IO) {
        val archivo = File(carpeta, nombre)
        if (archivo.exists()) archivo.readText() else null
    }
}
