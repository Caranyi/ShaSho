package com.example.shashoapp.features.listaListas.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ktor.websocket.Frame
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun CrearLista(
    onDismiss: () -> Unit,
    onGuardar: (String) -> Unit
) {
    var nombre by rememberSaveable {
        mutableStateOf("")
    }

    AlertDialog(
        onDismiss,
        title = {
            Text("Crear lista")
        },
        text = {
            Column{
                Text("Datos")

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value =  nombre,
                    onValueChange = {nombre = it},
                    label = { Text("Nombre de la lista")}
                )
            }
        },

        confirmButton = {
            TextButton(
                onClick = {onGuardar(nombre)},
                enabled = nombre.isNotBlank()
            ){
                Text("Guardar")
            }
        },

        dismissButton = {
            TextButton(
                onClick = onDismiss
            ){
                Text("Cancelar")
            }
        }
    )
}