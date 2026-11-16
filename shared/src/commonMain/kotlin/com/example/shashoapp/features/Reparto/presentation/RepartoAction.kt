package com.example.shashoapp.features.Reparto.presentation

sealed interface RepartoAction {
    data object CargarReparto: RepartoAction
    data object GuardarReparto: RepartoAction
}