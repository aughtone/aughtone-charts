package io.github.aughtone.charts.grid.axisscale

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class FixedTicksXAxisScaleTest {

    @Test
    fun dividesTheRangeIntoEvenTicks() {
        val scale = FixedTicksXAxisScale(min = 0L, max = 100L, tickCount = 5)

        assertEquals(0L, scale.min)
        assertEquals(100L, scale.max)
        assertEquals(20L, scale.tick)
        assertEquals(0L, scale.start)
    }

    @Test
    fun startsAtMinForANonZeroOrigin() {
        val scale = FixedTicksXAxisScale(min = 50L, max = 150L, tickCount = 4)

        assertEquals(50L, scale.start)
        assertEquals(25L, scale.tick)
    }

    @Test
    fun tickTruncatesWhenTheRangeDoesNotDivideEvenly() {
        // Integer division: 10 / 3 is 3, not 3.33, so the final tick falls short of max.
        val scale = FixedTicksXAxisScale(min = 0L, max = 10L, tickCount = 3)

        assertEquals(3L, scale.tick)
    }

    @Test
    fun zeroTickCountDividesByZero() {
        // BarChart derives tickCount from the category count, so an unguarded zero reaches here.
        // The exception type is platform-specific (ArithmeticException on JVM, a plain Exception
        // on JS), so assert only that it fails.
        assertFails {
            FixedTicksXAxisScale(min = 0L, max = 10L, tickCount = 0)
        }
    }
}
