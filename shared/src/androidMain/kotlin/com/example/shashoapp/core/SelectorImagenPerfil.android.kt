package com.example.shashoapp.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import java.io.ByteArrayOutputStream
import kotlin.math.max
import kotlin.math.roundToInt
import androidx.core.graphics.scale

@Composable
actual fun rememberSelectorImagenPerfil(
    onImagenSeleccionada: (ByteArray) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val bytes = context.contentResolver.openInputStream(uri)?.use { input ->
                input.readBytes()
            }
            if (bytes != null) {
                onImagenSeleccionada(reducirImagenAndroid(bytes))
            }
        }
    }
    return { launcher.launch("image/*") }
}

private fun reducirImagenAndroid(bytes: ByteArray, maxLado: Int = 360): ByteArray {
    val original = BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return bytes
    val ladoMayor = max(original.width, original.height)
    val bitmapFinal = if (ladoMayor > maxLado) {
        val escala = maxLado.toFloat() / ladoMayor.toFloat()
        val ancho = (original.width * escala).roundToInt().coerceAtLeast(1)
        val alto = (original.height * escala).roundToInt().coerceAtLeast(1)
        original.scale(ancho, alto)
    } else {
        original
    }
    val out = ByteArrayOutputStream()
    bitmapFinal.compress(Bitmap.CompressFormat.JPEG, 85, out)
    return out.toByteArray()
}

actual fun ByteArray.aImageBitmap(): ImageBitmap? = runCatching {
    BitmapFactory.decodeByteArray(this, 0, size)?.asImageBitmap()
}.getOrNull()