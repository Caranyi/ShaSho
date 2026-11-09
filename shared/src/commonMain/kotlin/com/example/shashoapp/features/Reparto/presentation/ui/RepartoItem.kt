package com.example.shashoapp.features.Reparto.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shashoapp.features.Reparto.domain.model.Participante
import com.example.shashoapp.features.listaListas.presentation.ListasAction
import kotlinx.serialization.StringFormat
import kotlin.math.roundToInt

@Composable
fun RepartoItem(
    participante: Participante,
    onCardClick: () -> Unit,
    onPorcentajeChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    borderWith: Dp = 1.dp,
    borderColor: Color = Color.Black,
    elevation: Dp = 4.dp,
    containerColor: Color = Color.White,
    ){

    Card(
        onClick = onCardClick,
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(width = borderWith, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(modifier = Modifier.weight(1f)){
                Text(
                    text = participante.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ){
                OutlinedTextField(
                    value = participante.porcentaje.toString(),
                    onValueChange = { nuevoValor ->
                        onPorcentajeChanged(nuevoValor.toIntOrNull() ?: 0)
                    },
                    modifier = modifier.width(100.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    suffix = {Text("%")},
                )
            }
        }

    }

}