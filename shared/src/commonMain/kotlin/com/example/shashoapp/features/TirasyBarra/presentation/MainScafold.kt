package com.example.shashoapp.features.TirasyBarra.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.example.shashoapp.features.BarraNavegacion.presentation.ui.BarraNav
import shashoapp.shared.generated.resources.Res
import shashoapp.shared.generated.resources.addCircle
import shashoapp.shared.generated.resources.home
import shashoapp.shared.generated.resources.settings

@ExperimentalMaterial3Api
@Composable
fun MainScafold(
    onInicio: () -> Unit,
    onCrear: () -> Unit,
    onAjustes: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold (
        topBar = {
            Tira(onClickLogo = onInicio)
        },

        bottomBar = {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Color(0xFF673AB7)),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ){
                BarraNav(
                    icon = Res.drawable.home,
                    label = "inicio",
                    onClick = onInicio
                )

                BarraNav(
                    icon = Res.drawable.addCircle,
                    label = "Perfil",
                    onClick = onCrear
                )

                BarraNav(
                    icon = Res.drawable.settings,
                    label = "Ajustes",
                    onClick = onAjustes
                )
            }

        }
    ){
        innerPadding ->
            content(innerPadding)
    }
}