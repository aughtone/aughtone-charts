package io.github.aughtone.charts.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * The default palette, derived from the current `MaterialTheme`.
 *
 * Provide it once above your charts:
 *
 * ```
 * CompositionLocalProvider(LocalChartColors provides ChartDefaults.chartColors()) {
 *     BarChart(data = data)
 * }
 * ```
 */
object ChartDefaults {
    @Composable
    /**
     * Builds a [ChartColors] from the current `MaterialTheme`, overriding any of its values.
     *
     * @param primary Color of drawn data where a chart does not take its colors from data.
     * @param surface Background of the overlay shown on touch or click.
     * @param grid Color of the axis grid lines.
     * @param emptyGasBottle Color representing an empty cylinder in the gas bottle chart.
     * @param fullGasBottle Color representing a full cylinder in the gas bottle chart.
     * @param overlayLine Color of the vertical line marking the selected x-axis value.
     */
    fun chartColors(
        primary: Color = MaterialTheme.colorScheme.primary,
        surface: Color = Color.Unspecified,
        grid: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
        emptyGasBottle: Color = MaterialTheme.colorScheme.error,
        fullGasBottle: Color = MaterialTheme.colorScheme.primary,
        overlayLine: Color = MaterialTheme.colorScheme.error,
    ) = ChartColors(
        primary = primary,
        surface = surface,
        grid = grid,
        emptyGasBottle = emptyGasBottle,
        fullGasBottle = fullGasBottle,
        overlayLine = overlayLine,
    )
}
