package ru.grishankov.task_tracker.domain.mappers

import androidx.compose.ui.text.buildAnnotatedString
import ru.grishankov.task_tracker.data.dto.TaskDto
import ru.grishankov.task_tracker.presentation.app.models.Task
import kotlin.time.Duration.Companion.seconds

fun TaskDto.toTask(): Task {
    return Task(id, label, duration, duration.updateTime(), false)
}

fun Task.toTaskDto() = TaskDto(id, label, duration)

fun Task.updateTime(): Task {
    return Task(id, label, duration, duration.updateTime(), false)
}

fun Long.updateTime(isShowSeconds: Boolean = true) = seconds.toComponents { days, hours, minutes, seconds, _ ->
    buildAnnotatedString {
        if (days > 0) append("$days d ")
        append("$hours h ")
        append("$minutes m ")
        if (isShowSeconds) {
            append("$seconds s")
        }
    }
}.toString()