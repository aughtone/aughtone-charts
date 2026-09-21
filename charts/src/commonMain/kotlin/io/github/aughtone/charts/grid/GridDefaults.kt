package io.github.aughtone.charts.grid

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Default labels and grid sizing shared by the axis-based charts.
 *
 * These are the values the chart composables fall back to, so a caller varying one label can
 * delegate to the default rather than reproducing it:
 *
 * ```
 * BarChart(
 *     data = data,
 *     yAxisLabel = { value -> GridDefaults.YAxisLabel(format(value)) },
 * )
 * ```
 */
object GridDefaults {

    val HORIZONTAL_LINES_OFFSET = 10.dp
    const val NUMBER_OF_GRID_LINES = 5
    const val ROUND_MIN_MAX_CLOSEST_TO = 10

    val YAxisLabel: @Composable (value: Any) -> Unit = { value ->
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            fontSize = 12.sp,
            text = value.toString(),
            textAlign = TextAlign.End,
            maxLines = 1
        )
    }

    val OverlayHeaderLabel: @Composable (value: Any) -> Unit = { value ->
        Text(
            text = value.toString(),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall
        )
    }

    val OverlayDataEntryLabel: @Composable (dataName: String, value: Any) -> Unit =
        { dataName, value ->
            Text(
                text = "$dataName: $value"
            )
        }

    val XAxisLabel: @Composable (value: Any) -> Unit = { value ->
        Text(
            fontSize = 12.sp,
            text = value.toString(),
            textAlign = TextAlign.Center
        )
    }

    val LegendItemLabel: @Composable (String) -> Unit = {
        Text(
            text = it,
        )
    }
}
