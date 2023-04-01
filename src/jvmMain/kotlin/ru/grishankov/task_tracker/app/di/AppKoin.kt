package ru.grishankov.task_tracker.app.di

import androidx.compose.ui.window.ApplicationScope
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.grishankov.task_tracker.data.FileSystemService
import ru.grishankov.task_tracker.domain.repo.TaskManager
import ru.grishankov.task_tracker.domain.repo.TaskRepo

fun initDi(appScope: ApplicationScope) {
    startKoin {
        applicationContext(appScope)
        modules(appModules)
    }
}

private fun applicationContext(appScope: ApplicationScope) = module {
    single { appScope }
}

val appModules = module {

    single { TaskRepo() }
    single { TaskManager() }
    single { FileSystemService() }

}