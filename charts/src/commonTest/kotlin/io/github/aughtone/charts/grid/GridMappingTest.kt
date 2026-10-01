package io.github.aughtone.charts.grid

import io.github.aughtone.charts.grid.axisscale.YAxisScale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GridMappingTest {

    private val area = PlotArea(top = 10f, bottom = 90f)

    @Test
    fun theOffsetIsKeptClearAboveAndBelow() {
        val plot = plotArea(height = 100f, offset = 10f)

        assertEquals(10f, plot.top)
        assertEquals(90f, plot.bottom)
    }

    @Test
    fun anOffsetLargerThanHalfTheHeightCollapsesRatherThanInverts() {
        // A large offset used to be able to put the top below the bottom, flipping the axis.
        val plot = plotArea(height = 100f, offset = 80f)

        assertTrue(plot.top <= plot.bottom, "top ${plot.top}, bottom ${plot.bottom}")
    }

    @Test
    fun theScaleBoundsMapToTheEdgesOfTheArea() {
        val scale = YAxisScale(min = 0f, max = 100f, maxTickCount = 5, roundClosestTo = 10)

        assertEquals(area.bottom, scale.positionOf(0f, area))
        assertEquals(area.top, scale.positionOf(100f, area))
    }

    @Test
    fun everyHorizontalLineUsesTheSharedMapping() {
        val scale = YAxisScale(min = 15f, max = 25f, maxTickCount = 4, roundClosestTo = 10)

        measureHorizontalLines(scale, area).forEach {
            assertEquals(scale.positionOf(it.value.toFloat(), area), it.position)
            assertTrue(it.position in area.top..area.bottom)
        }
    }
}
