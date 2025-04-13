package pl.sebcel.bpg.ui.measurementlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.sebcel.bpg.BpgApplication
import pl.sebcel.bpg.R
import pl.sebcel.bpg.data.local.database.model.Measurement
import pl.sebcel.bpg.data.local.database.model.PainDescriptions
import pl.sebcel.bpg.ui.theme.BPGTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
private val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

private val daysOfWeek = BpgApplication.instance.resources.getStringArray(R.array.days_of_the_week)

private val emojis = arrayOf(R.drawable.smiling_face, R.drawable.neutral_face, R.drawable.sad_face, R.drawable.angry_face)
private val emojiDescriptions = arrayOf(R.string.pain_description_0, R.string.pain_description_1, R.string.pain_description_2, R.string.pain_description_3)

private val painDescriptions = PainDescriptions()

data class DropDownItem(
    val text: String
)

@Composable
fun MeasurementListTable(
    measurements: List<Measurement>,
    onDelete: (Measurement) -> Unit,
    ) {
    LazyColumn(modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)) {
        items(measurements) {
            measurement -> MeasurementCard(measurement, onDelete)
        }
    }
}

@Composable
fun MeasurementCard(measurement: Measurement, onDelete: (Measurement) -> Unit, modifier : Modifier = Modifier) {
    val openAlertDialog = remember { mutableStateOf(false) }

    val shape = RectangleShape

    var isContextMenuVisible by rememberSaveable { mutableStateOf(false)}
    var pressOffset by remember { mutableStateOf(DpOffset.Zero) }
    val interactionSource = remember { MutableInteractionSource() }
    var itemHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    val dropdownItems = listOf(
        DropDownItem(BpgApplication.instance.getString(R.string.delete_button_label))
    )
    val onItemClick = {
        onDelete(measurement)
        openAlertDialog.value = true
    }

    Card(
        modifier = modifier
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
            }
            .padding(horizontal = 16.dp, vertical = 4.dp)

    ){
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiary, shape)
                .padding(16.dp)
                .fillMaxWidth()
                .indication(interactionSource, LocalIndication.current)
                .pointerInput(true) {
                    detectTapGestures(
                        onLongPress = {
                            isContextMenuVisible = true
                            pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                        },
                        onPress = {
                            val press = PressInteraction.Press(it)
                            interactionSource.emit(press)
                            tryAwaitRelease()
                            interactionSource.emit(PressInteraction.Release(press))
                        }
                    )
                }
        ){
            MeasurementRow(measurement)
        }
        DropdownMenu(
            expanded = isContextMenuVisible,
            onDismissRequest = {
                isContextMenuVisible = false
            },
            offset = pressOffset.copy(
                y = pressOffset.y - itemHeight
            )
        ) {
            dropdownItems.forEach {
                DropdownMenuItem(
                    text = {
                        Text(it.text)
                    },
                    onClick = {
                        onItemClick()
                        isContextMenuVisible = false
                    }
                )
            }
        }
    }

    when {
        openAlertDialog.value -> {
            AlertDialogExample(
                onConfirmation = {
                    openAlertDialog.value = false
                },
                onDismissRequest = {
                    openAlertDialog.value = false
                },
                dialogTitle = BpgApplication.instance.getString(R.string.delete_confirmation_title),
                dialogText = BpgApplication.instance.getString(R.string.delete_confirmation_text),
                icon = Icons.Default.Info
            )
        }
    }
}

@Composable
fun MeasurementRow(measurement: Measurement) {
    Column {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                DateElement(measurement)
                TimeElement(measurement)
            }
            Spacer(modifier = Modifier.weight(1.0f))
            ValueElement(measurement, Modifier.fillMaxHeight())
            Spacer(modifier = Modifier.width(10.dp))
            EmojiElement(measurement, Modifier.fillMaxHeight())
        }
        if (measurement.comment.isNotEmpty()) {
            Row(modifier = Modifier.padding(all = 0.dp)) {
                Text(
                    measurement.comment,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontSize = 10.sp,
                    lineHeight = 10.sp
                )
            }
        }
    }
}

@Composable
fun EmojiElement(measurement: Measurement, modifier: Modifier = Modifier) {
    val painValue = measurement.pain
    val emoji = emojis[painValue]
    val emojiDescription = emojiDescriptions[painValue]

    Image(
        painter = painterResource(emoji),
        contentDescription = stringResource(emojiDescription),
        Modifier.size(48.dp)
    )
}

@Composable
fun ValueElement(measurement: Measurement, modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.Center, modifier = modifier) {
        Text(
            painDescriptions.getPainDescription(measurement.pain),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onTertiary,
        )
    }
}

@Composable
fun DateElement(measurement: Measurement) {
    val date = measurement.date
    val calendar = Calendar.getInstance()
    calendar.time = date
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
    Text(
        text = "${dateFormatter.format(date)}, ${daysOfWeek[dayOfWeek]}",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 16.sp
    )
}

@Composable
fun TimeElement(measurement: Measurement) {
    Text(
        text = timeFormatter.format(measurement.date),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onTertiary,
        fontSize = 16.sp
    )
}

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(BpgApplication.instance.getString(R.string.delete_confirmation_button_label))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(BpgApplication.instance.getString(R.string.cancel_button_label))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun MeasurementListTableRowPreview() {
    BPGTheme {
        MeasurementListTable(
            listOf(
                Measurement(date = Date(), pain = 1, comment = "Sample comment 1", weatherDescription = "Sunny", periodState = "normal", location = "home"),
                Measurement(date = Date(), pain = 2, comment = "Sample comment 2", weatherDescription = "Rain", periodState = "normal", location = "home"),
                Measurement(date = Date(), pain = 0, comment = "Sample comment 3", weatherDescription = "Fog", periodState = "bleeding", location = "school")
            ),
            onDelete = TODO(),
        )
    }
}