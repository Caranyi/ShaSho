package com.example.shashoapp.features.auth.presentation.register.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shashoapp.features.auth.presentation.register.RegisterAction
import com.example.shashoapp.features.auth.presentation.register.RegisterState
import com.example.shashoapp.features.auth.presentation.register.RegisterViewModel

@Composable
fun RegisterScreenRoot(
    viewModel: RegisterViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.registroExitoso) {
        if (state.registroExitoso) onNavigateToHome()
    }

    RegisterScreen(
        state = state,
        onAction = {
            action -> if (action is RegisterAction.OnIrALoginClick) onNavigateToLogin()
            else viewModel.onAction(action)
        },
        modifier = modifier
    )
}

@Composable
fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Crear Cuenta",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = state.nombre,
            onValueChange = {onAction(RegisterAction.OnNombreChanged(it))},
            label = {Text("Nombre completo")},
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.correo,
            onValueChange = {onAction(RegisterAction.OnCorreoChanged(it))},
            label = {Text("Correo electrónico")},
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.contrasenya,
            onValueChange = {onAction(RegisterAction.OnContrasenyaChanged(it))},
            label = {Text("Contraseña")},
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        state.error?.let {
            err -> Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = err,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {onAction(RegisterAction.OnRegisterClick)},
            enabled = !state.cargando,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.cargando) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            else {
                Text("Registrarse")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {onAction(RegisterAction.OnIrALoginClick)}) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}