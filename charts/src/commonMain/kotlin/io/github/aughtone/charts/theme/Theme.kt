package io.github.aughtone.charts.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Composition local holding the [ChartColors] the charts draw with.
 *
 * Every color defaults to [Color.Unspecified], so provide a palette above any chart:
 *
 * ```
 * CompositionLocalProvider(LocalChartColors provides myChartColors) {
 *     BarChart(data = data)
 * }
 * ```
 */
val LocalChartColors = staticCompositionLocalOf {
    ChartColors(
        primary = Color.Unspecified,
        surface = Color.Unspecified,
        grid = Color.Unspecified,
        emptyGasBottle = Color.Unspecified,
        fullGasBottle = Color.Unspecified,
        overlayLine = Color.Unspecified,
    )
}

internal object ChartTheme {
    val colors: ChartColors
        @Composable
        get() = LocalChartColors.current
}
