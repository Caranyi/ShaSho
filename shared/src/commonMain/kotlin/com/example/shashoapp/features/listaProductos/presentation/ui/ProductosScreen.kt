package com.example.shashoapp.features.listaProductos.presentation.ui

import com.example.shashoapp.features.BarraNavegacion.presentation.ui.BarraNav
import androidx.compose.foundation.Image
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

@Composable
fun ProductosScreenRoot(
    viewModel: ProductosViewModel,
    onVolver: () -> Unit,
    onHome: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ProductosScreen(
        state = state,
        onAction = viewModel::onAction,
        onVolver = onVolver,
        onHome = onHome
    )
}

@Composable
fun ProductosScreen(
    state: ProductosState,
    onAction: (ProductosAction) -> Unit,
    onVolver: () -> Unit,
    onHome: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            // SECCIÓN 1: CABECERA MORADA CON LOGO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(Color(0xFF673AB7)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.logoShaSho),
                        contentDescription = "Logo ShaSho",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(90.dp)
                            .padding(6.dp)
                    )
                }
            }

            // SECCIÓN 2: TÍTULO DE LA LISTA
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVolver) {
                    Icon(
                        painter = painterResource(Res.drawable.arrowLeft),
                        contentDescription = "Anterior",
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Text(
                    text = "PISO",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                IconButton(onClick = { /* ir a lista siguiente */ }) {
                    Icon(
                        painter = painterResource(Res.drawable.arrowRight),
                        contentDescription = "Siguiente",
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                }
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
                    text = "LISTA",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* ir a reparto */ }) {
                        Icon(
                            painter = painterResource(Res.drawable.reparto),
                            contentDescription = "Repartir pagos lista",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(onClick = { /* ir a filtro */ }) {
                        Icon(
                            painter = painterResource(Res.drawable.filtro),
                            contentDescription = "Filtrar productos lista",
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
                            text = "La lista está vacía.\n Añade un producto con +",
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

        // SECCIÓN 5: BARRA DE NAVEGACIÓN INFERIOR
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(74.dp)
                .background(Color(0xFF673AB7))
                .align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BarraNav(
                    icon = Res.drawable.home,
                    label = "Inicio",
                    onClick = onHome
                )
                BarraNav(
                    icon = Res.drawable.addCircle,
                    label = "Crear",
                    onClick = { /* Acción de crear una lista */}
                )
                BarraNav(
                    icon = Res.drawable.settings,
                    label = "Ajustes",
                    onClick = { /* Acción ajustes */ }
                )
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
            AnnyadirCatalogue(
                catalogo = state.productosDisponibles,
                productosConfirmados = { listaNuevos ->
                    onAction(ProductosAction.ConfirmarProductos(listaNuevos))
                },
                onDismissRequest = {
                    onAction(ProductosAction.CerrarMenuCrear)
                }
            )
        }
    }
}