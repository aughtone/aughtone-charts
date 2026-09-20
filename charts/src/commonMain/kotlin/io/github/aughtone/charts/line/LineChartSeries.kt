package io.github.aughtone.charts.line

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.aughtone.charts.grid.GridChartData

@Immutable
/**
 * One plotted point in a [LineChartSeries].
 *
 * @param x Timestamp of the point, in epoch milliseconds.
 * @param y Value at that timestamp.
 */
data class LineChartPoint(
    val x: Long,
    val y: Float,
)

@Immutable
/**
 * One line within a [LineChartData], together with how it is drawn.
 *
 * The bounds are derived from [listOfPoints] when the series is constructed, and are all zero
 * for an empty series.
 *
 * @param dataName Name of the series, shown in the legend and the touch overlay.
 * @param lineWidth Thickness of the drawn line.
 * @param lineColor Color of the line, and of its legend symbol.
 * @param fillColor Color of the shading under the line. Defaults to [lineColor].
 * @param dashedLine Whether the line is drawn dashed.
 * @param listOfPoints Points making up the line. May be empty.
 */
data class LineChartSeries(
    val dataName: String,
    val lineWidth: Dp = 3.dp,
    val lineColor: Color, // this should be captured from Material colourScheme which means it should be composable.
    val fillColor: Color = lineColor,
    val dashedLine: Boolean = false,
    val listOfPoints: List<LineChartPoint> = emptyList(),
) {
    /** Lowest [LineChartPoint.y] in the series, or `0f` when it is empty. */
    val minValue: Float
    /** Highest [LineChartPoint.y] in the series, or `0f` when it is empty. */
    val maxValue: Float
    /** Earliest [LineChartPoint.x] in the series, or `0` when it is empty. */
    val minTimestamp: Long
    /** Latest [LineChartPoint.x] in the series, or `0` when it is empty. */
    val maxTimeStamp: Long

    init {
        // find max and min in series
        if (listOfPoints.isNotEmpty()) {
            val minMaxValue = getMinMaxValue()
            minValue = minMaxValue.first
            maxValue = minMaxValue.second

            val minMaxTimestamp = getMinMaxTimestamp()
            minTimestamp = minMaxTimestamp.first
            maxTimeStamp = minMaxTimestamp.second
        } else {
            minValue = 0f
            maxValue = 0f
            minTimestamp = 0L
            maxTimeStamp = 0L
        }
    }

    private fun getMinMaxTimestamp(): Pair<Long, Long> {
        val sortedTimestamp = listOfPoints.sortedBy { it.x }
        return Pair(sortedTimestamp.first().x, sortedTimestamp.last().x)
    }

    private fun getMinMaxValue(): Pair<Float, Float> {
        val sortedValue = listOfPoints.sortedBy { it.y }
        return Pair(sortedValue.first().y, sortedValue.last().y)
    }
}

@Immutable
/**
 * Data for [LineChart]: one or more series sharing a single pair of axes.
 *
 * The axis ranges span every series and are derived when the data is constructed.
 *
 * @param series Lines to draw. May be empty, in which case every bound is zero.
 */
data class LineChartData(
    val series: List<LineChartSeries>,
) : GridChartData {
    override val legendData: List<LegendItemData>
        get() = series.map {
            LegendItemData(
                name = it.dataName,
                symbolShape = SymbolShape.LINE,
                color = it.lineColor,
                dashed = it.dashedLine
            )
        }

    override val minX: Long
    override val maxX: Long
    override val minY: Float
    override val maxY: Float

    init {
        // find max and min in all data
        val timeStamps = mutableListOf<Long>()
        val values = mutableListOf<Float>()
        series.forEach {
            timeStamps.add(it.minTimestamp)
            timeStamps.add(it.maxTimeStamp)
            values.add(it.minValue)
            values.add(it.maxValue)
        }
        minX = timeStamps.minOrNull() ?: 0
        maxX = timeStamps.maxOrNull() ?: 0
        minY = values.minOrNull() ?: 0f
        maxY = values.maxOrNull() ?: 0f
    }
}
