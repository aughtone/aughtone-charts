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
 * @param horizontalLinesOffset Space kept clear above and below the plotted range, so the top and
 * bottom lines are not drawn on the edge. It is capped at half the canvas height.
 * @return The measured grid, ready to pass to [drawChartGrid].
 */
fun DrawScope.measureChartGrid(
    xAxisScale: XAxisScale,
    yAxisScale: YAxisScale,
    horizontalLinesOffset: Dp
): ChartGrid {

    val area = plotArea(size.height, horizontalLinesOffset.toPx())
    val horizontalLines = measureHorizontalLines(axisScale = yAxisScale, area = area)

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
        zeroPosition = LineParameters(yAxisScale.positionOf(zero, area), zero)
    )
}

internal fun measureHorizontalLines(
    axisScale: YAxisScale,
    area: PlotArea,
): List<LineParameters> {
    val horizontalLines = mutableListOf<LineParameters>()

    // No range, or no step to walk it with: draw the one line there is, labelled with its real
    // value rather than zero.
    if (axisScale.max == axisScale.min || axisScale.tick == 0f)
        return listOf(
            LineParameters(
                position = axisScale.positionOf(axisScale.min, area),
                value = axisScale.min
            )
        )

    val valueStep = axisScale.tick
    var currentValue = axisScale.min

    while (currentValue in axisScale.min..axisScale.max) {
        val currentPosition = axisScale.positionOf(currentValue, area)
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

/**
 * The vertical extent a grid, and everything drawn against it, is mapped into, in pixels: the axis
 * maximum sits at [top] and the minimum at [bottom].
 */
internal class PlotArea(val top: Float, val bottom: Float)

/**
 * The plot area for a canvas [height] with [offset] kept clear above and below. The offset is
 * capped at half the height, so a large one collapses the area rather than turning it upside down.
 */
internal fun plotArea(height: Float, offset: Float): PlotArea {
    val clear = offset.coerceIn(0f, height / 2f)
    return PlotArea(top = clear, bottom = height - clear)
}

/**
 * Where [value] sits within [area] on this scale.
 *
 * Everything drawn against a grid goes through this one mapping — the grid lines, the zero line,
 * bars and line points — so they cannot drift apart. A scale with no range places everything
 * mid-area rather than dividing by zero.
 */
internal fun YAxisScale.positionOf(value: Float, area: PlotArea): Float =
    if (max == min) {
        (area.top + area.bottom) / 2f
    } else {
        value.mapValueToDifferentRange(min, max, area.bottom, area.top)
    }
