package io.github.aughtone.charts.simple // Or your preferred package

import androidx.compose.foundation.Canvas
// import androidx.compose.foundation.border.border // Not adding for now, as it might not be available
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A very simple line chart Composable for KMP.
 * It draws lines connecting a series of y-values, with an option for curved lines.
 *
 * @param dataPoints A list of Float values representing the y-coordinates of the chart.
 *                   The x-coordinates are derived from the index of these points.
 * @param modifier Modifier for this Composable.
 * @param lineColor The color of the line.
 * @param lineStrokeWidth The thickness of the line.
 * @param adaptToData If true, the y-axis will scale to fit the min/max of the dataPoints.
 *                    If false, it assumes dataPoints are already normalized between 0f (bottom) and 1f (top).
 * @param useCurvedLines If true, draws smooth curves through the points instead of straight lines.
 *                       For 2 points, a straight line is drawn.
 * @param extendAndClipEnds If true, the line ends are extended slightly beyond the component
 *                          boundaries and clipped, creating a clean edge. This is most
 *                          effective when lineStrokeWidth > 0.
 */
@Composable
fun SimpleLineChart(
    dataPoints: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    lineStrokeWidth: Dp = 4.dp,
    adaptToData: Boolean = true,
    useCurvedLines: Boolean = false,
    extendAndClipEnds: Boolean = false // New parameter
) {
    if (dataPoints.isEmpty()) {
        Canvas(modifier = modifier) {} // Draw nothing for empty list
        return
    }

    if (dataPoints.size == 1) {
        Canvas(modifier = modifier) { // Density scope for single point
            val singlePointLineThicknessPx = lineStrokeWidth.toPx() // Correct: inside DrawScope
            val y = if (adaptToData) size.height / 2f else size.height * (1f - dataPoints.first()
                .coerceIn(0f, 1f))
            drawLine(
                color = lineColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = singlePointLineThicknessPx,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)),
                cap = StrokeCap.Round
            )
        }
        return
    }

    Canvas(modifier = modifier) { // Density scope for main chart
        val lineThicknessPx = lineStrokeWidth.toPx() // Correct: inside DrawScope

        val (minY, maxY) = if (adaptToData) {
            Pair(dataPoints.minOrNull() ?: 0f, dataPoints.maxOrNull() ?: 1f)
        } else {
            Pair(0f, 1f) // Assume data is normalized if not adapting
        }

        val yRange = if ((maxY - minY) == 0f) 1f else maxY - minY // Avoid division by zero

        val currentVisibleWidth = size.width
        val actualDrawingWidth: Float
        val xOffset: Float

        if (extendAndClipEnds && lineThicknessPx > 0f) {
            // Extend by half the stroke width. This aligns the center of the stroke cap
            // with the original boundary, making the visible line start/end cleanly at the edge.
            val extensionPx = lineThicknessPx / 2f
            actualDrawingWidth = currentVisibleWidth + 2 * extensionPx
            xOffset = -extensionPx
        } else {
            actualDrawingWidth = currentVisibleWidth
            xOffset = 0f
        }

        val xStep = actualDrawingWidth / (dataPoints.size - 1)

        val canvasPoints = dataPoints.mapIndexed { index, value ->
            val x = xOffset + (index * xStep)
            val y = size.height * (1f - ((value - minY) / yRange).coerceIn(0f, 1f))
            Offset(x, y)
        }

        if (useCurvedLines) {
            val path = Path()
            path.moveTo(canvasPoints[0].x, canvasPoints[0].y)

            if (canvasPoints.size == 2) { // Straight line for 2 points
                path.lineTo(canvasPoints[1].x, canvasPoints[1].y)
            } else { // More than 2 points, use curves
                val tension = 0.2f
                for (i in 0 until canvasPoints.size - 1) {
                    val p_i = canvasPoints[i]
                    val p_i_plus_1 = canvasPoints[i + 1]
                    val p_i_minus_1 = canvasPoints.getOrElse(i - 1) { p_i }
                    val p_i_plus_2 = canvasPoints.getOrElse(i + 2) { p_i_plus_1 }

                    val cp1_x = p_i.x + (p_i_plus_1.x - p_i_minus_1.x) * tension
                    val cp1_y = p_i.y + (p_i_plus_1.y - p_i_minus_1.y) * tension
                    val cp2_x = p_i_plus_1.x - (p_i_plus_2.x - p_i.x) * tension
                    val cp2_y = p_i_plus_1.y - (p_i_plus_2.y - p_i.y) * tension

                    path.cubicTo(cp1_x, cp1_y, cp2_x, cp2_y, p_i_plus_1.x, p_i_plus_1.y)
                }
            }
            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(width = lineThicknessPx, cap = StrokeCap.Round)
            )
        } else { // Draw straight lines
            for (i in 0 until canvasPoints.size - 1) {
                drawLine(
                    color = lineColor,
                    start = canvasPoints[i],
                    end = canvasPoints[i + 1],
                    strokeWidth = lineThicknessPx,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

@Preview
@Composable
private fun SimpleLineChartIncreasingPreview() {
    MaterialTheme {
        SimpleLineChart(
            dataPoints = listOf(10f, 20f, 5f, 40f, 30f, 60f, 50f),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            lineColor = MaterialTheme.colorScheme.tertiary,
            lineStrokeWidth = 3.dp
        )
    }
}

@Preview
@Composable
private fun SimpleLineChartIncreasingCurvedPreview() {
    MaterialTheme {
        SimpleLineChart(
            dataPoints = listOf(10f, 20f, 5f, 40f, 30f, 60f, 50f),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            lineColor = MaterialTheme.colorScheme.primary,
            lineStrokeWidth = 3.dp,
            useCurvedLines = true
        )
    }
}

@Preview
@Composable
private fun SimpleLineChartNormalizedPreview() {
    MaterialTheme {
        SimpleLineChart(
            dataPoints = listOf(0.1f, 0.3f, 0.2f, 0.8f, 0.5f, 0.9f, 0.4f),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            lineColor = MaterialTheme.colorScheme.secondary,
            lineStrokeWidth = 2.dp,
            adaptToData = false // Data is already 0-1
        )
    }
}

@Preview
@Composable
private fun SimpleLineChartNormalizedCurvedPreview() {
    MaterialTheme {
        SimpleLineChart(
            dataPoints = listOf(0.1f, 0.3f, 0.2f, 0.8f, 0.5f, 0.9f, 0.4f),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            lineColor = MaterialTheme.colorScheme.secondary,
            lineStrokeWidth = 2.dp,
            adaptToData = false, // Data is already 0-1
            useCurvedLines = true
        )
    }
}

@Preview
@Composable
private fun SimpleLineChartFlatPreview() {
    MaterialTheme {
        SimpleLineChart(
            dataPoints = listOf(50f, 50f, 50f, 50f),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun SimpleLineChartFlatCurvedPreview() {
    MaterialTheme {
        SimpleLineChart(
            dataPoints = listOf(50f, 50f, 50f, 50f),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            useCurvedLines = true
        )
    }
}

@Preview
@Composable
private fun SimpleLineChartTwoPointsPreview() {
    MaterialTheme {
        SimpleLineChart(
            dataPoints = listOf(20f, 80f),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun SimpleLineChartTwoPointsCurvedPreview() {
    MaterialTheme {
        SimpleLineChart(
            dataPoints = listOf(20f, 80f), // Will draw a straight line
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            useCurvedLines = true,
            lineStrokeWidth = 4.dp
        )
    }
}

@Preview
@Composable
private fun SimpleLineChartSinglePointPreview() {
    MaterialTheme {
        SimpleLineChart(
            dataPoints = listOf(50f),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun SimpleLineChartEmptyPreview() {
    MaterialTheme {
        SimpleLineChart(
            dataPoints = emptyList(),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun SimpleLineChartExtendAndClipPreview() {
    MaterialTheme {
        SimpleLineChart(
            dataPoints = listOf(10f, 60f, 20f, 80f, 30f),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp) // Smaller height to see edges clearly
                .padding(8.dp),
            lineColor = MaterialTheme.colorScheme.error,
            lineStrokeWidth = 8.dp, // Thicker stroke
            extendAndClipEnds = true,
            useCurvedLines = false
        )
    }
}

@Preview
@Composable
private fun SimpleLineChartExtendAndClipCurvedPreview() {
    MaterialTheme {
        SimpleLineChart(
            dataPoints = listOf(10f, 60f, 20f, 80f, 30f),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(8.dp),
            lineColor = MaterialTheme.colorScheme.tertiary,
            lineStrokeWidth = 8.dp, // Thicker stroke
            extendAndClipEnds = true,
            useCurvedLines = true
        )
    }
}
