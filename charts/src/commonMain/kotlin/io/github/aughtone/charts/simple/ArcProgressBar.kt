package io.github.aughtone.charts.simple

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview // KMP Preview

/**
 * A Composable that displays a progress bar in an arc shape, suitable for KMP.
 *
 * @param progress The current progress to display, between 0.0f and 1.0f.
 * @param modifier Modifier for this Composable.
 * @param color The color of the progress arc.
 * @param strokeWidth The width of the progress arc and its background.
 * @param backgroundColor The color of the background track for the arc.
 * @param startAngle The starting angle of the arc in degrees.
 *                   `180f`: top semi-circle, ends on horizontal plane (progresses left-to-right over the top).
 *                   `0f`: bottom semi-circle, ends on horizontal plane (progresses right-to-left over the bottom).
 *                   `-90f`: right-side semi-circle, ends on vertical plane (progresses top-to-bottom on the right).
 *                   `90f`: left-side semi-circle, ends on vertical plane (progresses bottom-to-top on the left).
 * @param totalArcDegrees The total angular extent of the arc. Defaults to 180f (a semi-circle).
 *                        Use values like 270f for a 3/4 circle, or 360f for a full circle.
 */
@Composable
fun ArcProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = 8.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    startAngle: Float = 180f, // Default to top semi-circle with horizontal ends
    totalArcDegrees: Float = 180f // Default to a 180-degree arc
) {
    // Progress sweep angle is now scaled by totalArcDegrees
    val sweepAngle = progress.coerceIn(0f, 1f) * totalArcDegrees

    Canvas(modifier = modifier) {
        val strokePx = strokeWidth.toPx() // Convert Dp to Px within DrawScope
        val arcStrokeStyle = Stroke(width = strokePx, cap = StrokeCap.Round)
        val backgroundStrokeStyle = Stroke(width = strokePx)

        // Calculate the diameter and top-left position to ensure the arc is centered
        // and fits within the bounds, considering the stroke width.
        val diameter = this.size.minDimension - strokePx
        val topLeftX = (this.size.width - diameter) / 2f
        val topLeftY = (this.size.height - diameter) / 2f
        val arcSize = Size(diameter, diameter)
        val arcTopLeft = Offset(topLeftX, topLeftY)

        // Draw the background arc (track) for totalArcDegrees
        drawArc(
            color = backgroundColor,
            startAngle = startAngle, // Background track starts at the same angle as the progress
            sweepAngle = totalArcDegrees, // Background covers the specified total arc
            useCenter = false,
            topLeft = arcTopLeft,
            size = arcSize,
            style = backgroundStrokeStyle
        )

        // Draw the progress arc
        if (sweepAngle > 0f) { // Only draw if there's progress
            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = arcStrokeStyle
            )
        }
    }
}

@Preview
@Composable
private fun ArcProgressBarPreview_Default_TopHalfHorizontalEnds() {
    MaterialTheme {
        ArcProgressBar(
            progress = 0.75f,
            modifier = Modifier.size(100.dp),
            strokeWidth = 12.dp
            // startAngle = 180f (default)
            // totalArcDegrees = 180f (default)
        )
    }
}

@Preview
@Composable
private fun ArcProgressBarPreview_BottomHalfHorizontalEnds() {
    MaterialTheme {
        ArcProgressBar(
            progress = 0.5f,
            modifier = Modifier.size(100.dp),
            strokeWidth = 12.dp,
            startAngle = 0f, // Bottom half, ends horizontal
            totalArcDegrees = 180f
        )
    }
}

@Preview
@Composable
private fun ArcProgressBarPreview_ZeroProgress_TopHalf() {
    MaterialTheme {
        ArcProgressBar(
            progress = 0f,
            modifier = Modifier.size(100.dp)
            // startAngle = 180f (default)
            // totalArcDegrees = 180f (default)
        )
    }
}

@Preview
@Composable
private fun ArcProgressBarPreview_FullProgress_TopHalf() {
    MaterialTheme {
        ArcProgressBar(
            progress = 1.0f,
            modifier = Modifier.size(100.dp),
            color = MaterialTheme.colorScheme.tertiary
            // startAngle = 180f (default)
            // totalArcDegrees = 180f (default)
        )
    }
}

@Preview
@Composable
private fun ArcProgressBarPreview_RightHalfVerticalEnds() {
    MaterialTheme {
        ArcProgressBar(
            progress = 0.6f,
            modifier = Modifier.size(100.dp),
            startAngle = -90f, // Right half, ends vertical
            totalArcDegrees = 180f
        )
    }
}

@Preview
@Composable
private fun ArcProgressBarPreview_270DegreeArc() {
    MaterialTheme {
        ArcProgressBar(
            progress = 0.75f,
            modifier = Modifier.size(100.dp),
            strokeWidth = 10.dp,
            startAngle = 135f, // Start partway through the top-left quadrant for a 270 arc
            totalArcDegrees = 270f
        )
    }
}

@Preview
@Composable
private fun ArcProgressBarPreview_Custom200DegreeArc() {
    MaterialTheme {
        ArcProgressBar(
            progress = 0.5f,
            modifier = Modifier.size(100.dp),
            strokeWidth = 10.dp,
            startAngle = 170f, // Adjust start angle for aesthetics with 200 degrees
            totalArcDegrees = 200f
        )
    }
}
