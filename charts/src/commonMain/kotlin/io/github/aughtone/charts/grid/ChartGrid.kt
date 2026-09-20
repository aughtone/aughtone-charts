package io.github.aughtone.charts.grid

import androidx.compose.runtime.Immutable
import kotlin.math.abs

@Immutable
/**
 * The measured positions of an axis grid, in pixels within the chart's canvas.
 *
 * @param verticalLines Positions and values of the x-axis grid lines.
 * @param horizontalLines Positions and values of the y-axis grid lines.
 * @param zeroPosition Position of the baseline. Where the value range does not span zero, this
 * is clamped to the nearer end of the range rather than falling outside the chart.
 */
data class ChartGrid(
    val verticalLines: List<LineParameters>,
    val horizontalLines: List<LineParameters>,
    val zeroPosition: LineParameters,
) {
    /**
     * Spacing between adjacent x-axis grid lines, in pixels.
     *
     * Requires at least two vertical lines.
     */
    val distanceBetweenVerticalLines: Float
        get() = abs(verticalLines[1].position - verticalLines[0].position)
}
