package io.github.aughtone.charts.bar

/**
 * The measured bounds of one drawn bar, used to resolve which bar a touch or click selected.
 *
 * @param width Horizontal extent of the bar, in pixels.
 * @param height Vertical extent of the bar, in pixels.
 * @param data The entry this bar was drawn from.
 */
data class BarChartBar(
    val width: ClosedFloatingPointRange<Float>,
    val height: ClosedFloatingPointRange<Float>,
    val data: BarChartEntry
)
