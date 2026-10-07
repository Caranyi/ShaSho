package com.example.shashoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shashoapp.model.Producto
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnyadirCatalogue(
    catalogo: List<Producto>,
    onProductSeleccionado: (Producto) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
){
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(
                items = catalogo

            ){
                producto ->
                ProductoItem(
                    producto = producto,
                    onCardClick = {onProductSeleccionado(producto)}
                )

            }

        }
    }
}