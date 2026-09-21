package io.github.aughtone.charts.line

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import io.github.aughtone.charts.theme.ChartColors

@Immutable
/**
 * The colors [LineChart] uses. Line colors come from the data, in [LineChartSeries.lineColor].
 *
 * @param grid Color of the axis grid lines.
 * @param surface Background of the overlay shown on touch or click.
 * @param overlayLine Color of the vertical line marking the selected x-axis value.
 */
data class LineChartColors(
    val grid: Color,
    val surface: Color,
    val overlayLine: Color,
)

/** Narrows a full [ChartColors] palette to the subset [LineChart] uses. */
val ChartColors.lineChartColors
    get() = LineChartColors(
        grid = grid,
        surface = surface,
        overlayLine = overlayLine,
    )
