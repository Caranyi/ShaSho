package com.example.shashoapp.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import java.awt.FileDialog
import java.awt.Frame
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FilenameFilter
import javax.imageio.ImageIO
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
actual fun rememberSelectorImagenPerfil(
    onImagenSeleccionada: (ByteArray) -> Unit
): () -> Unit {
    return {
        val dialog = FileDialog(null as Frame?, "Seleccionar foto de perfil", FileDialog.LOAD)
        dialog.filenameFilter = FilenameFilter { _, name ->
            val lower = name.lowercase()
            lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".webp")
        }
        dialog.isVisible = true
        val dir = dialog.directory
        val file = dialog.file
        if (dir != null && file != null) {
            val bytes = runCatching { File(dir, file).readBytes() }.getOrNull()
            if (bytes != null) {
                onImagenSeleccionada(reducirImagenDesktop(bytes))
            }
        }
    }
}

private fun reducirImagenDesktop(bytes: ByteArray, maxLado: Int = 360): ByteArray {
    val original = runCatching { ImageIO.read(ByteArrayInputStream(bytes)) }.getOrNull() ?: return bytes
    val ladoMayor = max(original.width, original.height)
    val imagenFinal = if (ladoMayor > maxLado) {
        val escala = maxLado.toFloat() / ladoMayor.toFloat()
        val ancho = (original.width * escala).roundToInt().coerceAtLeast(1)
        val alto = (original.height * escala).roundToInt().coerceAtLeast(1)
        val destino = BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB)
        val g = destino.createGraphics()
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        g.drawImage(original, 0, 0, ancho, alto, null)
        g.dispose()
        destino
    } else {
        original
    }
    val out = ByteArrayOutputStream()
    ImageIO.write(imagenFinal, "png", out)
    return out.toByteArray()
}

actual fun ByteArray.aImageBitmap(): ImageBitmap? = runCatching {
    Image.makeFromEncoded(this).toComposeImageBitmap()
}.getOrNull()