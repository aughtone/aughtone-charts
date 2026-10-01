package io.github.aughtone.charts.line

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import io.github.aughtone.charts.fadedBy
import io.github.aughtone.charts.grid.PlotArea
import io.github.aughtone.charts.grid.axisscale.YAxisScale
import io.github.aughtone.charts.grid.positionOf
import io.github.aughtone.charts.mapValueToDifferentRange

internal fun DrawScope.drawLineChart(
    lineChartData: LineChartData,
    yAxisScale: YAxisScale,
    area: PlotArea,
    alpha: List<Float>,
) {
    // calculate path
    val path = Path()
    lineChartData.series.forEachIndexed { seriesIndex, data ->

        val mappedPoints = mapDataToPixels(lineChartData, data, yAxisScale, area, size.width)
        // An empty series has nothing to draw, and closing its fill needs a first and last point.
        if (mappedPoints.isEmpty()) return@forEachIndexed

        val connectionPoints = calculateConnectionPointsForBezierCurve(mappedPoints)

        path.reset() // reuse path
        mappedPoints.forEachIndexed { index, value ->
            if (index == 0) {
                path.moveTo(value.x, value.y)
            } else {
                path.cubicTo(
                    connectionPoints[index - 1].first.x,
                    connectionPoints[index - 1].first.y,
                    connectionPoints[index - 1].second.x,
                    connectionPoints[index - 1].second.y,
                    value.x,
                    value.y
                )
            }
        }

        // draw line
        drawPath(
            path = path,
            color = data.lineColor.fadedBy(alpha[seriesIndex]),
            style = Stroke(
                width = data.lineWidth.toPx(),
                pathEffect = if (data.dashedLine) dashedPathEffect else null
            )
        )

        // close shape and fill
        path.lineTo(mappedPoints.last().x, size.height)
        path.lineTo(mappedPoints.first().x, size.height)
        drawPath(
            path = path,
            Brush.verticalGradient(
                listOf(
                    Color.Transparent,
                    data.fillColor.fadedBy(alpha[seriesIndex] / 12),
                    data.fillColor.fadedBy(alpha[seriesIndex] / 6)
                ),
                startY = path.getBounds().bottom,
                endY = path.getBounds().top,
            ),
            style = Fill
        )
    }
}

/**
 * Maps a series' points to canvas pixels: x across [width] over the chart's time range, and y into
 * [area] through [yAxisScale] — the same scale and area the grid is drawn with, so the line sits
 * against its own axis labels.
 */
internal fun mapDataToPixels(
    lineChartData: LineChartData,
    currentSeries: LineChartSeries,
    yAxisScale: YAxisScale,
    area: PlotArea,
    width: Float,
): List<PointF> = currentSeries.listOfPoints.map {
    // A single timestamp leaves no time range to spread across: centre it rather than divide by zero.
    val x = if (lineChartData.maxX == lineChartData.minX) {
        width / 2f
    } else {
        it.x.mapValueToDifferentRange(lineChartData.minX, lineChartData.maxX, 0f, width)
    }
    PointF(x, yAxisScale.positionOf(it.y, area))
}

private fun calculateConnectionPointsForBezierCurve(points: List<PointF>): MutableList<Pair<PointF, PointF>> {
    val conPoint = mutableListOf<Pair<PointF, PointF>>()
    for (i in 1 until points.size) {
        conPoint.add(
            Pair(
                PointF((points[i].x + points[i - 1].x) / 2f, points[i - 1].y),
                PointF((points[i].x + points[i - 1].x) / 2f, points[i].y)
            )
        )
    }
    return conPoint
}

/**
 * A point in the chart's canvas, in pixels.
 *
 * @param x Horizontal offset.
 * @param y Vertical offset.
 */
data class PointF(val x: Float, val y: Float)
