package ru.grishankov.task_tracker.app.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import ru.grishankov.task_tracker.app.icons.FormatIcon.PNG

enum class FormatIcon {
    PNG,
    SVG,
}

enum class AppIcons(val path: String, val format: FormatIcon) {

    Logo("logo", FormatIcon.SVG),
    Delete("delete", FormatIcon.SVG),
    Add("add", FormatIcon.SVG),
    Play("start", FormatIcon.SVG),
    Stop("stop", FormatIcon.SVG),

}

@Composable
fun AppIcons.painter(): Painter {
    val path = "assets/icons/$path.${format.name.lowercase()}"
    return painterResource(path)
}