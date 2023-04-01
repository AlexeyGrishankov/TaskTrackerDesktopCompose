package ru.grishankov.task_tracker.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.grishankov.task_tracker.data.DataBaseObject

@Serializable
data class TaskDto(
    @SerialName("id") override val id: Int,
    @SerialName("label") val label: String,
    @SerialName("duration") val duration: Long,
) : DataBaseObject