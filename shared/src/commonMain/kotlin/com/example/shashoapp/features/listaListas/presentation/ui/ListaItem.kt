package com.example.shashoapp.features.listaListas.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.TextosApp
import com.example.shashoapp.features.listaListas.domain.model.Lista

@Composable
fun ListaItem(
    lista: Lista,
    onClick: () -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val idioma by AppPreferences.idioma.collectAsStateWithLifecycle()
    val textos = TextosApp.get(idioma)

    var muestraDialogoConfirmacionEliminar by remember {
        mutableStateOf(false)
    }

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lista.nombre,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // indica si la lista es compartida
                if (lista.compartida) {
                    Text(
                        text = textos.compartida,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(
                onClick = { muestraDialogoConfirmacionEliminar = true },
                modifier = Modifier.size(32.dp)
            ) {
                Text(
                    text = "x",
                    color = Color.Red,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (muestraDialogoConfirmacionEliminar) {
                AlertDialog(
                    onDismissRequest = { muestraDialogoConfirmacionEliminar = false },
                    title = { Text(textos.eliminarListaTitulo) },
                    text = {
                        Text("${textos.quieresEliminar} ${lista.nombre}?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                muestraDialogoConfirmacionEliminar = false
                                onEliminar()
                            }
                        ) {
                            Text(textos.eliminar, color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { muestraDialogoConfirmacionEliminar = false }
                        ) {
                            Text(textos.cancelar)
                        }
                    }
                )
            }
        }
    }
}