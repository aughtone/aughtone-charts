package io.github.aughtone.charts.grid.axisscale

/**
 * An x-axis over epoch milliseconds, with grid lines on whole hours.
 *
 * The first line is the first whole hour after [min], and spacing is rounded down to a whole
 * number of hours so that at most [maxTicksCount] lines are drawn. A range shorter than
 * [maxTicksCount] hours, including an empty range, falls back to hourly spacing.
 *
 * @param min Lowest timestamp on the axis, in epoch milliseconds.
 * @param max Highest timestamp on the axis, in epoch milliseconds.
 * @param maxTicksCount Upper bound on the number of grid lines.
 */
class TimestampXAxisScale(
    override val min: Long,
    override val max: Long,
    val maxTicksCount: Int = 10
) : XAxisScale {
    // find first round hour, greater than min timestamp
    override val start: Long = min - min % HOUR_MS + HOUR_MS

    // find period of vertical lines based on maxTicksCount, period can be in round hours ie. 1h, 2h, 3h
    private val period: Long = HOUR_MS * ((max - min) / HOUR_MS / maxTicksCount)

    // if period is 0 or less set period to round hour
    // avoid division by 0 when app starts and min and max are 0
    override val tick: Long = if (period > 0) period else HOUR_MS

    companion object {
        /** One hour, in milliseconds. */
        const val HOUR_MS = 3600000L
    }
}
