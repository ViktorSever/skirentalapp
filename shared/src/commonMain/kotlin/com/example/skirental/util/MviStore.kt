package com.example.skirental.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class MviStore<Intent, State, Effect>(
    initialState: State
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<Effect>()
    val effects: SharedFlow<Effect> = _effects.asSharedFlow()

    protected fun setState(reducer: (State) -> State) {
        _state.update(reducer)
    }

    protected fun emitEffect(effect: Effect) {
        scope.launch {
            _effects.emit(effect)
        }
    }

    fun handleIntent(intent: Intent) {
        scope.launch {
            reduce(intent)
        }
    }

    protected abstract suspend fun reduce(intent: Intent)

    fun clear() {
        scope.cancel()
    }
}

