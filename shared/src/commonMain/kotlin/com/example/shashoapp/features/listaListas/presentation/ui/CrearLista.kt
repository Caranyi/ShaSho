package com.example.shashoapp.features.listaListas.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.TextosApp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.RadioButton
import androidx.compose.ui.Alignment


@Composable
fun CrearLista(
    onDismiss: () -> Unit,
    onGuardar: (String, String) -> Unit
) {
    val idioma by AppPreferences.idioma.collectAsStateWithLifecycle()
    val textos = TextosApp.get(idioma)

    var nombre by rememberSaveable {
        mutableStateOf("")
    }

    var tipoSeleccionado by rememberSaveable{
        mutableStateOf("")
    }

    val tipos = listOf(
        "productos" to textos.tipo_lista_productos,
        "tareas" to textos.tipo_lista_tareas
    )

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

                Spacer(Modifier.height(16.dp))

                Text(textos.tipo_de_lista)

                tipos.forEach { (valor,etiqueta) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable{ tipoSeleccionado = valor },
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        RadioButton(
                            selected = tipoSeleccionado == valor,
                            onClick = { tipoSeleccionado = valor }
                        )
                        Text(etiqueta)
                    }
                }
            }
        },

        confirmButton = {
            TextButton(
                onClick = {onGuardar(nombre, tipoSeleccionado)},
                enabled = nombre.isNotBlank() && tipoSeleccionado.isNotBlank()
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