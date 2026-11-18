package com.example.shashoapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.SesionUsuario
import com.example.shashoapp.core.TextosApp
import com.example.shashoapp.core.database.iniciarAlmacenLocal
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class MainActivity : ComponentActivity() {

    private var toScreen by mutableStateOf<Route?>(null)

    private val pedirPermisoNotificaciones = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        //INICIA ALMACENAMIENTO LOCAL
        iniciarAlmacenLocal(applicationContext)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {
                pedirPermisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        toScreen = jsonToRoute(intent)

        setContent {
            App(
                toScreen = toScreen
            )
        }
    }

    override fun onStart() {
        super.onStart()
        Notification.cancelNotifications(this)
    }

    override fun onStop() {
        super.onStop()
        val notificacionesActivas = AppPreferences.notificacionesActivas.value
        val haySesion = SesionUsuario.getIdUsuario() != null
        if (notificacionesActivas && haySesion) {
            val textos = TextosApp.get(AppPreferences.idioma.value)
            Notification.scheduleNotification(
                context = this,
                title = textos.notifTitulo,
                message = textos.notifMensaje,
                delaySeconds = 5,
                toScreen = toScreen ?: Route.Home
            )
        } else {
            Notification.cancelNotifications(this)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        toScreen = jsonToRoute(intent)
    }

    private fun jsonToRoute(intent: Intent): Route? =
        intent?.getStringExtra(EXTRA_TO_SCREEN)
            ?.let { runCatching { Json.decodeFromString<Route?>(it) }.getOrNull() }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}