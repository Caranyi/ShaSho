package com.example.shashoapp.features.auth.presentation.login.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.TextosApp
import com.example.shashoapp.features.auth.presentation.login.LoginAction
import com.example.shashoapp.features.auth.presentation.login.LoginState
import com.example.shashoapp.features.auth.presentation.login.LoginViewModel

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.loginExitoso) {
        if (state.loginExitoso) onNavigateToHome()
    }

    LoginScreen(
        state = state,
        onAction = {
            action -> if (action is LoginAction.OnIrARegistroClick) onNavigateToRegister()
            else viewModel.onAction(action)
        },
        modifier = modifier
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val idioma by AppPreferences.idioma.collectAsStateWithLifecycle()
    val textos = TextosApp.get(idioma)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = textos.iniciarSesion,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = state.correo,
            onValueChange = {onAction(LoginAction.OnCorreoChanged(it))},
            label = {Text(textos.correoElectronico)},
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.contrasenya,
            onValueChange = {onAction(LoginAction.OnContrasenyaChanged(it))},
            label = {Text(textos.contrasenya)},
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
            onClick = {onAction(LoginAction.OnLoginClick)},
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
                Text(textos.entrar)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {onAction(LoginAction.OnIrARegistroClick)}) {
            Text(textos.noTienesCuenta)
        }
    }
}