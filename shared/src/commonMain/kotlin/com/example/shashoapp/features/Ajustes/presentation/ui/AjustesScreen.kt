package com.example.shashoapp.features.Ajustes.presentation.ui

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
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
import com.example.shashoapp.Route
import com.example.shashoapp.features.Ajustes.presentation.AjustesAction
import com.example.shashoapp.features.Ajustes.presentation.AjustesState
import com.example.shashoapp.features.Ajustes.presentation.AjustesViewModel
import com.example.shashoapp.features.BarraNavegacion.presentation.ui.BarraNav
import com.example.shashoapp.features.listaListas.presentation.ListasAction
import com.example.shashoapp.features.listaProductos.presentation.ProductosAction
import com.example.shashoapp.features.listaProductos.presentation.ui.ProductoItem
import org.jetbrains.compose.resources.painterResource
import shashoapp.shared.generated.resources.Res
import shashoapp.shared.generated.resources.addCircle
import shashoapp.shared.generated.resources.home
import shashoapp.shared.generated.resources.settings

@Composable
fun AjustesScreenRoot(
    viewModel: AjustesViewModel,
    onIrHome: () -> Unit,
    onCerrarSesion: () -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    AjustesScreen(
        state = state,
        onAction = {action ->
            if(action is AjustesAction.onCerrarSesionClick) {
                viewModel.onAction(action)
                onCerrarSesion()
            }else{
                viewModel.onAction(action)
            }
        },
        onIrHome = onIrHome
    )
}

@Composable
fun AjustesScreen(
    state: AjustesState,
    onAction: (AjustesAction) -> Unit,
    onIrHome: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(Color(0xFF673AB7)),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Ajustes",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ){
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Usuario #${state.idUsuario}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (state.enLinea) "Modo en línea activado" else "Modo sin conexión",
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                    }

                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text("Notificaciones", fontSize = 16.sp)
                        Switch(
                            checked = state.notificacionesActivas,
                            onCheckedChange = { onAction(AjustesAction.onActivarNotificaciones(it))}
                        )
                    }
                }


                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {onAction(AjustesAction.onCerrarSesionClick)},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4137)),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ){
                    Text(
                        "Cerrar sesión",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }

        }
    }
}
