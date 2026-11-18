package com.example.shashoapp.features.listaProductos.presentation.ui

import com.example.shashoapp.features.BarraNavegacion.presentation.ui.BarraNav
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shashoapp.Route

// Imports de recursos visuales (Drawables)
import shashoapp.shared.generated.resources.Res
import shashoapp.shared.generated.resources.logoShaSho
import shashoapp.shared.generated.resources.home
import shashoapp.shared.generated.resources.addCircle
import shashoapp.shared.generated.resources.settings
import shashoapp.shared.generated.resources.arrowLeft
import shashoapp.shared.generated.resources.arrowRight

// Imports de clases propias del proyecto
import com.example.shashoapp.features.listaProductos.presentation.ProductosAction
import com.example.shashoapp.features.listaProductos.presentation.ProductosState
import com.example.shashoapp.features.listaProductos.presentation.ProductosViewModel
import org.jetbrains.compose.resources.painterResource
import shashoapp.shared.generated.resources.filtro
import shashoapp.shared.generated.resources.reparto

//compartir listas
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.TextosApp


@Composable
fun ProductosScreenRoot(
    viewModel: ProductosViewModel,
    listaNombre: String,
    onVolver: () -> Unit,
    onHome: () -> Unit,
    onReparto: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ProductosScreen(
        state = state,
        listaNombre = listaNombre,
        onAction = viewModel::onAction,
        onVolver = onVolver,
        onHome = onHome,
        onReparto = onReparto,
        modifier = modifier
    )
}

@Composable
fun ProductosScreen(
    state: ProductosState,
    listaNombre: String,
    onAction: (ProductosAction) -> Unit,
    onVolver: () -> Unit,
    onReparto:() -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val idioma by AppPreferences.idioma.collectAsStateWithLifecycle()
    val textos = TextosApp.get(idioma)

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            // SECCIÓN 2: TÍTULO DE LA LISTA
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
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(34.dp))

                Text(
                    text = listaNombre,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // SECCIÓN 3: IDENTIFICADOR DE LISTA Y BOTONES DE FILTRADO Y REPARTO
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = textos.listaCabecera,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //COMPARTIR LISTA
                    TextButton(onClick = { onAction(ProductosAction.AbrirCompartir) }) {
                        Text(
                            text = textos.anadirPersona,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }

                    IconButton(onClick = onReparto) {
                        Icon(
                            painter = painterResource(Res.drawable.reparto),
                            contentDescription = textos.repartoTitulo,
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                }
            }

            // SECCIÓN 4: LISTA DE PRODUCTOS
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFF5F5F5))
            ) {
                if (state.productosLista.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = textos.listaVacia,
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        itemsIndexed(
                            items = state.productosLista,
                            key = { i, prod -> "${prod.id}_$i" }
                        ) { i, producto ->
                            ProductoItem(
                                producto = producto,
                                showCheckBox = true,
                                ensenyaCantidad = true,
                                onCardClick = {
                                    onAction(ProductosAction.ToggleChecked(i))
                                },
                                onCheckedChange = {
                                    onAction(ProductosAction.ToggleChecked(i))
                                },
                                incrementarCantidad = {
                                    onAction(ProductosAction.IncrementarCantidad(i))
                                },
                                decrementarCantidad = {
                                    onAction(ProductosAction.DecrementarCantidad(i))
                                },
                                onEliminar = {
                                    onAction(ProductosAction.EliminarProducto(producto.id))
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

        // SECCIÓN 6: BOTÓN FLOTANTE AÑADIR PRODUCTO
        Button(
            onClick = { onAction(ProductosAction.AbrirMenuCrear) },
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom=90.dp, end=16.dp)
                .size(56.dp)
        ) {
            Text(
                text = "+",
                fontSize = 28.sp,
                textAlign = TextAlign.Center
            )
        }

        // SECCIÓN 7: DESPLEGABLE DE AÑADIR CATÁLOGO DE PRODUCTOS
        if (state.showCreateMenu) {
            CrearProducto(
                onDismissRequest = {
                    onAction(ProductosAction.CerrarMenuCrear)
                },
                onGuardar = {
                    nombre, tipo, cantidad, precio, tienda ->
                        onAction(
                            ProductosAction.GuardarProducto(
                                nombre,
                                tipo,
                                cantidad,
                                precio,
                                tienda
                            )
                        )
                }
            )
        }

        // SECCION 8: COMPARTIR LISTA
        if (state.mostrarCompartir) {
            AlertDialog(
                onDismissRequest = { onAction(ProductosAction.CerrarCompartir) },
                title = { Text(textos.anadirPersona) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = state.correoCompartir,
                            onValueChange = { onAction(ProductosAction.OnCorreoChange(it)) },
                            label = { Text(textos.correo) },
                            singleLine = true
                        )
                        state.mensajeCompartir?.let { mensaje ->
                            Text(
                                text = mensaje,
                                color = Color.Red,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { onAction(ProductosAction.ConfirmarCompartir) }) {
                        Text(textos.anadir)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onAction(ProductosAction.CerrarCompartir) }) {
                        Text(textos.cancelar)
                    }
                }
            )
        }
    }
}