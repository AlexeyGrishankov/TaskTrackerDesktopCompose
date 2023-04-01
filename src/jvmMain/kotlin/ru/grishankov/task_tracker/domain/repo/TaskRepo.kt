package ru.grishankov.task_tracker.domain.repo

import org.koin.core.component.KoinComponent
import ru.grishankov.task_tracker.data.JsonDataBase
import ru.grishankov.task_tracker.data.dto.TaskDto

class TaskRepo : KoinComponent {

    suspend fun createTask(): List<TaskDto> {
        JsonDataBase.create {
            TaskDto(id = it + 1, label = "NEW TASK", duration = 0)
        }
        return loadTasks()
    }

    suspend fun updateTask(task: List<TaskDto>): List<TaskDto> {
        val old = JsonDataBase.loadAll<TaskDto>()
        val updated = old.mapNotNull { saved ->
            val find = task.find { it.id == saved.id } ?: return@mapNotNull null
            saved.copy(label = find.label, duration = find.duration)
        }
        JsonDataBase.saveAll(updated)
        return loadTasks()
    }

    suspend fun loadTasks(): List<TaskDto> {
        return JsonDataBase.loadAll()
    }

    suspend fun renameTask(id: Int, label: String): List<TaskDto> {
        JsonDataBase.update<TaskDto>(id) {
            it.copy(label = label)
        }
        return loadTasks()
    }

    suspend fun deleteTask(id: Int): List<TaskDto> {
        JsonDataBase.delete<TaskDto>(id)
        return loadTasks()
    }
}