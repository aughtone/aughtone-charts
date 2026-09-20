package io.github.aughtone.charts.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
/**
 * The palette every chart draws from, supplied through [LocalChartColors].
 *
 * Individual charts narrow this to the subset they use, for example [ChartColors.barChartColors].
 *
 * @param primary Color of the drawn data itself, where a chart does not take its colors from data.
 * @param surface Background of the overlay shown on touch or click.
 * @param grid Color of the axis grid lines.
 * @param emptyGasBottle Color representing an empty cylinder in the gas bottle chart.
 * @param fullGasBottle Color representing a full cylinder in the gas bottle chart.
 * @param overlayLine Color of the vertical line marking the selected x-axis value.
 */
data class ChartColors constructor(
    val primary: Color,
    val surface: Color,
    val grid: Color,
    val emptyGasBottle: Color,
    val fullGasBottle: Color,
    val overlayLine: Color,
)
