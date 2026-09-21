package io.github.aughtone.charts.simple

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.max

/**
 * A simple bar chart Composable for KMP.
 * It draws bars representing a series of y-values.
 * Assumes data points are positive and bars grow upwards from the bottom of the chart.
 *
 * @param dataPoints A list of Float values representing the magnitude of the bars.
 * @param modifier Modifier for this Composable.
 * @param barColor The color of the bars.
 * @param adaptToData If true, the y-axis will scale to fit the min (clamped at 0) and max of the dataPoints.
 *                    If false, it assumes dataPoints are already normalized between 0f (no height) and 1f (full height).
 * @param barWidthFraction Fraction of the available horizontal space each bar should occupy (allows for gaps).
 *                         Value should be between 0f (no width) and 1f (no gap).
 */
@Composable
fun SimpleBarChart(
    dataPoints: List<Float>,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary,
    adaptToData: Boolean = true,
    barWidthFraction: Float = 0.8f
) {
    if (dataPoints.isEmpty()) {
        Canvas(modifier = modifier) {} // Draw nothing for empty list
        return
    }

    val clampedBarWidthFraction = barWidthFraction.coerceIn(0f, 1f)

    Canvas(modifier = modifier) {
        val (minY, maxY) = if (adaptToData) {
            // For bars starting at 0, min is 0, max is the highest data point or 1f if all are 0.
            val dataMax = dataPoints.maxOrNull() ?: 0f
            Pair(0f, max(dataMax, if (dataPoints.all { it == 0f }) 1f else 0f) )// Ensure maxY is at least 1f if all data is 0 to avoid division by zero in range, or max value.
        } else {
            Pair(0f, 1f) // Assume data is normalized (0 to 1 represents height)
        }

        val yRange = (maxY - minY).takeIf { it > 0 } ?: 1f // Avoid division by zero

        val numBars = dataPoints.size
        val slotWidth = size.width / numBars
        val actualBarPixelWidth = slotWidth * clampedBarWidthFraction
        val barHorizontalPadding = slotWidth * (1f - clampedBarWidthFraction) / 2f

        dataPoints.forEachIndexed { index, value ->
            val normalizedValue = ((value - minY) / yRange).coerceIn(0f, 1f)
            val barHeightPx = size.height * normalizedValue
            val barTopY = size.height - barHeightPx
            val barLeftX = (index * slotWidth) + barHorizontalPadding

            if (barHeightPx > 0f) { // Only draw if there's some height
                drawRect(
                    color = barColor,
                    topLeft = Offset(barLeftX, barTopY),
                    size = Size(actualBarPixelWidth, barHeightPx)
                )
            }
        }
    }
}

@Preview("Normal Data")
@Composable
private fun SimpleBarChartNormalDataPreview() {
    MaterialTheme {
        SimpleBarChart(
            dataPoints = listOf(10f, 20f, 5f, 40f, 15f, 30f),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            barColor = MaterialTheme.colorScheme.secondary
        )
    }
}

@Preview("Normalized Data")
@Composable
private fun SimpleBarChartNormalizedDataPreview() {
    MaterialTheme {
        SimpleBarChart(
            dataPoints = listOf(0.1f, 0.8f, 0.3f, 1f, 0.5f, 0.05f),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            barColor = MaterialTheme.colorScheme.tertiary,
            adaptToData = false
        )
    }
}

@Preview
@Composable
private fun SimpleBarChartSingleBarPreview() {
    MaterialTheme {
        SimpleBarChart(
            dataPoints = listOf(50f),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun SimpleBarChartAllZerosPreview() {
    MaterialTheme {
        SimpleBarChart(
            dataPoints = listOf(0f, 0f, 0f, 0f),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(16.dp)
        )
    }
}


@Preview
@Composable
private fun SimpleBarChartEmptyPreview() {
    MaterialTheme {
        SimpleBarChart(
            dataPoints = emptyList(),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun SimpleBarChartWideBarsPreview() {
    MaterialTheme {
        SimpleBarChart(
            dataPoints = listOf(10f, 25f, 5f, 30f),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(16.dp),
            barWidthFraction = 1f // No gaps
        )
    }
}

@Preview
@Composable
private fun SimpleBarChartNarrowBarsPreview() {
    MaterialTheme {
        SimpleBarChart(
            dataPoints = listOf(10f, 25f, 5f, 30f, 10f),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(16.dp),
            barWidthFraction = 0.5f // More gap
        )
    }
}
