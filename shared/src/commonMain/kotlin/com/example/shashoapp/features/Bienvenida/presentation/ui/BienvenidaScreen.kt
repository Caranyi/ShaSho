package com.example.shashoapp.features.Bienvenida.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.TextosApp
import org.jetbrains.compose.resources.painterResource
import shashoapp.shared.generated.resources.Res
import shashoapp.shared.generated.resources.ShaSho

@Composable
fun BienvenidaScreenRoot(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
    conexionOk: Boolean? = null
) {
    BienvenidaScreen(
        onLoginClick = onLoginClick,
        onRegisterClick = onRegisterClick,
        modifier = modifier,
        conexionOk = conexionOk
    )
}

@Composable
fun BienvenidaScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
    conexionOk: Boolean? = null
) {
    val idioma by AppPreferences.idioma.collectAsStateWithLifecycle()
    val textos = TextosApp.get(idioma)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
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
                    painter = painterResource(Res.drawable.ShaSho),
                    contentDescription = "Logo ShaSho",
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
                    text = textos.bienvenidaTitulo,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // SECCIÓN 3: ESTADO CONEXIÓN AL SERVIDOR
            Text(
                text = when (conexionOk) {
                    null -> textos.conectandoServidor
                    true -> textos.conectadoBd
                    false -> textos.sinConexionServidor
                },
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )

            // SECCIÓN 4: ZONA DE BOTONES
            Row(
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(textos.iniciarSesion)
                }

                Button(
                    onClick = onRegisterClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(textos.registrarse)
                }
            }
        }
    }
}