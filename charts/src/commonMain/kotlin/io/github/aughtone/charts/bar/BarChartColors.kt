package io.github.aughtone.charts.bar

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import io.github.aughtone.charts.theme.ChartColors

@Immutable
/**
 * The colors [BarChart] uses. Bar colors come from the data, in [BarChartEntry.color].
 *
 * @param grid Color of the axis grid lines.
 * @param surface Background of the overlay shown on touch or click.
 */
data class BarChartColors(
    val grid: Color,
    val surface: Color,
)

/** Narrows a full [ChartColors] palette to the subset [BarChart] uses. */
val ChartColors.barChartColors
    get() = BarChartColors(
        grid = grid,
        surface = surface,
    )
