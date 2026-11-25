package com.example.shashoapp.features.listaProductos.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenuPopup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.TextosApp
import com.example.shashoapp.core.aImageBitmap
import com.example.shashoapp.core.rememberSelectorImagenPerfil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearProducto (
    onDismissRequest: () -> Unit,
    onGuardar: (String, String, Int, Double, String, ByteArray?) -> Unit
){
    val idioma by AppPreferences.idioma.collectAsStateWithLifecycle()
    val textos = TextosApp.get(idioma)

    var nombre by rememberSaveable { mutableStateOf("") }
    var tipo by rememberSaveable { mutableStateOf("") }
    var cantidad by rememberSaveable { mutableStateOf("1") }
    var precio by rememberSaveable { mutableStateOf("") }
    var tienda by rememberSaveable { mutableStateOf("") }

    var menuAbierto by rememberSaveable { mutableStateOf(false) }

    val tipos = listOf(
        "alimentación" to textos.alimentacion,
        "otros" to textos.otros
    )

    var imagenBytes by remember { mutableStateOf<ByteArray?>(null) }
    val abrirGaleria = rememberSelectorImagenPerfil { bytes -> imagenBytes = bytes }
    val bitmapPreview = remember(imagenBytes) { imagenBytes?.aImageBitmap() }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
    ){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Text(
                text = textos.anadirProducto,
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = {nombre = it},
                label = {Text(textos.nombreProducto)},
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Box {
                OutlinedButton(
                    onClick = { menuAbierto = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val etiquetaActual = tipos.firstOrNull {it.first == tipo}?.second ?: textos.seleccionarTipo
                    Text(text = etiquetaActual)
                }

                DropdownMenu(
                    expanded = menuAbierto,
                    onDismissRequest = { menuAbierto = false }
                ) {
                    tipos.forEach { (valorServidor, etiquetaVisible) ->
                        DropdownMenuItem(
                            text = { Text(etiquetaVisible) },
                            onClick = {
                                tipo = valorServidor
                                menuAbierto = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = cantidad,
                onValueChange = {
                    valor ->
                        if (valor.all {it.isDigit()}) {
                            cantidad = valor
                        }
                },

                label = { Text(textos.cantidad)},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine =  true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = precio,
                onValueChange = {precio = it},
                label = { Text(textos.precio)},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine =  true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = tienda,
                onValueChange = {tienda = it},
                label = {Text(textos.tienda)},
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (bitmapPreview != null) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onDismissRequest
                ){
                    Text(textos.cancelar)
                }

                Button(
                    onClick = {
                        val cantidadInt = cantidad.toIntOrNull()
                        val precioDouble = precio.replace(',','.').toDoubleOrNull()

                        if(cantidadInt != null && precioDouble != null){
                            onGuardar(nombre.trim(), tipo, cantidadInt, precioDouble, tienda.trim(), imagenBytes)
                        }
                    },

                    //si ninguno de los campos es inválido...
                    enabled = nombre.isNotBlank() &&
                        tipo.isNotBlank() && (cantidad.toIntOrNull() ?: 0) > 0
                            && (precio.replace(',','.').toDoubleOrNull() ?: -1.0) >= 0.0

                ){
                    Text(textos.anadir)
                }
            }
        }

    }
}