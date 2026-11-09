package com.example.shashoapp.features.TirasyBarra.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import shashoapp.shared.generated.resources.Res
import shashoapp.shared.generated.resources.logoShaSho

@Composable
fun Tira(
    onClickLogo: () -> Unit,
    modifier: Modifier = Modifier
){
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
                .background(Color.White)
                .clickable {onClickLogo()},
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
}