package ru.grishankov.task_tracker.app.vm

import kotlinx.coroutines.flow.*

abstract class BaseStore<STATE : Any, ACTION : Any>(
    state: STATE,
) : KStore() {

    abstract fun dispatch(action: ACTION)

    private val _stateFlow = MutableStateFlow(state)
    val stateFlow = _stateFlow.asStateFlow()

    val state: STATE
        get() = stateFlow.value

    fun updateState(state: STATE) {
        _stateFlow.value = state
    }
}