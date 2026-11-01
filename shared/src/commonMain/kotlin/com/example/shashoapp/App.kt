    package com.example.shashoapp

    import androidx.compose.material3.MaterialTheme
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.remember
    import androidx.navigation.compose.NavHost
    import androidx.navigation.compose.composable
    import androidx.navigation.compose.rememberNavController
    import androidx.navigation.toRoute
    import com.example.shashoapp.di.AppContainer
    import com.example.shashoapp.di.DefaultAppContainer
    import com.example.shashoapp.features.listaListas.presentation.ListasViewModel
    import com.example.shashoapp.features.listaProductos.presentation.ProductosViewModel
    import com.example.shashoapp.features.listaProductos.presentation.ui.ProductosScreenRoot
    import com.example.shashoapp.features.listalistas.presentation.ui.ListasScreenRoot

    @Composable
    fun App(appContainer: AppContainer = DefaultAppContainer()) {
        MaterialTheme{
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Route.Home) {
                composable<Route.Home> {
                    val viewModel = remember {
                        ListasViewModel(appContainer.getListasUseCase)
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
                composable<Route.Login> {/**/}
                composable<Route.Register> {/**/}
                composable<Route.Settings> {/**/}
                composable<Route.Reparto> {/**/}
            }
        }

    }



