package com.example.shashoapp.core.database

//GUARDA UN TEXTO EN UN ARCHIVO DEL DISPOSITIVO (cada plataforma lo hace a su manera)
expect suspend fun guardarArchivo(nombre: String, contenido: String)

//LEE UN ARCHIVO DEL DISPOSITIVO (null si no existe)
expect suspend fun leerArchivo(nombre: String): String?
