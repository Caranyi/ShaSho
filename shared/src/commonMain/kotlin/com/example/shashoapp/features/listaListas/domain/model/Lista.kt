package com.example.shashoapp.features.listaListas.domain.model

data class Lista(
    val id: Int,
    val nombre: String,
    val id_creador: Int,
    val ids_usuarios: IntArray = intArrayOf(),
    val cantidadProductos: Int,
    val compartida: Boolean = false,
    val tipo: String = "productos",
    val imagenBytes: ByteArray? = null
) {
    val dameNumUsuarios: Int
        get() = ids_usuarios.size

    val dameCantidadProductosLista: Int
        get() = cantidadProductos

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Lista

        if (id != other.id) return false
        if (id_creador != other.id_creador) return false
        if (cantidadProductos != other.cantidadProductos) return false
        if (compartida != other.compartida) return false
        if (nombre != other.nombre) return false
        if (tipo != other.tipo) return false
        if (!ids_usuarios.contentEquals(other.ids_usuarios)) return false
        if (imagenBytes != null) {
            if (other.imagenBytes == null) return false
            if (!imagenBytes.contentEquals(other.imagenBytes)) return false
        } else if (other.imagenBytes != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + id_creador
        result = 31 * result + cantidadProductos
        result = 31 * result + compartida.hashCode()
        result = 31 * result + nombre.hashCode()
        result = 31 * result + tipo.hashCode()
        result = 31 * result + ids_usuarios.contentHashCode()
        result = 31 * result + (imagenBytes?.contentHashCode() ?: 0)
        return result
    }
}