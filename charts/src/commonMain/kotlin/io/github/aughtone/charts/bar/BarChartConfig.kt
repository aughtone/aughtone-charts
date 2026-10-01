package io.github.aughtone.charts.bar

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import io.github.aughtone.charts.grid.GridDefaults

/**
 * The customization parameters for [BarChart]
 *
 * @param thickness Width of a single bar
 * @param cornerRadius 0 for square bars, thickness/2 for fully rounded corners
 * @param barsSpacing The space between bars in a cluster
 * @param maxHorizontalLinesCount Roughly how many horizontal grid lines to draw. Their spacing is
 * rounded to 1, 2 or 5 times a power of ten, so there can be a few more.
 * @param roundMinMaxClosestTo Multiple the y-axis bounds are rounded out to: the minimum down and
 * the maximum up, so the axis always contains the data.
 */
@Immutable
data class BarChartConfig(
    val thickness: Dp = BarChartDefaults.BAR_THICKNESS,
    val cornerRadius: Dp = BarChartDefaults.BAR_CORNER_RADIUS,
    val barsSpacing: Dp = BarChartDefaults.BAR_HORIZONTAL_SPACING,
    val maxHorizontalLinesCount: Int = GridDefaults.NUMBER_OF_GRID_LINES,
    val roundMinMaxClosestTo: Int = GridDefaults.ROUND_MIN_MAX_CLOSEST_TO,
)
