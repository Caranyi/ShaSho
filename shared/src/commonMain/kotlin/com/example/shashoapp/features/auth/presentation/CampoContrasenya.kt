package com.example.shashoapp.features.auth.presentation

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.TextosApp
import org.jetbrains.compose.resources.painterResource
import shashoapp.shared.generated.resources.Res
import shashoapp.shared.generated.resources.eye
import shashoapp.shared.generated.resources.eyeClosed

@Composable
fun CampoContrasenya(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val estaPulsado by interactionSource.collectIsPressedAsState()
    val idioma by AppPreferences.idioma.collectAsStateWithLifecycle()
    val textos = TextosApp.get(idioma)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (estaPulsado) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation(mask = '*')
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(
                onClick = {},
                interactionSource = interactionSource
            ) {
                Icon(
                    painter = painterResource(if (estaPulsado) Res.drawable.eye else Res.drawable.eyeClosed),
                    contentDescription = textos.contrasenya
                )
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}