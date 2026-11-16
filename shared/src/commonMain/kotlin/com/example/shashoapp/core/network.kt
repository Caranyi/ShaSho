package com.example.shashoapp.core


import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout


object ServidorConfig {
    var BASE_URL: String = "http://192.168.1.16:8080"
}


val httpClient: HttpClient = HttpClient {
    //TIEMPO DE ESPERA
    install(HttpTimeout) {
        connectTimeoutMillis = 5_000
        requestTimeoutMillis = 10_000
    }
}
