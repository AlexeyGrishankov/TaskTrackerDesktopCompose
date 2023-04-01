package ru.grishankov.task_tracker.presentation.app.models

data class Task(
    val id: Int,
    val label: String,
    val duration: Long,
    val durationText: String,
    val isActive: Boolean,
)
