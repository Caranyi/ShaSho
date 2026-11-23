package com.example.shashoapp.features.Ajustes.presentation

sealed interface AjustesAction {
    data object onCerrarSesionClick : AjustesAction
    data class onActivarNotificaciones(val activadas: Boolean) : AjustesAction
    data class onActivarModoOscuro(val activado: Boolean) : AjustesAction
    data object onToggleMenuIdioma : AjustesAction
    data object onCerrarMenuIdioma : AjustesAction
    data class onCambiarIdioma(val codigoIdioma: String) : AjustesAction
    data class onFotoPerfilSeleccionada(val bytes: ByteArray) : AjustesAction
    data object onAbrirDialogoNombre : AjustesAction
    data object onCerrarDialogoNombre : AjustesAction
    data class onNombreTemporalChanged(val texto: String) : AjustesAction
    data object onConfirmarCambioNombre : AjustesAction
}