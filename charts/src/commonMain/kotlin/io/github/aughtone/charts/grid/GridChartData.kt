package io.github.aughtone.charts.grid

import io.github.aughtone.charts.line.LegendItemData

/**
 * Data for a chart drawn against an axis grid, such as [BarChart] or [LineChart].
 *
 * Implementations derive their ranges from the data they hold rather than taking them directly.
 */
interface GridChartData {
    /** Lowest x-axis value present in the data. */
    val minX: Long
    /** Highest x-axis value present in the data. */
    val maxX: Long
    /** Lowest y-axis value to plot. */
    val minY: Float
    /** Highest y-axis value to plot. */
    val maxY: Float
    /** One legend entry per distinct series in the data. */
    val legendData: List<LegendItemData>
}
