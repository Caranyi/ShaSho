    package com.example.shashoapp

    import androidx.compose.material3.MaterialTheme
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import com.example.shashoapp.core.ApiServidor
    import androidx.navigation.compose.NavHost
    import androidx.navigation.compose.composable
    import androidx.navigation.compose.rememberNavController
    import androidx.navigation.toRoute
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
    import com.example.shashoapp.features.listalistas.presentation.ui.ListasScreenRoot
    import com.example.shashoapp.core.EstadoConexion
    import com.example.shashoapp.core.SesionUsuario

    @Composable
    fun App(appContainer: AppContainer = DefaultAppContainer()) {
        MaterialTheme{
            val navController = rememberNavController()

            var conexionOk by remember { mutableStateOf<Boolean?>(null) }

            //Modo sin conexion (pasa a la pantalla de listas(home))
            LaunchedEffect(Unit) {
                val hayConexion = ApiServidor.ping()
                conexionOk = hayConexion
                EstadoConexion.setConexion(hayConexion) //para ver de donde cogemos los datos
                if (!hayConexion) {
                    navController.navigate(Route.Home) {
                        popUpTo(Route.Bienvenida) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            NavHost(navController = navController, startDestination = Route.Bienvenida) {

                composable<Route.Bienvenida>{
                    BienvenidaScreenRoot(
                        onLoginClick = {navController.navigate(Route.Login)},
                        onRegisterClick =  {navController.navigate(Route.Register)},
                        conexionOk = conexionOk
                    )
                }

                composable<Route.Home> {
                    val viewModel = remember {
                        ListasViewModel(
                            getListasUseCase = appContainer.getListasUseCase, idUsuario = SesionUsuario.getIdUsuario()
                        )

                    }

                    ListasScreenRoot(
                        viewModel = viewModel,
                        onListaClick = {
                            lista -> navController.navigate(Route.Lista(id = lista.id))
                        }
                    )
                }

                composable<Route.Lista> {
                        backStackEntry ->
                    val route: Route.Lista = backStackEntry.toRoute()

                    val viewModel = remember(route.id){
                        ProductosViewModel(appContainer.getProductosDisponiblesUseCase)
                    }

                    ProductosScreenRoot(
                        viewModel = viewModel,
                        onVolver = {
                            navController.popBackStack() //asi solo retrocede una pantalla de la pila
                        },

                        onHome = {
                            navController.popBackStack(Route.Home, inclusive = false) //asi vuelve a home y cierra todas las demás pantallas menos ella
                        }
                    )
                }
                composable<Route.Login> {
                    val viewModel = LoginViewModel(appContainer.loginUseCase)
                    LoginScreenRoot(
                        viewModel = viewModel,
                        onNavigateToHome = {
                            navController.navigate(Route.Home) {
                                popUpTo(Route.Bienvenida) {inclusive = true}
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
                composable<Route.Settings> {/**/}
                composable<Route.Reparto> {/**/}
            }
        }

    }



