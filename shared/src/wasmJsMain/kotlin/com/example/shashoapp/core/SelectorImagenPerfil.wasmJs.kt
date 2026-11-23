package com.example.shashoapp.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

@OptIn(ExperimentalWasmJsInterop::class)
@Composable
actual fun rememberSelectorImagenPerfil(
    onImagenSeleccionada: (ByteArray) -> Unit
): () -> Unit {
    return {}
}

actual fun ByteArray.aImageBitmap(): ImageBitmap? = runCatching {
    Image.makeFromEncoded(this).toComposeImageBitmap()
}.getOrNull()