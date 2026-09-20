package io.github.aughtone.charts.bar

import androidx.compose.runtime.Immutable
import io.github.aughtone.charts.grid.GridChartData
import io.github.aughtone.charts.line.LegendItemData
import io.github.aughtone.charts.line.SymbolShape

@Immutable
/**
 * Data for [BarChart]: a list of categories, each holding the bars drawn in one cluster.
 *
 * The axis ranges are derived from the categories and are not set directly. The y-axis range
 * always spans zero, so bars share a common baseline.
 *
 * @param categories The clusters to draw, in the order they appear on the x-axis.
 */
data class BarChartData(
    val categories: List<BarChartCategory>,
) : GridChartData {
    // TODO hide those values from the user
    override val minX: Long = 0
    override val maxX: Long
        get() = if (categories.isEmpty()) {
            1
        } else {
            categories.size * 2.toLong()
        }

    override val minY: Float
        get() = if (categories.isEmpty()) {
            0f
        } else {
            categories.minOf { it.minY }.coerceAtMost(0f)
        }

    override val maxY: Float
        get() = if (categories.isEmpty()) {
            1f
        } else {
            categories.maxOf { it.maxY }.coerceAtLeast(0f)
        }

    override val legendData: List<LegendItemData>
        get() {
            return categories
                .flatMap { it.entries }
                .distinctBy { it.x }
                .map {
                    LegendItemData(
                        name = it.x,
                        symbolShape = SymbolShape.RECTANGLE,
                        color = it.color,
                        dashed = false,
                    )
                }
        }
}
