package ru.grishankov.task_tracker.domain.repo

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import ru.grishankov.task_tracker.domain.mappers.updateTime
import ru.grishankov.task_tracker.presentation.app.models.Task
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TaskManager {

    private var isRunning = false
    private var tasks = emptyList<Task>()
    private var counter = 0L

    data class TaskUpdater(
        val tasks: List<Task>,
        val counter: Long,
    )

    suspend fun startManager(updater: suspend (updater: TaskUpdater) -> Unit) {
        isRunning = true
        start(1.seconds)
            .onEach(updater)
            .collect()
    }

    private fun start(period: Duration, initialDelay: Duration = Duration.ZERO) = flow {
        delay(initialDelay)
        while (true) {
            if (!isRunning) {
                break
            }
            delay(period)
            counter++
            emit(
                TaskUpdater(sortTasks(), counter)
            )
        }
    }

    private suspend fun sortTasks(): List<Task> {
        return tasks.map {
            if (it.isActive) {
                val duration = it.duration + 1
                it.copy(duration = duration, durationText = duration.updateTime())
            } else it.copy()
        }.also {
            updateTasks(it)
        }
    }

    suspend fun updateTasks(tasks: List<Task>) {
        this.tasks = tasks
    }

    suspend fun stopManager() {
        isRunning = false
    }

}