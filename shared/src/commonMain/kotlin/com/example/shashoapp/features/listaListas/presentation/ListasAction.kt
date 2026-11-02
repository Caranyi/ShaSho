package com.example.shashoapp.features.listaListas.presentation

import com.example.shashoapp.features.listaListas.domain.model.Lista

sealed interface ListasAction {
    data class OnListaClick(val lista: Lista) : ListasAction
    data object AbrirCrearLista : ListasAction
}