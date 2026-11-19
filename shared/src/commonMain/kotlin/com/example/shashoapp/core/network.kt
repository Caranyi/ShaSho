package com.example.shashoapp.core


import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout


expect object ServidorConfig {
    val BASE_URL: String
}


