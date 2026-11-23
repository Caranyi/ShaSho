package com.example.shashoapp.features.Ajustes.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shashoapp.core.EstadoConexion
import com.example.shashoapp.core.TextosApp
import com.example.shashoapp.core.aImageBitmap
import com.example.shashoapp.core.rememberSelectorImagenPerfil
import com.example.shashoapp.features.Ajustes.presentation.AjustesAction
import com.example.shashoapp.features.Ajustes.presentation.AjustesState
import com.example.shashoapp.features.Ajustes.presentation.AjustesViewModel

@Composable
fun AjustesScreenRoot(
    viewModel: AjustesViewModel,
    onIrHome: () -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AjustesScreen(
        state = state,
        onAction = { action ->
            if (action is AjustesAction.onCerrarSesionClick) {
                viewModel.onAction(action)
                onCerrarSesion()
            } else {
                viewModel.onAction(action)
            }
        },
        onIrHome = onIrHome,
        modifier = modifier
    )
}

@Composable
fun AjustesScreen(
    state: AjustesState,
    onAction: (AjustesAction) -> Unit,
    onIrHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textos = TextosApp.get(state.idiomaActual)
    val abrirGaleria = rememberSelectorImagenPerfil { bytes ->
        onAction(AjustesAction.onFotoPerfilSeleccionada(bytes))
    }
    val bitmapPerfil = remember(state.fotoPerfilBytes) {
        state.fotoPerfilBytes?.aImageBitmap()
    }

    val fondoPantalla = MaterialTheme.colorScheme.background
    val fondoTarjeta = MaterialTheme.colorScheme.surfaceVariant
    val textoPrincipal = MaterialTheme.colorScheme.onBackground
    val textoSecundario = MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(fondoPantalla)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = textos.ajustes.uppercase(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = textoPrincipal,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Tarjeta de perfil
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = fondoTarjeta)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF673AB7))
                                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                .clickable { abrirGaleria() },
                            contentAlignment = Alignment.Center
                        ) {
                            if (bitmapPerfil != null) {
                                Image(
                                    bitmap = bitmapPerfil,
                                    contentDescription = textos.cambiarFotoPerfil,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                val inicial = state.nombreUsuario.firstOrNull()?.uppercase()
                                    ?: if ((state.idUsuario ?: -1) > 0) state.idUsuario.toString() else "U"
                                Text(
                                    text = inicial,
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            val nombreMostrado = state.nombreUsuario.ifBlank {
                                "${textos.usuarioPrefix}${state.idUsuario ?: "-"}"
                            }
                            Text(
                                text = nombreMostrado,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = textoPrincipal,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${textos.usuarioPrefix}${state.idUsuario ?: "-"} · " +
                                        if (state.enLinea) textos.modoOnline else textos.modoOffline,
                                fontSize = 13.sp,
                                color = textoSecundario
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { abrirGaleria() },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = textos.cambiarFotoPerfil,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        OutlinedButton(
                            onClick = { onAction(AjustesAction.onAbrirDialogoNombre) },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = textos.cambiarNombreUsuario,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            // Modo oscuro
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = fondoTarjeta)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = textos.modoOscuro,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = textoPrincipal
                    )
                    Switch(
                        checked = state.modoOscuro,
                        onCheckedChange = { onAction(AjustesAction.onActivarModoOscuro(it)) },
                        colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF673AB7))
                    )
                }
            }

            // Notificaciones
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = fondoTarjeta)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = textos.notificaciones,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (state.enLinea) textoPrincipal else textoSecundario
                    )
                    Switch(
                        checked = if (EstadoConexion.getConexion()) state.notificacionesActivas else false,
                        onCheckedChange = { activado ->
                            onAction(AjustesAction.onActivarNotificaciones(activado))
                        },
                        enabled = state.enLinea,
                        colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF673AB7))
                    )
                }
            }

            // Idioma
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = fondoTarjeta)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = textos.idiomaLabel,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = textoPrincipal
                    )
                    Box {
                        OutlinedButton(
                            onClick = { onAction(AjustesAction.onToggleMenuIdioma) }
                        ) {
                            Text(
                                text = if (state.idiomaActual == "en") textos.ingles else textos.espanol,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        DropdownMenu(
                            expanded = state.menuIdiomaAbierto,
                            onDismissRequest = { onAction(AjustesAction.onCerrarMenuIdioma) }
                        ) {
                            DropdownMenuItem(
                                text = { Text(textos.espanol) },
                                onClick = { onAction(AjustesAction.onCambiarIdioma("es")) }
                            )
                            DropdownMenuItem(
                                text = { Text(textos.ingles) },
                                onClick = { onAction(AjustesAction.onCambiarIdioma("en")) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onAction(AjustesAction.onCerrarSesionClick) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4137)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = textos.cerrarSesion,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }

        if (state.mostrarDialogoNombre) {
            AlertDialog(
                onDismissRequest = { onAction(AjustesAction.onCerrarDialogoNombre) },
                title = { Text(textos.cambiarNombreUsuario) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = state.nombreTemporal,
                            onValueChange = { onAction(AjustesAction.onNombreTemporalChanged(it)) },
                            label = { Text(textos.nuevoNombreUsuario) },
                            singleLine = true
                        )
                        state.errorNombre?.let { err ->
                            Text(
                                text = err,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { onAction(AjustesAction.onConfirmarCambioNombre) },
                        enabled = !state.guardandoNombre && state.nombreTemporal.isNotBlank()
                    ) {
                        Text(textos.guardar)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onAction(AjustesAction.onCerrarDialogoNombre) }) {
                        Text(textos.cancelar)
                    }
                }
            )
        }
    }
}