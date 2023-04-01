package ru.grishankov.task_tracker.presentation.app

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.grishankov.task_tracker.app.vm.ComposableStore
import ru.grishankov.task_tracker.app.vm.collectAsState
import ru.grishankov.task_tracker.app.vm.dispatch
import ru.grishankov.task_tracker.presentation.app.mvi.AppAction
import ru.grishankov.task_tracker.presentation.app.mvi.AppStore

@Composable
fun App() {
    ComposableStore(store = AppStore()) { store ->

        LaunchedEffect(Unit) {
            store.dispatch { AppAction.StartApp }
        }

        val state = store.collectAsState().value
        val focus = LocalFocusManager.current

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        focus.clearFocus()
                    }
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    modifier = Modifier
                        .clickable {
                            store.dispatch { AppAction.CreateTask }
                        },
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
                Text(
                    text = "Total: ${state.totalDuration}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            val stateHorizontal = rememberLazyListState()
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                LazyColumn(
                    modifier = Modifier.padding(end = 8.dp),
                    state = stateHorizontal,
                ) {
                    items(state.tasks.sortedBy { it.id }, key = { it.hashCode() }) {
                        TaskView(
                            label = it.label,
                            duration = it.durationText,
                            isActive = it.isActive,
                            onClickDelete = {
                                store.dispatch { AppAction.DeleteTask(it.id) }
                            },
                            onClickStart = {
                                store.dispatch { AppAction.StartTask(it.id) }
                            },
                            onClickStop = {
                                store.dispatch { AppAction.StopTask(it.id) }
                            },
                            onRename = { newLabel ->
                                store.dispatch { AppAction.RenameTask(it.id, newLabel) }
                            }
                        )
                        Divider()
                    }
                }
                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(
                        scrollState = stateHorizontal
                    )
                )
            }
        }
    }
}

