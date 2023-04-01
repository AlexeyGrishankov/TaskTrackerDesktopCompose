package ru.grishankov.task_tracker.app.vm

import kotlinx.coroutines.*
import ru.grishankov.task_tracker.app.dsl.TaskTrackerDsl
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@TaskTrackerDsl
suspend fun launch(
    ceh: CoroutineExceptionHandler? = null,
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend () -> Unit
) {
    coroutineScope {
        launch(context) {
            try {
                block.invoke()
            } catch (t: Throwable) {
                withContext(coroutineContext) {
                    ceh?.handleException(coroutineContext, t)
                }
            }
        }
    }
}

@TaskTrackerDsl
fun <ACTION : Any> BaseStore<*, ACTION>.dispatch(block: () -> ACTION) {
    dispatch(block())
}

@TaskTrackerDsl
fun <ACTION : Any> BaseStore<*, ACTION>.intent(block: suspend CoroutineScope.() -> Unit) {
    storeScope.launch(block = block)
}

@TaskTrackerDsl
suspend fun <ACTION : Any, STATE: Any> BaseStore<STATE, ACTION>.reduce(
    block: suspend CoroutineScope.() -> STATE
) {
    updateState(withContext(this.storeScope.coroutineContext, block))
}