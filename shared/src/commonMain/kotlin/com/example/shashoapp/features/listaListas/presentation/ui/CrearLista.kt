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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.TextosApp

@Composable
fun CrearLista(
    onDismiss: () -> Unit,
    onGuardar: (String) -> Unit
) {
    val idioma by AppPreferences.idioma.collectAsStateWithLifecycle()
    val textos = TextosApp.get(idioma)

    var nombre by rememberSaveable {
        mutableStateOf("")
    }

    AlertDialog(
        onDismiss,
        title = {
            Text(textos.crearLista)
        },
        text = {
            Column{
                Text(textos.datos)

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value =  nombre,
                    onValueChange = {nombre = it},
                    label = { Text(textos.nombreLista)}
                )
            }
        },

        confirmButton = {
            TextButton(
                onClick = {onGuardar(nombre)},
                enabled = nombre.isNotBlank()
            ){
                Text(textos.guardar)
            }
        },

        dismissButton = {
            TextButton(
                onClick = onDismiss
            ){
                Text(textos.cancelar)
            }
        }
    )
}