package io.github.aughtone.charts.line

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals

class LineChartDataTest {

    private fun series(vararg points: Pair<Long, Float>) = LineChartSeries(
        dataName = "series",
        lineColor = Color.Red,
        listOfPoints = points.map { LineChartPoint(x = it.first, y = it.second) },
    )

    @Test
    fun anEmptySeriesDoesNotWidenTheAxes() {
        // Regression guard: an empty series reported timestamp 0 and value 0, so beside real data
        // it stretched the time axis back to the epoch and pulled the y-axis down to zero.
        val data = LineChartData(
            listOf(series(1_000_000L to 15f, 2_000_000L to 25f), series()),
        )

        assertEquals(1_000_000L, data.minX)
        assertEquals(2_000_000L, data.maxX)
        assertEquals(15f, data.minY)
        assertEquals(25f, data.maxY)
    }

    @Test
    fun onlyEmptySeriesGiveZeroBounds() {
        val data = LineChartData(listOf(series(), series()))

        assertEquals(0L, data.minX)
        assertEquals(0L, data.maxX)
        assertEquals(0f, data.minY)
        assertEquals(0f, data.maxY)
    }
}
