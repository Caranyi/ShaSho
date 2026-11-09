package com.example.shashoapp.features.Reparto.presentation.ui

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shashoapp.features.Reparto.presentation.RepartoState
import com.example.shashoapp.features.Reparto.presentation.RepartoViewModel
import org.jetbrains.compose.resources.painterResource
import shashoapp.shared.generated.resources.Res
import shashoapp.shared.generated.resources.arrowLeft
import shashoapp.shared.generated.resources.logoShaSho
import androidx.compose.runtime.getValue
import com.example.shashoapp.features.Reparto.domain.model.Participante
import com.example.shashoapp.features.Reparto.presentation.RepartoAction
import com.example.shashoapp.features.listaListas.presentation.ListasAction
import com.example.shashoapp.features.listaListas.presentation.ui.ListaItem
import kotlin.math.roundToInt

@Composable
fun RepartoScreenRoot(
    viewModel: RepartoViewModel,
    onVolver: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier
    ){
    val state by viewModel.state.collectAsStateWithLifecycle()

    RepartoScreen(
        state = state,
        onVolver = onVolver,
        onHome = onHome,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
fun RepartoScreen(
    state: RepartoState,
    onVolver: () -> Unit,
    onHome: () -> Unit,
    onAction: (RepartoAction) -> Unit,
    modifier: Modifier = Modifier
){
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // SECCIÓN 2: TÍTULO DE LA LISTA Y PORCENTAJEPORREPARTIR
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
                    text = "REPARTO",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = "Queda: ${state.porcentajeRestante}%",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if(state.porcentajeRestante == 0) Color(0xFF673AB7) else Color(0xFFFF0000)
                )

            }


            // LISTA DE TARJETAS
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFF5F5F5))
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(
                        items = state.participantes,
                        key = { i, participantes -> "${participantes.usuarioId}_$i"}
                    ) { i, participante->
                        RepartoItem(
                            participante = participante,
                            onCardClick = {},
                            onPorcentajeChanged = {
                                nuevoValor ->
                                    onAction(RepartoAction.OnPorcentajeChanged(participante.usuarioId, nuevoValor))
                            }
                        )

                    }
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }

    }
}
