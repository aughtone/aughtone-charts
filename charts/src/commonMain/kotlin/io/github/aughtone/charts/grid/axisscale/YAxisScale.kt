package io.github.aughtone.charts.grid.axisscale

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

/**
 * A y-axis whose bounds are widened to round numbers and divided into readable ticks.
 *
 * The lower bound is rounded down and the upper bound up, to multiples of [roundClosestTo], so the
 * axis always contains the data. A range with no width — a constant series, or values that are all
 * zero — is widened by one step, so there is always a range to draw against: upwards from zero, so a
 * baseline stays at the bottom, or evenly around any other value, so a flat line sits mid-chart. A
 * NaN bound is treated as zero.
 *
 * Tick spacing is then a round number — 1, 2 or 5 times a power of ten — close to the range divided
 * by [maxTickCount]. Because the spacing is rounded, the axis can end up with a few more intervals
 * than [maxTickCount].
 *
 * @param min Lowest value the axis must contain.
 * @param max Highest value the axis must contain.
 * @param maxTickCount The number of intervals to aim for.
 * @param roundClosestTo Multiple the bounds are rounded out to. Values below 1 are treated as 1.
 */
class YAxisScale(
    min: Float,
    max: Float,
    maxTickCount: Int,
    roundClosestTo: Int,
) {
    val tick: Float
    val min: Float
    val max: Float

    init {
        val step = roundClosestTo.coerceAtLeast(1)
        // Rounding the lower bound down and the upper bound up is what keeps the data inside the
        // axis whatever its sign. Adding 0f turns a -0.0 from ceil into 0.0, so no label reads -0.
        var low = floor((if (min.isNaN()) 0f else min) / step) * step + 0f
        var high = ceil((if (max.isNaN()) 0f else max) / step) * step + 0f
        if (low == high) {
            if (low == 0f) {
                high = step.toFloat()
            } else {
                low -= step
                high += step
            }
        }
        this.min = low
        this.max = high

        val range = niceNum(this.max - this.min, false)
        this.tick = niceNum(range / (maxTickCount), true)
    }

    /**
     * Returns a "nice" number approximately equal to range.
     * Rounds the number if round = true Takes the ceiling if round = false.
     *
     * @param range the data range
     * @param round whether to round the result
     * @return a "nice" number to be used for the data range
     */
    private fun niceNum(range: Float, round: Boolean): Float {
        /** nice, rounded fraction  */
        val exponent: Float = floor(log10(range))
        /** exponent of range  */
        val fraction = range / 10.0f.pow(exponent)
        /** fractional part of range  */
        val niceFraction: Float = if (round) {
            if (fraction < 1.5) 1.0f else if (fraction < 3) 2.0f else if (fraction < 7) 5.0f else 10.0f
        } else {
            if (fraction <= 1) 1.0f else if (fraction <= 2) 2.0f else if (fraction <= 5) 5.0f else 10.0f
        }
        return niceFraction * 10.0f.pow(exponent)
    }
}
