    package com.example.shashoapp

    import androidx.compose.material3.MaterialTheme
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.remember
    import com.example.shashoapp.di.AppContainer
    import com.example.shashoapp.di.DefaultAppContainer
    import com.example.shashoapp.features.productos.presentation.ProductosViewModel
    import com.example.shashoapp.features.productos.presentation.ui.ProductosScreenRoot

    @Composable
    fun App(appContainer: AppContainer = DefaultAppContainer()) {
        MaterialTheme {
            val viewModel = remember {
                ProductosViewModel(appContainer.getProductosDisponiblesUseCase)
            }
            ProductosScreenRoot(viewModel = viewModel)
        }
    }



