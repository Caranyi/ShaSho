package com.example.shashoapp.features.listaListas.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.TextosApp
import com.example.shashoapp.core.aImageBitmap
import com.example.shashoapp.core.rememberSelectorImagenPerfil

@Composable
fun CrearLista(
    onDismiss: () -> Unit,
    onGuardar: (String, String, ByteArray?) -> Unit
) {
    val idioma by AppPreferences.idioma.collectAsStateWithLifecycle()
    val textos = TextosApp.get(idioma)

    var nombre by rememberSaveable { mutableStateOf("") }
    var tipoSeleccionado by rememberSaveable { mutableStateOf("") }
    var imagenBytes by remember { mutableStateOf<ByteArray?>(null) }

    val abrirGaleria = rememberSelectorImagenPerfil { bytes ->
        imagenBytes = bytes
    }
    val bitmapPreview = remember(imagenBytes) {
        imagenBytes?.aImageBitmap()
    }

    val tipos = listOf(
        "productos" to textos.tipo_lista_productos,
        "tareas" to textos.tipo_lista_tareas
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(textos.crearLista) },
        text = {
            Column {
                Text(textos.datos)

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text(textos.nombreLista) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Text(textos.tipo_de_lista)

                tipos.forEach { (valor, etiqueta) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { tipoSeleccionado = valor },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = tipoSeleccionado == valor,
                            onClick = { tipoSeleccionado = valor }
                        )
                        Text(etiqueta)
                    }
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (bitmapPreview != null) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Image(
                                bitmap = bitmapPreview,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = abrirGaleria,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (imagenBytes == null) textos.anadirImagenOpcional else textos.cambiarImagen
                        )
                    }

                    if (imagenBytes != null) {
                        TextButton(onClick = { imagenBytes = null }) {
                            Text(textos.quitarImagen)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onGuardar(nombre, tipoSeleccionado, imagenBytes) },
                enabled = nombre.isNotBlank() && tipoSeleccionado.isNotBlank()
            ) {
                Text(textos.guardar)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(textos.cancelar)
            }
        }
    )
}