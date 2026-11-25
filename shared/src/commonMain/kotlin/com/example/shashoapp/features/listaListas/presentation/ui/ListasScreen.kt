package com.example.shashoapp.features.listaListas.presentation.ui

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.TextosApp
import com.example.shashoapp.features.listaListas.domain.model.Lista
import com.example.shashoapp.features.listaListas.presentation.ListasAction
import com.example.shashoapp.features.listaListas.presentation.ListasState
import com.example.shashoapp.features.listaListas.presentation.ListasViewModel

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
    modifier: Modifier
) {
    val idioma by AppPreferences.idioma.collectAsStateWithLifecycle()
    val textos = TextosApp.get(idioma)

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
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
                    text = textos.misListas,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // LISTA DE TARJETAS
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(
                        items = state.listas,
                        key = { i, lista -> "${lista.id}_$i" }
                    ) { _, lista ->
                        ListaItem(
                            lista = lista,
                            onClick = { onAction(ListasAction.OnListaClick(lista)) },
                            onEliminar = { onAction(ListasAction.EliminarLista(lista.id)) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }

            if (state.mostrarCrear) {
                CrearLista(
                    onDismiss = { onAction(ListasAction.CerrarCrearLista) },
                    onGuardar = { nombre, tipo, imagenBytes ->
                        onAction(ListasAction.GuardarLista(nombre, tipo, imagenBytes))
                    }
                )
            }
        }
    }
}