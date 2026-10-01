package io.github.aughtone.charts.grid.axisscale

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class YAxisScaleTest {

    @Test
    fun producesNiceBoundsAndTickForASimpleRange() {
        val scale = YAxisScale(min = 0f, max = 100f, maxTickCount = 5, roundClosestTo = 10)

        assertEquals(0f, scale.min)
        assertEquals(100f, scale.max)
        assertEquals(20f, scale.tick)
    }

    @Test
    fun roundsBoundsOutwardToTheRequestedMultiple() {
        val scale = YAxisScale(min = -45f, max = 41f, maxTickCount = 5, roundClosestTo = 10)

        assertEquals(-50f, scale.min)
        assertEquals(50f, scale.max)
    }

    @Test
    fun treatsNaNBoundsAsZero() {
        val scale = YAxisScale(
            min = Float.NaN,
            max = Float.NaN,
            maxTickCount = 5,
            roundClosestTo = 10,
        )

        // Both bounds become zero, which is then widened upwards so there is a range to draw.
        assertEquals(0f, scale.min)
        assertEquals(10f, scale.max)
    }

    @Test
    fun zeroCrossingRangeKeepsBothSides() {
        val scale = YAxisScale(min = -20f, max = 80f, maxTickCount = 5, roundClosestTo = 10)

        assertEquals(-20f, scale.min)
        assertEquals(80f, scale.max)
    }

    @Test
    fun fractionalMaxRoundsUpToContainTheData() {
        // Regression guard: this used to truncate to 40, putting a 40.7 data point outside the
        // plotted area.
        val scale = YAxisScale(min = 0f, max = 40.7f, maxTickCount = 4, roundClosestTo = 10)

        assertEquals(50f, scale.max)
    }

    @Test
    fun fractionalMinRoundsDownToContainTheData() {
        val scale = YAxisScale(min = -40.7f, max = 0f, maxTickCount = 4, roundClosestTo = 10)

        assertEquals(-50f, scale.min)
    }

    @Test
    fun boundsAlreadyOnAMultipleAreLeftAlone() {
        val scale = YAxisScale(min = -40f, max = 40f, maxTickCount = 4, roundClosestTo = 10)

        assertEquals(-40f, scale.min)
        assertEquals(40f, scale.max)
    }

    @Test
    fun anAllPositiveRangeContainsTheData() {
        // Regression guard: a positive minimum used to round up, away from zero, so a 15..25
        // series got an axis of 20..30 and its lowest points fell below the chart.
        val scale = YAxisScale(min = 15f, max = 25f, maxTickCount = 4, roundClosestTo = 10)

        assertEquals(10f, scale.min)
        assertEquals(30f, scale.max)
    }

    @Test
    fun aSmallPositiveRangeDoesNotCollapse() {
        // Regression guard: 3..7 used to round both bounds up to 10, leaving no range at all.
        val scale = YAxisScale(min = 3f, max = 7f, maxTickCount = 4, roundClosestTo = 10)

        assertEquals(0f, scale.min)
        assertEquals(10f, scale.max)
    }

    @Test
    fun anAllNegativeRangeContainsTheData() {
        val scale = YAxisScale(min = -25f, max = -15f, maxTickCount = 4, roundClosestTo = 10)

        assertEquals(-30f, scale.min)
        assertEquals(-10f, scale.max)
    }

    @Test
    fun aConstantValueIsWidenedAroundIt() {
        // Regression guard: a series that never changes left min == max, so there was no range
        // to map values onto and a line drawn against it came out as NaN.
        val scale = YAxisScale(min = 10f, max = 10f, maxTickCount = 5, roundClosestTo = 10)

        assertEquals(0f, scale.min)
        assertEquals(20f, scale.max)
        assertTrue(scale.tick > 0f, "tick was ${scale.tick}")
    }

    @Test
    fun allZeroValuesGetARangeAboveZero() {
        // Regression guard: a bar chart whose values were all zero had a 0..0 scale.
        val scale = YAxisScale(min = 0f, max = 0f, maxTickCount = 5, roundClosestTo = 10)

        assertEquals(0f, scale.min)
        assertEquals(10f, scale.max)
    }

    @Test
    fun aNonPositiveRoundingStepIsTreatedAsOne() {
        // A step of zero used to divide by zero and give NaN bounds.
        val scale = YAxisScale(min = 1.5f, max = 4.5f, maxTickCount = 4, roundClosestTo = 0)

        assertEquals(1f, scale.min)
        assertEquals(5f, scale.max)
    }

    @Test
    fun aBoundThatRoundsToZeroIsNotNegativeZero() {
        // ceil(-0.3) is -0.0, which would label an axis "-0".
        val scale = YAxisScale(min = -3f, max = -1f, maxTickCount = 4, roundClosestTo = 10)

        assertEquals(0f, scale.max)
        assertTrue(1f / scale.max > 0f, "max was negative zero")
    }
}
