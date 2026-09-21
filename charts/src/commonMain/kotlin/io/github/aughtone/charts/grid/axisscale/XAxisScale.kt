package io.github.aughtone.charts.grid.axisscale

/**
 * The x-axis range and tick spacing a chart's grid is measured from.
 *
 * @see FixedTicksXAxisScale
 * @see TimestampXAxisScale
 */
interface XAxisScale {
    /** Distance between adjacent grid lines, in axis units. */
    val tick: Long
    /** Lowest value on the axis. */
    val min: Long
    /** Highest value on the axis. */
    val max: Long
    /** Value of the first grid line, which need not be [min]. */
    val start: Long
}
