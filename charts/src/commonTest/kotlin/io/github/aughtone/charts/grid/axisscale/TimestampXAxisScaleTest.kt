package io.github.aughtone.charts.grid.axisscale

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TimestampXAxisScaleTest {

    private val hour = TimestampXAxisScale.HOUR_MS
    private val minute = 60_000L
    private val second = 1_000L
    private val onTheHour = 1_700_000_000_000L.let { it - it % hour }

    private fun TimestampXAxisScale.ticks() =
        generateSequence(start) { it + tick }.takeWhile { it <= max }.toList()

    @Test
    fun aWindowShorterThanAnHourGetsMinuteTicks() {
        // Regression guard: ticks were whole hours, starting at the first hour after the window
        // began, so a 15-minute window got no ticks and no time labels at all.
        val scale = TimestampXAxisScale(
            min = onTheHour + 5 * minute,
            max = onTheHour + 20 * minute,
            maxTicksCount = 4,
        )

        assertEquals(5 * minute, scale.tick)
        assertEquals(
            listOf(onTheHour + 10 * minute, onTheHour + 15 * minute, onTheHour + 20 * minute),
            scale.ticks(),
        )
    }

    @Test
    fun aWindowOfSecondsGetsSecondTicks() {
        val scale = TimestampXAxisScale(min = onTheHour, max = onTheHour + 20 * second, maxTicksCount = 4)

        assertEquals(5 * second, scale.tick)
        assertEquals(4, scale.ticks().size)
    }

    @Test
    fun windowsOfHoursKeepWholeHourTicks() {
        val scale = TimestampXAxisScale(
            min = onTheHour + 30 * minute,
            max = onTheHour + 30 * minute + 10 * hour,
            maxTicksCount = 4,
        )

        assertEquals(2 * hour, scale.tick)
        assertEquals(onTheHour + hour, scale.start)
    }

    @Test
    fun aSingleTimestampHasNoTicksAndDoesNotThrow() {
        val scale = TimestampXAxisScale(min = onTheHour, max = onTheHour, maxTicksCount = 4)

        assertTrue(scale.ticks().isEmpty())
    }

    @Test
    fun aTickCountOfZeroDoesNotThrow() {
        // Regression guard: the tick count divided the range, so zero threw.
        val scale = TimestampXAxisScale(min = onTheHour, max = onTheHour + hour, maxTicksCount = 0)

        assertTrue(scale.tick > 0L)
    }
}
