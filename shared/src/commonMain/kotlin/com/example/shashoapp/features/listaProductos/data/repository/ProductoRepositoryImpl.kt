package com.example.shashoapp.features.listaProductos.data.repository import com.example.shashoapp.features.listaProductos.domain.model.Producto
import com.example.shashoapp.features.listaProductos.domain.repository.ProductoRepository

//imports para la conexion
import com.example.shashoapp.core.ApiServidor
import com.example.shashoapp.core.EstadoConexion
import com.example.shashoapp.features.listaProductos.data.model.ProductoDto
import com.example.shashoapp.features.listaProductos.data.model.toDomain
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

//imports para leer y guardar
import com.example.shashoapp.core.database.guardarArchivo
import com.example.shashoapp.core.database.leerArchivo

class ProductoRepositoryImpl : ProductoRepository {
    override suspend fun getProductosDisponibles(): List<Producto>{
        return listOf(
            Producto(1, "Leche entera", 1.10),
            Producto(2, "Pan de molde", 1.45),
            Producto(3, "Docena de huevos", 2.30)
            )
    }

    //se va a pedir asl servidor (modo online)
    override suspend fun getProductosDeLista(idLista: Int): List<Producto> {
        val archivo = "productos_$idLista.json"

        //ONLINE: actualiza el archivo con lo que tenga el servidor
        if (EstadoConexion.getConexion()) {
            try {
                val texto = ApiServidor.getProductos(idLista)
                guardarArchivo(archivo, texto)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
            }
        }

        //SIEMPRE: LEER ARCHIVO
        val texto = leerArchivo(archivo)
        if (texto == null) {
            return emptyList()
        }

        try {
            val productosDto = Json.decodeFromString<List<ProductoDto>>(texto)
            return productosDto.map { it.toDomain() }
        } catch (e: Exception) {
            return emptyList()
        }
    }
}