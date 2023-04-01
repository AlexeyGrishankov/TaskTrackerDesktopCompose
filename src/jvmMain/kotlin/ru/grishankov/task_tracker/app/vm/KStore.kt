package ru.grishankov.task_tracker.app.vm

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class KStore {

    private val coroutineTags = hashMapOf<String, CoroutineScope>()
    private val mainCoroutineContext = (SupervisorJob() + Dispatchers.Main.immediate)

    val storeScope: CoroutineScope
        get() = coroutineTags[MAIN_JOB_KEY] ?: launchNewScope()

    protected open fun onCleared() {}

    fun clear() {
        coroutineTags.forEach { it.value.cancel() }
        onCleared()
    }

    private fun launchNewScope(
        key: String = MAIN_JOB_KEY,
        coroutineContext: CoroutineContext = mainCoroutineContext
    ): CoroutineScope =
        coroutineTags.getOrPut(key) {
            CoroutineScope(coroutineContext)
        }

    companion object {
        private const val MAIN_JOB_KEY = "main.store.coroutine.job"
    }
}