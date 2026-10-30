    package com.example.shashoapp

    import androidx.compose.material3.MaterialTheme
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.remember
    import androidx.navigation.compose.rememberNavController
    import com.example.shashoapp.di.AppContainer
    import com.example.shashoapp.di.DefaultAppContainer
    import com.example.shashoapp.features.productos.presentation.ProductosViewModel
    import com.example.shashoapp.features.productos.presentation.ui.ProductosScreenRoot

    @Composable
    fun App(appContainer: AppContainer = DefaultAppContainer()) {

        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = Route.Car)

        MaterialTheme {
            val viewModel = remember {
                ProductosViewModel(appContainer.getProductosDisponiblesUseCase)
            }
            ProductosScreenRoot(viewModel = viewModel)
        }
    }



