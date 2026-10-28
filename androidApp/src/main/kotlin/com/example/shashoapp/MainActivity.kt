package com.example.shashoapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.serialization.json.Json


class MainActivity : ComponentActivity() {

    private var toScreen by mutableStateOf<Route?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(
                toScreen = toScreen
            )
        }
    }

    override fun onStop() {
        super.onStop()
        Notification.scheduleNotification(
            this, "PAD", "Vuelve a entrar!!", 5,
            toScreen ?: Route.Home
        )
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        toScreen = jsonToRoute(intent)
    }

    private fun jsonToRoute(intent: Intent?): Route? =
        intent?.getStringExtra(EXTRA_TO_SCREEN)
            ?.let { runCatching { Json.decodeFromString<Route>(it) }.getOrNull() }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}