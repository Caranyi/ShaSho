package com.example.shashoapp.features.listaProductos.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shashoapp.features.listaProductos.domain.model.Producto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnyadirCatalogue(
    catalogo: List<Producto>,
    productosConfirmados: (List<Producto>) -> Unit,
    onDismissRequest: () -> Unit
) {
    val seleccionados = remember {
        mutableStateListOf<Producto>().apply {
            addAll(catalogo.map { it.copy(cantidad = 1, isChecked = false) })
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Catálogo de Productos",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF673AB7)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(
                    items = seleccionados,
                    key = { _, prod -> prod.id }
                ) { i, producto ->
                    ProductoItem(
                        producto = producto,
                        showCheckBox = true,
                        ensenyaCantidad = producto.isChecked,
                        onCardClick = {
                            seleccionados[i] = producto.copy(isChecked = !producto.isChecked)
                        },
                        onCheckedChange = { nuevoEstado ->
                            seleccionados[i] = producto.copy(isChecked = nuevoEstado)
                        },
                        incrementarCantidad = {
                            val act = seleccionados[i]
                            seleccionados[i] = act.copy(cantidad = act.dameCantidad + 1)
                        },
                        decrementarCantidad = {
                            val act = seleccionados[i]
                            if (act.dameCantidad > 1) {
                                seleccionados[i] = act.copy(cantidad = act.dameCantidad - 1)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onDismissRequest) {
                    Text("Cancelar", color = Color.Gray)
                }

                Button(
                    onClick = {
                        val confirmados = seleccionados.filter { it.isChecked }
                        productosConfirmados(confirmados)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
                ) {
                    Text("Añadir a la lista")
                }
            }
        }
    }
}