package ru.grishankov.task_tracker.app.vm

import androidx.compose.runtime.*

@Composable
fun <T : BaseStore<STATE, ACTION>, STATE : Any, ACTION: Any> ComposableStore(
    store: T,
    content: @Composable (T) -> Unit
) {

    val store = remember { store }
    content(store)

    DisposableEffect(Unit) {
        onDispose(store::clear)
    }
}

@Composable
fun <STATE : Any> BaseStore<STATE, *>.collectAsState(): State<STATE> {
    val stateFlow = stateFlow
    val initialValue = stateFlow.value
    return stateFlow.collectAsState(initialValue)
}