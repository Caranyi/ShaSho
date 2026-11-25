package com.example.shashoapp.features.listaTareas.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.EstadoConexion
import com.example.shashoapp.core.TextosApp
import com.example.shashoapp.features.listaTareas.presentation.TareasAction
import com.example.shashoapp.features.listaTareas.presentation.TareasState
import com.example.shashoapp.features.listaTareas.presentation.TareasViewModel
import org.jetbrains.compose.resources.painterResource
import shashoapp.shared.generated.resources.Res
import shashoapp.shared.generated.resources.arrowLeft

@Composable
fun TareasScreenRoot(
    viewModel: TareasViewModel,
    listaNombre: String,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TareasScreen(
        state = state,
        listaNombre = listaNombre,
        onAction = viewModel::onAction,
        onVolver = onVolver,
        modifier = modifier
    )
}

@Composable
fun TareasScreen(
    state: TareasState,
    listaNombre: String,
    onAction: (TareasAction) -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val idioma by AppPreferences.idioma.collectAsStateWithLifecycle()
    val textos = TextosApp.get(idioma)

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVolver) {
                    Icon(
                        painter = painterResource(Res.drawable.arrowLeft),
                        contentDescription = textos.anterior,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(34.dp))

                Text(
                    text = listaNombre,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = textos.listaCabecera,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (EstadoConexion.getConexion()) {
                        TextButton(
                            onClick = {
                                onAction(TareasAction.AbrirCompartir)
                            }
                        ) {
                            Text(
                                text = textos.anadirPersona,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Text(
                        text = textos.tipo_lista_tareas,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                if (state.tareas.isEmpty()) {
                    Text(
                        text = textos.tareasVacias,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 16.sp
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        itemsIndexed(
                            items = state.tareas,
                            key = { index, tarea -> "${tarea.id}_$index" }
                        ) { _, tarea ->
                            TareaItem(
                                tarea = tarea,
                                onCardClick = {
                                    onAction(
                                        TareasAction.CambiarCompletada(tarea.id)
                                    )
                                },
                                onCheckedChange = {
                                    onAction(
                                        TareasAction.CambiarCompletada(tarea.id)
                                    )
                                },
                                onEliminar = {
                                    onAction(
                                        TareasAction.EliminarTarea(tarea.id)
                                    )
                                }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(72.dp))
                        }
                    }
                }
            }
        }

        Button(
            onClick = { onAction(TareasAction.AbrirCrear) },
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF673AB7)
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 16.dp)
                .size(56.dp)
        ) {
            Text(
                text = "+",
                fontSize = 28.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        if (state.mostrarCrear) {
            CrearTarea(
                onDismissRequest = {
                    onAction(TareasAction.CerrarCrear)
                },
                onGuardar = { nombre ->
                    onAction(TareasAction.GuardarTarea(nombre))
                }
            )
        }

        if (state.mostrarCompartir) {
            AlertDialog(
                onDismissRequest = {
                    onAction(TareasAction.CerrarCompartir)
                },
                title = {
                    Text(textos.anadirPersona)
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = state.correoCompartir,
                            onValueChange = {
                                onAction(TareasAction.OnCorreoChange(it))
                            },
                            label = { Text(textos.correo) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                autoCorrectEnabled = false
                            )
                        )

                        state.mensajeCompartir?.let { mensaje ->
                            Text(
                                text = mensaje,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onAction(TareasAction.ConfirmarCompartir)
                        }
                    ) {
                        Text(textos.anadir)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onAction(TareasAction.CerrarCompartir)
                        }
                    ) {
                        Text(textos.cancelar)
                    }
                }
            )
        }

        state.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(8.dp)
            )
        }
    }
}