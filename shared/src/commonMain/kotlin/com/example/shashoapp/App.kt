package com.example.shashoapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.shashoapp.core.ApiServidor
import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.EstadoConexion
import com.example.shashoapp.core.SesionUsuario
import com.example.shashoapp.di.AppContainer
import com.example.shashoapp.di.DefaultAppContainer
import com.example.shashoapp.features.Ajustes.presentation.AjustesViewModel
import com.example.shashoapp.features.Ajustes.presentation.ui.AjustesScreenRoot
import com.example.shashoapp.features.Bienvenida.presentation.ui.BienvenidaScreenRoot
import com.example.shashoapp.features.Reparto.presentation.RepartoViewModel
import com.example.shashoapp.features.Reparto.presentation.ui.RepartoScreenRoot
import com.example.shashoapp.features.TirasyBarra.presentation.MainScafold
import com.example.shashoapp.features.auth.presentation.login.LoginViewModel
import com.example.shashoapp.features.auth.presentation.login.ui.LoginScreenRoot
import com.example.shashoapp.features.auth.presentation.register.RegisterViewModel
import com.example.shashoapp.features.auth.presentation.register.ui.RegisterScreenRoot
import com.example.shashoapp.features.listaListas.presentation.ListasAction
import com.example.shashoapp.features.listaListas.presentation.ListasViewModel
import com.example.shashoapp.features.listaListas.presentation.ui.ListasScreenRoot
import com.example.shashoapp.features.listaProductos.presentation.ProductosViewModel
import com.example.shashoapp.features.listaProductos.presentation.ui.ProductosScreenRoot
import com.example.shashoapp.features.listaTareas.presentation.ui.TareasScreenRoot
import com.example.shashoapp.features.listaTareas.presentation.TareasViewModel

@Composable
fun App(
    appContainer: AppContainer = DefaultAppContainer(),
    toScreen: Route? = null
) {
    val idiomaActual by AppPreferences.idioma.collectAsStateWithLifecycle()
    val modoOscuro by AppPreferences.modoOscuro.collectAsStateWithLifecycle()

    val esquemaColores = if (modoOscuro) {
        darkColorScheme(
            primary = Color(0xFF9575CD),
            onPrimary = Color.White,
            background = Color(0xFF121212),
            onBackground = Color(0xFFEDEDED),
            surface = Color(0xFF1E1E1E),
            onSurface = Color(0xFFEDEDED),
            surfaceVariant = Color(0xFF262626),
            onSurfaceVariant = Color(0xFFBDBDBD)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF673AB7),
            onPrimary = Color.White,
            background = Color.White,
            onBackground = Color.Black,
            surface = Color.White,
            onSurface = Color.Black,
            surfaceVariant = Color(0xFFF5F5F5),
            onSurfaceVariant = Color.DarkGray
        )
    }

    MaterialTheme(colorScheme = esquemaColores) {
        val navController = rememberNavController()
        var conexionOk by remember { mutableStateOf<Boolean?>(null) }

        LaunchedEffect(toScreen) {
            AppPreferences.cargarPreferencias()
            val idGuardado = AppPreferences.idUsuario.value

            if (idGuardado != null && idGuardado > 0) {
                SesionUsuario.setIdUsuario(idGuardado)
            }

            val hayConexion = ApiServidor.ping()
            conexionOk = hayConexion
            EstadoConexion.setConexion(hayConexion)

            if (toScreen != null) {
                val usuarioActivo = SesionUsuario.getIdUsuario()
                if (usuarioActivo == null && hayConexion) {
                    navController.navigate(Route.Login) {
                        popUpTo(Route.Bienvenida) { inclusive = true }
                    }
                } else {
                    navController.navigate(toScreen) { launchSingleTop = true }
                }
            } else if (hayConexion) {
                if (idGuardado != null && idGuardado > 0) {
                    navController.navigate(Route.Home) {
                        popUpTo(Route.Bienvenida) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            } else {
                val idOffline = idGuardado ?: -1
                SesionUsuario.setIdUsuario(idOffline)

                navController.navigate(Route.Home) {
                    popUpTo(Route.Bienvenida) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }

        NavHost(navController = navController, startDestination = Route.Bienvenida) {

            composable<Route.Bienvenida> {
                BienvenidaScreenRoot(
                    onLoginClick = { navController.navigate(Route.Login) },
                    onRegisterClick = { navController.navigate(Route.Register) },
                    conexionOk = conexionOk
                )
            }

            composable<Route.Home> {
                val idUsuario = SesionUsuario.getIdUsuario() ?: -1

                val viewModel = remember(idUsuario) {
                    ListasViewModel(
                        compartirListaUseCase = appContainer.compartirListaUseCase,
                        getListasUseCase = appContainer.getListasUseCase,
                        sincronizarListasUseCase = appContainer.sincronizarListasUseCase,
                        crearListaUseCase = appContainer.crearListaUseCase,
                        getProductosListaUseCase = appContainer.getProductosListaUseCase,
                        eliminarListaUseCase = appContainer.eliminarListaUseCase,
                        idUsuario = idUsuario
                    )
                }

                MainScafold(
                    onInicio = { navController.popBackStack(Route.Home, false) },
                    onCrear = { viewModel.onAction(ListasAction.AbrirCrearLista) },
                    onAjustes = { navController.navigate(Route.Ajustes) }
                ) { inner ->
                    ListasScreenRoot(
                        viewModel = viewModel,
                        onListaClick = { lista ->
                            if(lista.tipo == "productos"){
                                navController.navigate(Route.ListaProductos(id = lista.id, nombre = lista.nombre))
                            }else{
                                navController.navigate(Route.ListaTareas(id = lista.id, nombre = lista.nombre))
                            }
                        },
                        modifier = Modifier.padding(inner)
                    )
                }
            }

            composable<Route.ListaProductos> { backStackEntry ->
                val route: Route.ListaProductos = backStackEntry.toRoute()

                val viewModel = remember(route.id) {
                    ProductosViewModel(
                        getProductosListaUseCase = appContainer.getProductosListaUseCase,
                        compartirListaUseCase = appContainer.compartirListaUseCase,
                        crearProductosUseCase = appContainer.crearProductosUseCase,
                        actualizarMarcadosUseCase = appContainer.actualizarMarcadosUseCase,
                        eliminarProductoUseCase = appContainer.eliminarProductoUseCase,
                        actualizaCantidadesUseCase = appContainer.actualizaCantidadesUseCase,
                        idLista = route.id
                    )
                }
                MainScafold(
                    onInicio = { navController.popBackStack(Route.Home, false) },
                    onCrear = {},
                    onAjustes = { navController.navigate(Route.Ajustes) }
                ) { inner ->
                    ProductosScreenRoot(
                        viewModel = viewModel,
                        onVolver = { navController.popBackStack() },
                        onHome = { navController.popBackStack(Route.Home, inclusive = false) },
                        modifier = Modifier.padding(inner),
                        onReparto = { navController.navigate(Route.Reparto(route.id)) },
                        listaNombre = route.nombre
                    )
                }
            }

            composable<Route.ListaTareas> { backStackEntry ->
                val route: Route.ListaTareas = backStackEntry.toRoute()

                val viewModel = remember(route.id) {
                    TareasViewModel(
                        getTareasUseCase = appContainer.getTareasUseCase,
                        compartirListaUseCase = appContainer.compartirListaUseCase,
                        crearTareaUseCase = appContainer.crearTareaUseCase,
                        actualizarTareaCompletadaUseCase =
                            appContainer.actualizarTareaCompletadaUseCase,
                        eliminarTareaUseCase = appContainer.eliminarTareaUseCase,
                        idLista = route.id,
                    )
                }
                MainScafold(
                    onInicio = { navController.popBackStack(Route.Home, false) },
                    onCrear = {},
                    onAjustes = { navController.navigate(Route.Ajustes) }
                ) { inner ->
                    TareasScreenRoot(
                        viewModel = viewModel,
                        listaNombre = route.nombre,
                        onVolver = { navController.popBackStack() },
                        modifier = Modifier.padding(inner)
                    )
                }
            }

            composable<Route.Login> {
                val viewModel = remember { LoginViewModel(appContainer.loginUseCase) }
                LoginScreenRoot(
                    viewModel = viewModel,
                    onNavigateToHome = {
                        navController.navigate(Route.Home) {
                            popUpTo(Route.Bienvenida) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Route.Register)
                    }
                )
            }

            composable<Route.Register> {
                val viewModel = remember { RegisterViewModel(appContainer.registerUseCase) }
                RegisterScreenRoot(
                    viewModel = viewModel,
                    onNavigateToHome = {
                        navController.navigate(Route.Home) {
                            popUpTo(Route.Bienvenida) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate(Route.Login)
                    }
                )
            }

            composable<Route.Reparto> { backStackEntry ->
                val route: Route.Reparto = backStackEntry.toRoute()

                val viewModel = remember(route.listaId) {
                    RepartoViewModel(
                        getRepartoUseCase = appContainer.getRepartoUSeCase,
                        listaId = route.listaId
                    )
                }

                MainScafold(
                    onInicio = { navController.popBackStack(Route.Home, false) },
                    onCrear = {},
                    onAjustes = { navController.navigate(Route.Ajustes) }
                ) { inner ->
                    RepartoScreenRoot(
                        viewModel = viewModel,
                        onVolver = { navController.popBackStack() },
                        onHome = { navController.popBackStack(Route.Home, false) },
                        modifier = Modifier.padding(inner)
                    )
                }
            }

            composable<Route.Ajustes> {
                val viewModel = remember { AjustesViewModel(appContainer.actualizarNombreUsuarioUseCase) }

                MainScafold(
                    onInicio = { navController.popBackStack(Route.Home, false) },
                    onCrear = {},
                    onAjustes = { navController.navigate(Route.Ajustes) }
                ) { inner ->
                    AjustesScreenRoot(
                        viewModel = viewModel,
                        onIrHome = {
                            navController.popBackStack(Route.Home, inclusive = false)
                        },
                        onCerrarSesion = {
                            navController.navigate(Route.Bienvenida) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        modifier = Modifier.padding(inner)
                    )
                }
            }
        }
    }
}