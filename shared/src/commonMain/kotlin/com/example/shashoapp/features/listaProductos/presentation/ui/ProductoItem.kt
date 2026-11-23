package com.example.shashoapp.features.listaProductos.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.TextosApp
import com.example.shashoapp.features.listaProductos.domain.model.Producto

@Composable
fun ProductoItem(
    producto: Producto,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderWith: Dp = 1.dp,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    elevation: Dp = 4.dp,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    showCheckBox: Boolean = false,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    ensenyaCantidad: Boolean = true,
    incrementarCantidad: () -> Unit,
    decrementarCantidad: () -> Unit,
    onEliminar: () -> Unit
) {
    val idioma by AppPreferences.idioma.collectAsStateWithLifecycle()
    val textos = TextosApp.get(idioma)

    var muestraDialogoConfirmacionEliminar by remember {
        mutableStateOf(false)
    }

    Card(
        onClick = onCardClick,
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(borderWith, borderColor),
        elevation = CardDefaults.cardElevation(elevation),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showCheckBox) {
                Checkbox(
                    checked = producto.isChecked,
                    onCheckedChange = onCheckedChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = producto.precioFormateado,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (ensenyaCantidad) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = decrementarCantidad) {
                        Text(
                            "-",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = "${producto.dameCantidad}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    IconButton(onClick = incrementarCantidad) {
                        Text(
                            "+",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
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
                    title = { Text(textos.eliminarProductoTitulo) },
                    text = {
                        Text("${textos.quieresEliminar} ${producto.nombre}?")
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