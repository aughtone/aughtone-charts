package io.github.aughtone.charts.grid.axisscale

/**
 * An x-axis over epoch milliseconds, with ticks on round times.
 *
 * A window at least [maxTicksCount] hours long ticks every whole number of hours, chosen so that
 * there are roughly [maxTicksCount] ticks. A shorter window steps down through 30, 15, 10, 5, 2 and
 * 1 minutes, then 30, 15, 10, 5, 2 and 1 seconds, taking the finest of those that gives no more than
 * [maxTicksCount] ticks; if none fits, it ticks hourly.
 *
 * Ticks fall on round times measured from the epoch, which is to say in UTC: whole hours, or
 * multiples of the minute or second step. The first tick is the first such time after [min], so a
 * single timestamp gets no ticks.
 *
 * @param min Lowest timestamp on the axis, in epoch milliseconds.
 * @param max Highest timestamp on the axis, in epoch milliseconds.
 * @param maxTicksCount The number of ticks to aim for. Values below 1 are treated as 1.
 */
class TimestampXAxisScale(
    override val min: Long,
    override val max: Long,
    val maxTicksCount: Int = 10
) : XAxisScale {
    private val ticksWanted = maxTicksCount.coerceAtLeast(1)

    private val hourlyPeriod: Long = HOUR_MS * ((max - min) / HOUR_MS / ticksWanted)

    override val tick: Long = if (hourlyPeriod > 0) {
        hourlyPeriod
    } else {
        SUB_HOUR_STEPS.firstOrNull { (max - min) / it <= ticksWanted } ?: HOUR_MS
    }

    // Hour ticks start on the hour, as they always have; finer ticks start on a multiple of the
    // step itself, so five-minute ticks fall on :05, :10, :15 rather than wherever min happened to be.
    override val start: Long = (if (tick >= HOUR_MS) HOUR_MS else tick).let { unit ->
        min - min % unit + unit
    }

    companion object {
        /** One hour, in milliseconds. */
        const val HOUR_MS = 3600000L

        private const val SECOND_MS = 1000L
        private const val MINUTE_MS = 60 * SECOND_MS

        private val SUB_HOUR_STEPS = listOf(1L, 2L, 5L, 10L, 15L, 30L).map { it * SECOND_MS } +
            listOf(1L, 2L, 5L, 10L, 15L, 30L).map { it * MINUTE_MS }
    }
}
