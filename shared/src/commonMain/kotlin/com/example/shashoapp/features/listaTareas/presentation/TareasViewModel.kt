package com.example.shashoapp.features.listaTareas.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shashoapp.core.AppPreferences
import com.example.shashoapp.core.TextosApp
import com.example.shashoapp.features.listaListas.domain.usecase.CompartirListaUseCase
import com.example.shashoapp.features.listaTareas.domain.usecase.ActualizarTareaCompletadaUseCase
import com.example.shashoapp.features.listaTareas.domain.usecase.CrearTareaUseCase
import com.example.shashoapp.features.listaTareas.domain.usecase.EliminarTareaUseCase
import com.example.shashoapp.features.listaTareas.domain.usecase.GetTareasUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TareasViewModel(
    private val getTareasUseCase: GetTareasUseCase,
    private val compartirListaUseCase: CompartirListaUseCase,
    private val crearTareaUseCase: CrearTareaUseCase,
    private val actualizarTareaCompletadaUseCase: ActualizarTareaCompletadaUseCase,
    private val eliminarTareaUseCase: EliminarTareaUseCase,
    private val idLista: Int,
) : ViewModel() {

    private val _state = MutableStateFlow(TareasState())
    val state: StateFlow<TareasState> = _state.asStateFlow()

    init {
        cargarTareas()
    }

    fun onAction(action: TareasAction) {
        when (action) {
            TareasAction.AbrirCrear ->
                _state.update { it.copy(mostrarCrear = true, error = null) }

            TareasAction.AbrirCompartir ->
                _state.update { it.copy(mostrarCompartir = true, mensajeCompartir = null) }

            TareasAction.CerrarCompartir ->
                _state.update {
                    it.copy(
                        mostrarCompartir = false,
                        correoCompartir = "",
                        mensajeCompartir = null
                    )
                }

            is TareasAction.OnCorreoChange ->
                _state.update {
                    it.copy(correoCompartir = action.correo, mensajeCompartir = null)
                }

            TareasAction.ConfirmarCompartir ->
                confirmarCompartir()


            TareasAction.CerrarCrear ->
                _state.update { it.copy(mostrarCrear = false, error = null) }

            is TareasAction.GuardarTarea ->
                guardarTarea(action.nombre)

            is TareasAction.CambiarCompletada ->
                cambiarCompletada(action.idTarea)

            is TareasAction.EliminarTarea ->
                eliminarTarea(action.idTarea)
        }
    }

    private fun cargarTareas() {
        viewModelScope.launch {
            _state.update { it.copy(cargando = true, error = null) }

            try {
                val tareas = getTareasUseCase(idLista)
                _state.update { it.copy(tareas = tareas, cargando = false) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update {
                    it.copy(cargando = false, error = e.message)
                }
            }
        }
    }

    private fun guardarTarea(nombre: String) {
        viewModelScope.launch {
            try {
                crearTareaUseCase(idLista, nombre)
                val tareas = getTareasUseCase(idLista)

                _state.update {
                    it.copy(
                        tareas = tareas,
                        mostrarCrear = false,
                        error = null
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    private fun cambiarCompletada(idTarea: Int) {
        val tarea = _state.value.tareas
            .firstOrNull { it.id == idTarea } ?: return

        viewModelScope.launch {
            try {
                actualizarTareaCompletadaUseCase(
                    idLista = idLista,
                    idTarea = idTarea,
                    completada = !tarea.completada
                )
                val tareas = getTareasUseCase(idLista)
                _state.update { it.copy(tareas = tareas, error = null) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    private fun eliminarTarea(idTarea: Int) {
        viewModelScope.launch {
            try {
                eliminarTareaUseCase(idLista, idTarea)
                val tareas = getTareasUseCase(idLista)
                _state.update { it.copy(tareas = tareas, error = null) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    private fun confirmarCompartir() {
        val correo = _state.value.correoCompartir

        if (correo.isBlank()) {
            val textos = TextosApp.get(AppPreferences.idioma.value)
            _state.update {
                it.copy(mensajeCompartir = textos.errorIntroduceCorreo)
            }
            return
        }

        val correoLimpio = correo.trim().lowercase()

        viewModelScope.launch {
            compartirListaUseCase(idLista, correoLimpio)
                .onSuccess {
                    _state.update {
                        it.copy(
                            mostrarCompartir = false,
                            correoCompartir = "",
                            mensajeCompartir = null
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(mensajeCompartir = error.message)
                    }
                }
        }
    }
}