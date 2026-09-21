package io.github.aughtone.charts.grid

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import io.github.aughtone.charts.grid.axisscale.XAxisScale
import io.github.aughtone.charts.grid.axisscale.YAxisScale
import io.github.aughtone.charts.mapValueToDifferentRange

/**
 * Draws a measured grid, including its baseline.
 *
 * @param grid Grid to draw, as returned by [measureChartGrid].
 * @param color Color of the grid lines.
 */
fun DrawScope.drawChartGrid(grid: ChartGrid, color: Color) {
    grid.horizontalLines.forEach {
        drawLine(
            color = color,
            start = Offset(0f, it.position),
            end = Offset(size.width, it.position),
            strokeWidth = 1f
        )
    }
    drawLine(
        color = color,
        start = Offset(0f, grid.zeroPosition.position),
        end = Offset(size.width, grid.zeroPosition.position),
        strokeWidth = 1f
    )
    grid.verticalLines.forEach {
        drawLine(
            color = color,
            start = Offset(it.position, 0f),
            end = Offset(it.position, size.height),
            strokeWidth = 1f
        )
    }
}

/**
 * Works out where an axis grid's lines fall within the current canvas.
 *
 * @param xAxisScale Supplies the x-axis range and tick spacing.
 * @param yAxisScale Supplies the y-axis range and tick spacing.
 * @param horizontalLinesOffset Padding kept above and below the plotted range.
 * @return The measured grid, ready to pass to [drawChartGrid].
 */
fun DrawScope.measureChartGrid(
    xAxisScale: XAxisScale,
    yAxisScale: YAxisScale,
    horizontalLinesOffset: Dp
): ChartGrid {

    val horizontalLines = measureHorizontalLines(
        axisScale = yAxisScale,
        startPosition = size.height,
        endPosition = 0f
    )

    val verticalLines = measureVerticalLines(
        axisScale = xAxisScale,
        startPosition = 0f,
        endPosition = size.width
    )

    val zero = when {
        yAxisScale.min > 0 -> yAxisScale.min
        yAxisScale.max < 0 -> yAxisScale.max
        else -> 0f
    }
    return ChartGrid(
        verticalLines = verticalLines,
        horizontalLines = horizontalLines,
        zeroPosition = LineParameters(
            zero.mapValueToDifferentRange(
                yAxisScale.min,
                yAxisScale.max,
                size.height,
                0f
            ),
            zero
        )
    )
}

private fun measureHorizontalLines(
    axisScale: YAxisScale,
    startPosition: Float,
    endPosition: Float
): List<LineParameters> {
    val horizontalLines = mutableListOf<LineParameters>()

    if (axisScale.max == axisScale.min || axisScale.tick == 0f)
        return listOf(
            LineParameters(
                position = startPosition / 2f,
                value = 0
            )
        )

    val valueStep = axisScale.tick
    var currentValue = axisScale.min

    while (currentValue in axisScale.min..axisScale.max) {
        val currentPosition = currentValue.mapValueToDifferentRange(
            axisScale.min,
            axisScale.max,
            startPosition,
            endPosition
        )
        horizontalLines.add(
            LineParameters(
                position = currentPosition,
                value = currentValue
            )
        )
        currentValue += valueStep
    }
    return horizontalLines
}

private fun measureVerticalLines(
    axisScale: XAxisScale,
    startPosition: Float,
    endPosition: Float
): List<LineParameters> {
    val verticalLines = mutableListOf<LineParameters>()
    val valueStep = axisScale.tick
    var currentValue = axisScale.start

    while (currentValue in axisScale.min..axisScale.max) {
        val currentPosition = currentValue.mapValueToDifferentRange(
            axisScale.min,
            axisScale.max,
            startPosition,
            endPosition
        )
        verticalLines.add(
            LineParameters(
                position = currentPosition,
                value = currentValue
            )
        )
        currentValue += valueStep
    }
    return verticalLines
}
