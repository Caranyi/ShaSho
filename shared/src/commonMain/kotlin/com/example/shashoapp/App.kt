    package com.example.shashoapp

    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.itemsIndexed
    import androidx.compose.foundation.rememberScrollState
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.foundation.verticalScroll
    import androidx.compose.material3.Button
    import androidx.compose.material3.ButtonDefaults
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateListOf
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import org.jetbrains.compose.resources.painterResource
    import org.jetbrains.compose.resources.DrawableResource
    import shashoapp.shared.generated.resources.Res
    import shashoapp.shared.generated.resources.arrowLeft
    import shashoapp.shared.generated.resources.arrowRight
    import shashoapp.shared.generated.resources.filtro
    import shashoapp.shared.generated.resources.logoShaSho
    import shashoapp.shared.generated.resources.reparto
    import shashoapp.shared.generated.resources.home
    import shashoapp.shared.generated.resources.addCircle
    import shashoapp.shared.generated.resources.settings
    import androidx.compose.material3.AlertDialog
    import com.example.shashoapp.model.Producto
    import com.example.shashoapp.ui.AnnyadirCatalogue
    import com.example.shashoapp.ui.ProductoItem
    import androidx.compose.foundation.lazy.items


    @Composable
    @Preview
    fun App() {
        MaterialTheme {

            /*
            MaterialTheme {
    val navController = rememberNavController()

    LaunchedEffect(toScreen) {
        toScreen?.let {
            navController.navigate(it) { launchSingleTop = true }
        }
    }

    NavHost(navController, startDestination = Route.Home) {
        composable<Route.Home> {
            HomeScreen(
                onNavigateToDetail = { message ->
                    navController.navigate(Route.Detail(message))
                }
            )
        }
        composable<Route.Detail> { backStackEntry ->
            val detail = backStackEntry.toRoute<Route.Detail>()
            DetailScreen(
                message = detail.message,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
             */


            var showCreateMenu by remember {mutableStateOf(false)}
            val productos = remember {
                mutableStateListOf<Producto>()
            }

            val productosDisp = remember {
                listOf(
                    Producto(1, "Leche Entera", 1.10),
                    Producto(2, "Pan de Molde", 1.45),
                    Producto(3, "Huevos Doce", 2.30)
                )
            }

            // COLUMNA PRINCIPAL DE LA PÁGINA
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
            ) {
                // PRIMERA SECCION: CABECERA MORADA CON LOGO
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                        .background(Color(0xFF673AB7)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.logoShaSho),
                        contentDescription = "Logo ShaSho",
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }

                // SEGUNDA SECCION: TITULO DE LA LISTA
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /*ir a la lista anterior*/ }) {
                        Icon(
                            painter = painterResource(Res.drawable.arrowLeft),
                            contentDescription = "Anterior",
                            tint = Color.Black,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Text(
                        text = "PISO",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    IconButton(onClick = { /*ir a la lista siguiente*/ }) {
                        Icon(
                            painter = painterResource(Res.drawable.arrowRight),
                            contentDescription = "Siguiente",
                            tint = Color.Black,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                // TERCERA SECCION : IDENTIFICADOR DE LISTA, BOTÓN DE FILTRADO Y BOTÓN DE REPARTO
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5))
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LISTA",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Row( //esta row es para separar los dos botones en la parte derecha de la row principal
                        horizontalArrangement = Arrangement.spacedBy(8.dp), //espacio entre los dos botones
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        IconButton(onClick = {/* ir a la sección de repartir pagos*/ }) {
                            Icon(
                                painter = painterResource(Res.drawable.reparto),
                                contentDescription = "Repartir pagos lista",
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(onClick = {/* ir a la sección de filtrado*/ }) {
                            Icon(
                                painter = painterResource(Res.drawable.filtro),
                                contentDescription = "Filtrar productos lista",
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // CUARTA SECCION: LISTA DE PRODUCTOS
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color(0xFFF5F5F5))
                ) {

                        if(productos.isEmpty()){
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                text = "    La lista está vacía. \n Añade un producto con +",
                                color = Color.Gray,
                                fontSize = 16.sp
                                )
                            }
                        }

                        else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                itemsIndexed(
                                    items = productos,
                                    key = {_, prod -> prod.id}

                                ){ i, producto ->
                                    ProductoItem(
                                        producto = producto,
                                        showCheckBox = true,
                                        ensennyaCantidad = true,

                                        onCardClick = {
                                            productos[i] = producto.copy(isChecked = !producto.isChecked)
                                        },
                                        onCheckedChange = { nuevoEstado ->
                                            productos[i] = producto.copy(isChecked = nuevoEstado)
                                        },

                                        incrementaCantidad = {
                                            val act = productos[i]
                                            productos[i] = act.copy(cantidad = act.dameCantidad + 1)
                                        },

                                        decrementarCantidad = {
                                            val act = productos[i]
                                            if (productos[i].dameCantidad > 1) {
                                                productos[i] = act.copy(cantidad = act.dameCantidad - 1 )
                                            } else {
                                                productos.remove(act)
                                            }
                                        }


                                    )

                                }
                                item {
                                    Spacer(modifier = Modifier.height(72.dp))
                                }
                            }
                    }

                    // BOTON AÑADIR PRODUCTO A LA LISTA
                    Button(
                        onClick = { showCreateMenu = true },
                            /*val nuevoNumero = productos.size + 1
                            productos.add(
                                Producto(
                                    id = nuevoNumero,
                                    nombre = "Producto  $nuevoNumero",
                                    precio = "Precio x€"
                                )
                            )*/

                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .size(56.dp)
                    ) {
                        Text(
                            text = "+",
                            fontSize = 28.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // BARRA DE NAVEGADOR (CON INICIO, CREAR y AJUSTES)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(74.dp)
                        .background(Color(0xFF673AB7))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NavItem(
                            icon = Res.drawable.home,
                            label = "Inicio",
                            onClick = { /* Acción ir al inicio */ }
                        )
                        NavItem(
                            icon = Res.drawable.addCircle,
                            label = "Crear",
                            onClick = { /* Acción crear lista */ }
                        )
                        NavItem(
                            icon = Res.drawable.settings,
                            label = "Ajustes",
                            onClick = { /* Acción ajustes */ }
                        )
                    }
                }
            }

            if(showCreateMenu){
                AnnyadirCatalogue(
                    catalogo = productosDisp,
                    productosConfirmados = { listaNuevos ->
                        listaNuevos.forEach{ nuevoProd ->
                            val i = productos.indexOfFirst { it.id == nuevoProd.id }
                            if(i != -1) {
                                val existente = productos[i]
                                productos[i] = existente.copy(cantidad = existente.dameCantidad + nuevoProd.dameCantidad)
                            }

                            else{
                                productos.add(nuevoProd.copy(isChecked = false))
                            }

                        }

                        showCreateMenu = false
                    },

                    onDismissRequest = {
                        showCreateMenu = false
                    }
                )
            }
        }



    }
    // LA FUNCIÓN PARA CREAR LOS BOTONES EN EL NAVEGADOR DE ABAJO
    @Composable
    fun NavItem(
        icon: DrawableResource,
        label: String,
        onClick: () -> Unit
    ) {
        Column(
            modifier = Modifier.clickable(onClick = onClick),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = label,
                color = Color.White,
                fontSize = 11.sp
            )
        }
    }
