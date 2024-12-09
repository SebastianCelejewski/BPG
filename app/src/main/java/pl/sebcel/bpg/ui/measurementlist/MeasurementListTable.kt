package pl.sebcel.bpg.ui.measurementlist

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import pl.sebcel.bpg.data.local.database.Measurement
import pl.sebcel.bpg.data.local.database.PainDescriptions
import pl.sebcel.bpg.ui.theme.BPGTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
private val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

private val daysOfWeek =
    arrayOf("niedziela", "poniedziałek", "wtorek", "środa", "czwartek", "piątek", "sobota")

private val painDescriptions = PainDescriptions()

data class DropDownItem(
    val text: String
)

@Composable
fun MeasurementListTable(
    items: List<Measurement>,
    modifier: Modifier = Modifier,
    onDelete: (Measurement) -> Unit
) {
    val shape = RectangleShape
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        var previousDate : String? = null
        items.forEach {
            if (previousDate == null || dateFormatter.format(it.date) != previousDate) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                ){
                    val calendar = Calendar.getInstance()
                    calendar.time = it.date
                    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                    Text(
                        text = "${dateFormatter.format(it.date)}, ${daysOfWeek[dayOfWeek]}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                previousDate = dateFormatter.format(it.date)
            }
            var isContextMenuVisible by rememberSaveable { mutableStateOf(false)}
            var pressOffset by remember { mutableStateOf(DpOffset.Zero) }
            val interactionSource = remember { MutableInteractionSource() }
            var itemHeight by remember { mutableStateOf(0.dp) }
            val density = LocalDensity.current

            val dropdownItems = listOf(
                DropDownItem("Usuń")
            )
            val onItemClick = {
                onDelete(it)
            }

            Card(
                modifier = modifier
                .onSizeChanged {
                    itemHeight = with(density) { it.height.toDp() }
                }
            ){
                Row(
                    modifier = Modifier
                        .padding(4.dp)
                        .border(2.dp, MaterialTheme.colorScheme.secondary, shape)
                        .background(MaterialTheme.colorScheme.primary, shape)
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
                    Text(
                        text = timeFormatter.format(it.date),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text("\t")
                    Text(
                        painDescriptions.getPainDescription(it.pain),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
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
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MeasurementListTableRowPreview() {
    BPGTheme {
        MeasurementListTable(
            listOf(
                Measurement(date = Date(), pain = 1, comment = "Sample comment 1"),
                Measurement(date = Date(), pain = 2, comment = "Sample comment 2"),
                Measurement(date = Date(), pain = 0, comment = "Sample comment 3")
            ),
            onDelete = TODO(),
        )
    }
}