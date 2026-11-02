package com.example.shashoapp.features.listaListas.presentation

import com.example.shashoapp.features.listaListas.domain.model.Lista

data class ListasState(
    val listas: List<Lista> = emptyList(),
    val isLoading: Boolean = false
){
}