    package com.example.shashoapp

    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Scaffold
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.remember
    import androidx.compose.ui.Modifier
    import androidx.navigation.compose.NavHost
    import androidx.navigation.compose.composable
    import androidx.navigation.compose.rememberNavController
    import androidx.navigation.toRoute
    import com.example.shashoapp.di.AppContainer
    import com.example.shashoapp.di.DefaultAppContainer
    import com.example.shashoapp.features.Bienvenida.presentation.ui.BienvenidaScreenRoot
    import com.example.shashoapp.features.Reparto.presentation.RepartoViewModel
    import com.example.shashoapp.features.Reparto.presentation.ui.RepartoScreenRoot
    import com.example.shashoapp.features.TirasyBarra.presentation.MainScafold
    import com.example.shashoapp.features.TirasyBarra.presentation.Tira
    import com.example.shashoapp.features.auth.presentation.login.LoginViewModel
    import com.example.shashoapp.features.auth.presentation.login.ui.LoginScreenRoot
    import com.example.shashoapp.features.auth.presentation.register.RegisterViewModel
    import com.example.shashoapp.features.auth.presentation.register.ui.RegisterScreenRoot
    import com.example.shashoapp.features.listaListas.presentation.ListasViewModel
    import com.example.shashoapp.features.listaProductos.presentation.ProductosViewModel
    import com.example.shashoapp.features.listaProductos.presentation.ui.ProductosScreenRoot
    import com.example.shashoapp.features.listalistas.presentation.ui.ListasScreenRoot

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun App(appContainer: AppContainer = DefaultAppContainer()) {
        MaterialTheme {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Route.Home
            ) {

                composable<Route.Bienvenida> {
                    BienvenidaScreenRoot(
                        onLoginClick = { navController.navigate(Route.Login) },
                        onRegisterClick = { navController.navigate(Route.Bienvenida) }
                    )
                }

                composable<Route.Home> {
                    val viewModel = remember {
                        ListasViewModel(appContainer.getListasUseCase)
                    }

                    MainScafold(
                        onInicio = { navController.popBackStack(Route.Home, false) },
                        onCrear = {},
                        onAjustes = {}
                    ) {
                        inner->
                        ListasScreenRoot(
                            viewModel = viewModel,
                            onListaClick = { lista ->
                                navController.navigate(Route.Lista(id = lista.id))
                            },
                            modifier = Modifier.padding(inner)
                        )
                    }

                }

                composable<Route.Lista> { backStackEntry ->
                    val route: Route.Lista = backStackEntry.toRoute()

                    val viewModel = remember(route.id) {

                        ProductosViewModel(
                            listaId = route.id,
                            appContainer.getProductosDisponiblesUseCase,
                            getListaIdUseCase = appContainer.getListaIdUseCase
                        )
                    }
                    MainScafold(
                        onInicio = { navController.popBackStack(Route.Home, false) },
                        onCrear = {},
                        onAjustes = {}
                    ) {
                        inner ->
                        ProductosScreenRoot(
                            viewModel = viewModel,
                            onVolver = {
                                navController.popBackStack() //asi solo retrocede una pantalla de la pila
                            },
                            onHome = {
                                navController.popBackStack(
                                    Route.Home,
                                    inclusive = false
                                ) //asi vuelve a home y cierra todas las demás pantallas menos ella
                            },
                            onRepartir = {
                                navController.navigate(Route.Reparto(listaId = route.id))
                            },
                            modifier = Modifier.padding(inner)

                        )
                    }

                }

                composable<Route.Reparto> { backStackEntry ->
                    val route: Route.Reparto = backStackEntry.toRoute()

                    val viewModel = remember {
                        RepartoViewModel(appContainer.getRepartoUseCase, listaId = route.listaId)
                    }
                    MainScafold(
                        onInicio = { navController.popBackStack(Route.Home, false) },
                        onCrear = {},
                        onAjustes = {}
                    ) {
                        inner ->
                        RepartoScreenRoot(
                            viewModel = viewModel,
                            onVolver = {
                                navController.popBackStack()
                            },
                            onHome = {
                                navController.popBackStack(Route.Home, inclusive = false)
                            },

                            modifier = Modifier.padding(inner)
                        )
                    }
                }

                composable<Route.Login> {
                    val viewModel = LoginViewModel(appContainer.loginUseCase)
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
                    val viewModel = RegisterViewModel(appContainer.registerUseCase)
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

                composable<Route.Settings> {/**/ }

            }

        }
    }



