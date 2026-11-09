package com.example.shashoapp.features.listalistas.presentation.ui

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

import org.jetbrains.compose.resources.painterResource
import shashoapp.shared.generated.resources.Res
import shashoapp.shared.generated.resources.addCircle
import shashoapp.shared.generated.resources.home
import shashoapp.shared.generated.resources.logoShaSho
import shashoapp.shared.generated.resources.settings

import com.example.shashoapp.features.BarraNavegacion.presentation.ui.BarraNav
import com.example.shashoapp.features.listaListas.domain.model.Lista
import com.example.shashoapp.features.listaListas.presentation.ListasAction
import com.example.shashoapp.features.listaListas.presentation.ListasState
import com.example.shashoapp.features.listaListas.presentation.ListasViewModel
import com.example.shashoapp.features.listaListas.presentation.ui.ListaItem

@Composable
fun ListasScreenRoot(
    viewModel: ListasViewModel,
    onListaClick: (Lista) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ListasScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is ListasAction.OnListaClick -> onListaClick(action.lista)
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier
    )
}

@Composable
fun ListasScreen(
    state: ListasState,
    onAction: (ListasAction) -> Unit,
    modifier: Modifier = Modifier

) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            // TÍTULO "MIS LISTAS"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MIS LISTAS",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
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
                        items = state.listas,
                        key = { i, lista -> "${lista.id}_$i"}
                    ) { i,lista ->
                        ListaItem(
                            lista = lista,
                            onClick = { onAction(ListasAction.OnListaClick(lista)) }
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