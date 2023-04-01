package ru.grishankov.task_tracker.presentation.app.mvi

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.grishankov.task_tracker.domain.mappers.toTask
import ru.grishankov.task_tracker.domain.mappers.toTaskDto
import ru.grishankov.task_tracker.domain.mappers.updateTime
import ru.grishankov.task_tracker.domain.repo.TaskManager
import ru.grishankov.task_tracker.domain.repo.TaskRepo

interface AppInteractor {
    suspend fun loadTasks(state: AppState): AppState
    suspend fun createTask(state: AppState): AppState
    suspend fun runTaskManager(state: AppState, updateTasks: suspend (TaskManager.TaskUpdater) -> Unit)

    suspend fun updateTask(state: AppState)

    suspend fun runTask(state: AppState, id: Int): AppState
    suspend fun stopTask(state: AppState, id: Int): AppState

    suspend fun renameTask(state: AppState, id: Int, label: String): AppState
    suspend fun deleteTask(state: AppState, id: Int): AppState

    suspend fun saveAll(state: AppState)
}

class AppInteractorImpl : AppInteractor, KoinComponent {

    private val taskRepo: TaskRepo by inject()
    private val taskManager: TaskManager by inject()

    override suspend fun loadTasks(state: AppState): AppState {
        val tasks = taskRepo.loadTasks().map { it.toTask() }
        return state.copy(
            tasks = tasks,
            isLoading = false,
            totalDuration = tasks.sumOf { it.duration }.updateTime(false)
        )
    }

    override suspend fun runTaskManager(state: AppState, updateTasks: suspend (TaskManager.TaskUpdater) -> Unit) {
        taskManager.updateTasks(state.tasks)
        taskManager.startManager(updateTasks)
    }

    override suspend fun updateTask(state: AppState) {
        val actualTasks = state.tasks.map { it.toTaskDto() }
        taskRepo.updateTask(actualTasks)
    }

    override suspend fun createTask(state: AppState): AppState {
        val actualTasks = taskRepo.createTask().map { it.toTask() }
        taskManager.updateTasks(actualTasks)
        return state.copy(
            tasks = actualTasks
        )
    }

    override suspend fun runTask(state: AppState, id: Int): AppState {
        val tasks = state.tasks.map {
            if (it.id == id) it.copy(isActive = true) else it.copy(isActive = false)
        }
        taskManager.updateTasks(tasks)
        return state.copy(
            isRunning = true,
            tasks = tasks,
        )
    }

    override suspend fun stopTask(state: AppState, id: Int): AppState {
        val updatedTasks = state.tasks.map {
            if (it.id == id) it.copy(isActive = false) else it.copy()
        }
        val tasks = taskRepo.updateTask(updatedTasks.map { it.toTaskDto() }).map { it.toTask() }
        taskManager.updateTasks(tasks)
        return state.copy(
            tasks = tasks,
        )
    }

    override suspend fun renameTask(state: AppState, id: Int, label: String): AppState {
        val tasks = taskRepo.renameTask(id, label).map { it.toTask() }
        taskManager.updateTasks(tasks)
        return state.copy(
            tasks = tasks,
        )
    }

    override suspend fun deleteTask(state: AppState, id: Int): AppState {
        val tasks = taskRepo.deleteTask(id).map { it.toTask() }
        taskManager.updateTasks(tasks)
        return state.copy(
            tasks = tasks,
        )
    }

    override suspend fun saveAll(state: AppState) {
        taskRepo.updateTask(state.tasks.map { it.toTaskDto() })
    }
}