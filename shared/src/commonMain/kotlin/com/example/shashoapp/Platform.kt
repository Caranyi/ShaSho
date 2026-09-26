package com.example.shashoapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform