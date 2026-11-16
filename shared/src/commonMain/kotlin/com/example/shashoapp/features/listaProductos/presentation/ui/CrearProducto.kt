package com.example.shashoapp.features.listaProductos.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearProducto (
    onDismissRequest: () -> Unit,
    onGuardar: (String, String, Int, Double, String ) -> Unit
){
    var nombre by rememberSaveable { mutableStateOf("") }
    var tipo by rememberSaveable { mutableStateOf("") }
    var cantidad by rememberSaveable { mutableStateOf("1") }
    var precio by rememberSaveable { mutableStateOf("") }
    var tienda by rememberSaveable { mutableStateOf("") }

    var menuAbierto by rememberSaveable { mutableStateOf(false) }

    val tipos = listOf(
        "alimentación",
        "otros"
    )

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
                text = "Añadir producto",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = {nombre = it},
                label = {Text("Nombre del produtcto")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Box {
                OutlinedButton(
                    onClick = { menuAbierto = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (tipo.isEmpty()) {
                            "Seleccionar tipo"
                        } else {
                            tipo
                        }
                    )
                }

                DropdownMenu(
                    expanded = menuAbierto,
                    onDismissRequest = { menuAbierto = false }
                ) {
                    tipos.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                tipo = opcion
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

                    label = { Text("Cantidad")},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine =  true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = precio,
                    onValueChange = {precio = it},
                    label = { Text("Precio")},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine =  true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = tienda,
                    onValueChange = {tienda = it},
                    label = {Text("Tienda")},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismissRequest
                    ){
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            val cantidadInt = cantidad.toIntOrNull()
                            val precioDouble = precio.replace(',','.').toDoubleOrNull()

                            if(cantidadInt != null && precioDouble != null){
                                onGuardar(
                                    nombre.trim(),
                                    tipo,
                                    cantidadInt,
                                    precioDouble,
                                    tienda.trim()
                                )
                            }
                        },

                        //si ninguno de los campos es inválido...
                        enabled = nombre.isNotBlank() &&
                            tipo.isNotBlank() && (cantidad.toIntOrNull() ?: 0) > 0
                                && (precio.replace(',','.').toDoubleOrNull() ?: -1.0) >= 0.0

                    ){
                        Text("Añadir")
                    }
                }
        }

    }
}