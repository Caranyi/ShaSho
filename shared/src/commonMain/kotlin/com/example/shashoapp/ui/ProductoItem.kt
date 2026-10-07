package com.example.shashoapp.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shashoapp.model.Producto

//En este file, vamos a encapsular las funciones box de enseñar un producto
@Composable
internal fun ProductoItem(
    producto: Producto,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier,

    borderWith: Dp = 1.dp,
    borderColor: Color = Color.Black,
    elevation: Dp = 4.dp,
    containerColor: Color = Color.White,

    showCheckBox: Boolean = true,
    onCheckedChange: ((Boolean) -> Unit)? = null

){
    Card(onClick = onCardClick,
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(borderWith, borderColor),
        elevation = CardDefaults.cardElevation(elevation),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (showCheckBox ) {
                Checkbox(
                    checked = producto.isChecked,
                    onCheckedChange = null,
                    colors = CheckboxDefaults.colors(
                        Color(
                            0xFF673AB7
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.width(12.dp)) //para separar la checkbox del texto

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = producto.precioFormateado)
            }
        }

    }

}
