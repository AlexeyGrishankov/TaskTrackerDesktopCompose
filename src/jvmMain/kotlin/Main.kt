import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ru.grishankov.task_tracker.app.di.initDi
import ru.grishankov.task_tracker.app.icons.AppIcons
import ru.grishankov.task_tracker.app.icons.painter
import ru.grishankov.task_tracker.presentation.app.App

fun main() = application {
    initDi(this)
    Window(
        onCloseRequest = this::exitApplication,
        title = "TaskTracker",
        icon = AppIcons.Logo.painter(),
        resizable = true,
        content = { App() },
    )
}