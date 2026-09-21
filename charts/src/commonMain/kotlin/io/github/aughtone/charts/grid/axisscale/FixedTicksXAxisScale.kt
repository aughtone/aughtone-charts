package io.github.aughtone.charts.grid.axisscale

/**
 * An x-axis divided into a fixed number of equal ticks.
 *
 * Tick spacing is integer division of the range, so it truncates where the range does not divide
 * evenly and the last line falls short of [max].
 *
 * @param min Lowest value on the axis; also the first grid line.
 * @param max Highest value on the axis.
 * @param tickCount Number of intervals to divide the range into. Must not be zero.
 */
class FixedTicksXAxisScale(
    override val min: Long,
    override val max: Long,
    tickCount: Int
) : XAxisScale {
    override val tick: Long = (max - min) / tickCount
    override val start: Long = min
}
