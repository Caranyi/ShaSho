package com.example.shashoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shashoapp.model.Producto
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnyadirCatalogue(
    catalogo: List<Producto>,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    productosConfirmados: (List<Producto>) -> Unit

){
    val listaCatalogo = remember(catalogo
    ){
        mutableStateListOf<Producto>().apply {
            addAll(catalogo.map {
                it.copy(isChecked = false,
                    cantidad = 0
                )
            })
        }
    }
    val tieneSel = listaCatalogo.any {it.dameCantidad > 0}

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
            itemsIndexed(
                items = listaCatalogo

            ){
                i, producto: Producto ->

                val selected = producto.dameCantidad > 0
                ProductoItem(
                    producto = producto,
                    showCheckBox = false,
                    ensennyaCantidad = selected,
                    onCardClick = {
                        if(!selected){
                            listaCatalogo[i] = producto.copy(isChecked  = true, cantidad = 1)
                        } },
                    incrementaCantidad = {
                        listaCatalogo[i] = producto.copy(cantidad = producto.dameCantidad + 1)
                    },

                    decrementarCantidad = {
                        if(producto.dameCantidad > 1){
                            listaCatalogo[i] = producto.copy(cantidad = producto.dameCantidad - 1)
                        }

                        else{
                            listaCatalogo[i] = producto.copy(isChecked = false, cantidad = 0)
                        }
                    }
                )
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val sel = listaCatalogo.filter {it.dameCantidad > 0}
                productosConfirmados(sel)
            },
            enabled = tieneSel,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)


            ){
                Text(
                    text = "Confirmar",
                    fontSize =  16.sp,
                    fontWeight = FontWeight.Bold
                )
        }
    }


}