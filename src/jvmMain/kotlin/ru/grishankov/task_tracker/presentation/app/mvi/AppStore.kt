package ru.grishankov.task_tracker.presentation.app.mvi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import ru.grishankov.task_tracker.app.vm.BaseStore
import ru.grishankov.task_tracker.app.vm.intent
import ru.grishankov.task_tracker.app.vm.launch
import ru.grishankov.task_tracker.app.vm.reduce
import ru.grishankov.task_tracker.domain.mappers.updateTime
import ru.grishankov.task_tracker.presentation.app.models.Task

data class AppState(
    val isLoading: Boolean = false,
    val isRunning: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val totalDuration: String = "",
)

sealed interface AppAction {
    object StartApp : AppAction
    object CreateTask : AppAction
    class StartTask(val id: Int) : AppAction
    class StopTask(val id: Int) : AppAction
    class RenameTask(val id: Int, val label: String) : AppAction
    class DeleteTask(val id: Int) : AppAction
    object SaveAll : AppAction
}

class AppStore : BaseStore<AppState, AppAction>(
    state = AppState()
), AppInteractor by AppInteractorImpl() {

    override fun dispatch(action: AppAction) {
        when (action) {
            AppAction.StartApp -> intent {
                reduce { state.copy(isLoading = true) }

                launch {
                    val newState = loadTasks(state)
                    reduce { newState }
                }

                launch(context = Dispatchers.IO) {
                    runTaskManager(state) { (tasks, counter) ->
                        reduce { state.copy(tasks = tasks) }
                        if (counter % 60 == 0L) {
                            updateTask(state)
                            reduce { state.copy(totalDuration = tasks.sumOf { it.duration }.updateTime(false)) }
                        }
                    }
                }
            }

            is AppAction.CreateTask -> intent {
                launch {
                    val newState = createTask(state)
                    reduce { newState }
                }
            }

            is AppAction.StartTask -> intent {
                launch {
                    val newState = runTask(state, action.id)
                    reduce { newState }
                }
            }

            is AppAction.DeleteTask -> intent {
                launch {
                    val newState = deleteTask(state, action.id)
                    reduce { newState }
                }
            }

            is AppAction.RenameTask -> intent {
                launch {
                    val newState = renameTask(state, action.id, action.label)
                    reduce { newState }
                }
            }

            is AppAction.StopTask -> intent {
                launch {
                    val newState = stopTask(state, action.id)
                    reduce { newState }
                }
            }

            AppAction.SaveAll -> intent {
                launch {
                    updateTask(state)
                }
            }
        }
    }

    override fun onCleared() {
        runBlocking(Dispatchers.IO) {
            updateTask(state)
        }
        super.onCleared()
    }
}