package com.example.shashoapp.features.Bienvenida.presentation.ui

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shashoapp.features.listaProductos.presentation.ProductosAction
import com.example.shashoapp.features.listaProductos.presentation.ui.ProductoItem
import org.jetbrains.compose.resources.painterResource
import shashoapp.shared.generated.resources.Res
import shashoapp.shared.generated.resources.arrowLeft
import shashoapp.shared.generated.resources.arrowRight
import shashoapp.shared.generated.resources.filtro
import shashoapp.shared.generated.resources.logoShaSho
import shashoapp.shared.generated.resources.reparto

@Composable
fun BienvenidaScreenRoot(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
){
    BienvenidaScreen(
        onLoginClick = onLoginClick,
        onRegisterClick = onRegisterClick,
        modifier = modifier
    )
}

@Composable
fun BienvenidaScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            // SECCIÓN 1: ZONA MORADA CON LOGO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(700.dp)
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(Color(0xFF673AB7)),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(Res.drawable.logoShaSho),
                    contentDescription = "Logo ShaSho",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(320.dp)
                        .clip(CircleShape)
                )
            }

            // SECCIÓN 2: BIENVENIDA
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "BIENVENIDO SHASHO/A!",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // SECCIÓN 3: ZONA DE BOTONES
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){

                Button(
                    onClick = onLoginClick,
                    modifier = Modifier.weight(1f)
                ){
                    Text("Iniciar sesión");
                }

                Button(
                    onClick = onRegisterClick,
                    modifier = Modifier.weight(1f)
                ){
                    Text("Registrarse");
                }
            }
        }
    }
}
