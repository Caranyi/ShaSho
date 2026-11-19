    package com.example.shashoapp

    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.modifier.modifierLocalOf
    import androidx.lifecycle.compose.collectAsStateWithLifecycle
    import com.example.shashoapp.core.ApiServidor
    import androidx.navigation.compose.NavHost
    import androidx.navigation.compose.composable
    import androidx.navigation.compose.rememberNavController
    import androidx.navigation.toRoute
    import com.example.shashoapp.core.AppPreferences
    import com.example.shashoapp.di.AppContainer
    import com.example.shashoapp.di.DefaultAppContainer
    import com.example.shashoapp.features.Bienvenida.presentation.ui.BienvenidaScreen
    import com.example.shashoapp.features.Bienvenida.presentation.ui.BienvenidaScreenRoot
    import com.example.shashoapp.features.auth.domain.usecase.RegisterUseCase
    import com.example.shashoapp.features.auth.presentation.login.LoginViewModel
    import com.example.shashoapp.features.auth.presentation.login.ui.LoginScreenRoot
    import com.example.shashoapp.features.auth.presentation.register.RegisterViewModel
    import com.example.shashoapp.features.auth.presentation.register.ui.RegisterScreenRoot
    import com.example.shashoapp.features.listaListas.presentation.ListasViewModel
    import com.example.shashoapp.features.listaProductos.presentation.ProductosViewModel
    import com.example.shashoapp.features.listaProductos.presentation.ui.ProductosScreenRoot
    import com.example.shashoapp.features.listaListas.presentation.ui.ListasScreenRoot
    import com.example.shashoapp.core.EstadoConexion
    import com.example.shashoapp.core.SesionUsuario
    import com.example.shashoapp.features.Ajustes.presentation.AjustesAction
    import com.example.shashoapp.features.Ajustes.presentation.AjustesViewModel
    import com.example.shashoapp.features.Ajustes.presentation.ui.AjustesScreenRoot
    import com.example.shashoapp.features.Reparto.presentation.RepartoViewModel
    import com.example.shashoapp.features.Reparto.presentation.ui.RepartoScreenRoot
    import com.example.shashoapp.features.TirasyBarra.presentation.MainScafold
    import com.example.shashoapp.features.listaListas.domain.usecase.CrearListaUseCase
    import com.example.shashoapp.features.listaListas.presentation.ListasAction
    import kotlinx.coroutines.launch

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun App(
        appContainer: AppContainer = DefaultAppContainer(),
        toScreen: Route? = null
        ) {

        val idiomaActual by AppPreferences.idioma.collectAsStateWithLifecycle()
        val navController = rememberNavController()
        var conexionOk by remember { mutableStateOf<Boolean?>(null)}

        LaunchedEffect(toScreen) {
            AppPreferences.cargarPreferencias()
            val idGuardado = AppPreferences.idUsuario.value

            if(idGuardado != null && idGuardado > 0){
                SesionUsuario.setIdUsuario(idGuardado)
            }

            val hayConexion = ApiServidor.ping()
            conexionOk = hayConexion
            EstadoConexion.setConexion(hayConexion)

            if(toScreen != null){
                val usuarioActivo = SesionUsuario.getIdUsuario()
                if(usuarioActivo == null && hayConexion){
                    navController.navigate(Route.Login){
                        popUpTo(Route.Bienvenida) { inclusive = true }
                    }
                }else{
                    navController.navigate(toScreen) { launchSingleTop = true }
                }
                return@LaunchedEffect
            }

            if(hayConexion){
                if(idGuardado != null && idGuardado > 0){
                    navController.navigate(Route.Home){
                        popUpTo(Route.Bienvenida) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }else{
                val idOffline = idGuardado ?: -1
                SesionUsuario.setIdUsuario(idOffline)

                navController.navigate(Route.Home){
                    popUpTo(Route.Bienvenida){inclusive = true}
                    launchSingleTop = true
                }
            }
        }

        MaterialTheme{

            NavHost(navController = navController, startDestination = Route.Bienvenida) {

                composable<Route.Bienvenida>{
                    BienvenidaScreenRoot(
                        onLoginClick = {navController.navigate(Route.Login)},
                        onRegisterClick =  {navController.navigate(Route.Register)},
                        conexionOk = conexionOk
                    )
                }

                composable<Route.Home> {
                    val idUsuario = SesionUsuario.getIdUsuario() ?: -1 //en caso de estar offline el id de usuario será -1

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
                        onCrear = {viewModel.onAction(ListasAction.AbrirCrearLista)},
                        onAjustes = {navController.navigate(Route.Ajustes)}
                    ) {
                            inner->
                        ListasScreenRoot(
                            viewModel = viewModel,
                            onListaClick = {
                                lista -> navController.navigate(Route.Lista(id = lista.id, nombre = lista.nombre))
                            },
                            modifier = Modifier.padding(inner)
                        )
                    }

                }

                composable<Route.Lista> {
                        backStackEntry ->
                    val route: Route.Lista = backStackEntry.toRoute()

                    val viewModel = remember(route.id){
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
                        onInicio = { navController.popBackStack(Route.Home, false)},
                        onCrear = {},
                        onAjustes = {navController.navigate(Route.Ajustes)}
                    ) {
                        inner ->
                        ProductosScreenRoot(
                            viewModel = viewModel,
                            onVolver = { navController.popBackStack()}, //asi solo retrocede una pantalla de la pila },
                            onHome = { navController.popBackStack(Route.Home, inclusive = false)}, //asi vuelve a home y cierra todas las demás pantallas menos ella
                            modifier = Modifier.padding(inner),
                            onReparto = {navController.navigate(Route.Reparto(route.id))} ,
                            listaNombre = route.nombre
                        )
                    }

                }

                composable<Route.Login> {
                    val viewModel = LoginViewModel(appContainer.loginUseCase)
                    LoginScreenRoot(
                        viewModel = viewModel,
                        onNavigateToHome = {
                            navController.navigate(Route.Home) { popUpTo(Route.Bienvenida) {inclusive = true}
                            }
                        },

                        onNavigateToRegister = {
                            navController.navigate(Route.Register)
                        }
                    )
                }
                composable<Route.Register> {
                    val viewModel = RegisterViewModel(appContainer.registerUseCase)
                    RegisterScreenRoot(
                        viewModel = viewModel,
                        onNavigateToHome = {
                            navController.navigate(Route.Home) {
                                popUpTo(Route.Bienvenida) {inclusive = true}
                            }
                        },
                        onNavigateToLogin = {
                            navController.navigate(Route.Login)
                        }
                    )
                }

                composable<Route.Reparto> {
                    backStackEntry ->
                    val route: Route.Reparto = backStackEntry.toRoute()

                    val viewModel = remember(route.listaId) {
                        RepartoViewModel(
                            getRepartoUseCase = appContainer.getRepartoUSeCase,
                            listaId = route.listaId
                        )
                    }

                    MainScafold(
                        onInicio = {navController.popBackStack(Route.Home, false)},
                        onCrear = {},
                        onAjustes = {navController.navigate(Route.Ajustes)}
                    ) {
                        inner ->
                        RepartoScreenRoot(
                            viewModel = viewModel,
                            onVolver = {navController.popBackStack()},
                            onHome = {navController.popBackStack(Route.Home, false)},
                            modifier = Modifier.padding(inner)
                        )
                    }

                }

                composable<Route.Ajustes> {
                    val viewModel = remember { AjustesViewModel() }

                    MainScafold(
                        onInicio = {navController.popBackStack(Route.Home, false)},
                        onCrear = {},
                        onAjustes = {navController.navigate(Route.Ajustes)}
                    ){ inner ->
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



