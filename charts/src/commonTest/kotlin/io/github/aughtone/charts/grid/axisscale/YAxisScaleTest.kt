package io.github.aughtone.charts.grid.axisscale

import kotlin.test.Test
import kotlin.test.assertEquals

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

        assertEquals(0f, scale.min)
        assertEquals(0f, scale.max)
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
    fun degenerateRangeYieldsZeroTick() {
        // min == max leaves no range to divide; niceNum collapses to zero rather than NaN.
        val scale = YAxisScale(min = 10f, max = 10f, maxTickCount = 5, roundClosestTo = 10)

        assertEquals(0f, scale.tick)
    }
}
