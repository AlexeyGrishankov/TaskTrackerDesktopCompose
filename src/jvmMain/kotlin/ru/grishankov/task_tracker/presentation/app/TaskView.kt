package ru.grishankov.task_tracker.presentation.app

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.grishankov.task_tracker.app.icons.AppIcons
import ru.grishankov.task_tracker.app.icons.painter


@Preview
@Composable
fun TaskViewPreview() {
    TaskView(
        label = "Msp-888",
        duration = "4h 13m 1s",
        isActive = true,
        onClickDelete = {},
        onClickStart = {},
        onClickStop = {},
        onRename = {}
    )
}

@Composable
fun TaskView(
    label: String,
    duration: String,
    isActive: Boolean,
    onClickStart: () -> Unit,
    onClickStop: () -> Unit,
    onClickDelete: () -> Unit,
    onRename: (String) -> Unit
) {

    var labelText by remember { mutableStateOf(label) }
    var isEdit by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth().height(50.dp).padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (!isEdit) {
            Text(
                modifier = Modifier
                    .clickable {
                        isEdit = true
                    }
                    .height(22.dp)
                    .padding(horizontal = 8.dp)
                    .padding(top = 2.dp),
                text = label,
                textAlign = TextAlign.Justify
            )
        } else {
            TextLabelField(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .height(22.dp)
                    .background(LightGray),
                label = labelText,
                onChangeLabel = { new: String ->
                    labelText = new
                },
                icon = if (labelText.isNotEmpty()) {
                    {
                        Icon(
                            modifier = Modifier.clickable { onRename(labelText); isEdit = false },
                            imageVector = Icons.Default.Done,
                            contentDescription = null
                        )
                    }
                } else null,
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(duration)
            Icon(
                modifier = Modifier.clickable {
                    if (isActive) {
                        onClickStop()
                    } else {
                        onClickStart()
                    }
                },
                painter = if (isActive) AppIcons.Stop.painter() else AppIcons.Play.painter(),
                contentDescription = null
            )

            Icon(
                modifier = Modifier.clickable(onClick = onClickDelete),
                painter = AppIcons.Delete.painter(),
                contentDescription = null
            )

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TextLabelField(
    modifier: Modifier = Modifier,
    label: String,
    onChangeLabel: (String) -> Unit,
    icon: @Composable (() -> Unit)?,
) {
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        modifier = modifier,
        value = label,
        onValueChange = {
            onChangeLabel(it.take(20))
        },
        maxLines = 1,
        decorationBox = { innerTextField ->
            TextFieldDefaults.TextFieldDecorationBox(
                value = label,
                trailingIcon = icon,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(horizontal = 8.dp),
            )
        }
    )
}