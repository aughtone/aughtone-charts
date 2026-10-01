package io.github.aughtone.charts.line

import androidx.compose.ui.graphics.Color
import io.github.aughtone.charts.grid.PlotArea
import io.github.aughtone.charts.grid.axisscale.YAxisScale
import io.github.aughtone.charts.grid.measureHorizontalLines
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LineMappingTest {

    private val area = PlotArea(top = 10f, bottom = 90f)

    private fun series(vararg points: Pair<Long, Float>) = LineChartSeries(
        dataName = "series",
        lineColor = Color.Red,
        listOfPoints = points.map { LineChartPoint(x = it.first, y = it.second) },
    )

    private fun scaleFor(data: LineChartData) =
        YAxisScale(min = data.minY, max = data.maxY, maxTickCount = 4, roundClosestTo = 10)

    private fun map(series: LineChartSeries): List<PointF> {
        val data = LineChartData(listOf(series))
        return mapDataToPixels(data, series, scaleFor(data), area, width = 200f)
    }

    @Test
    fun aSingleTimestampIsPlacedInTheMiddle() {
        // Regression guard: x was mapped with integer division over maxX - minX, so a series
        // with one timestamp threw instead of drawing.
        val points = map(series(1_000L to 5f))

        assertEquals(100f, points.single().x)
    }

    @Test
    fun aConstantSeriesMapsToFinitePositions() {
        // Regression guard: a series whose value never changed mapped y through 0 / 0.
        val points = map(series(0L to 5f, 1_000L to 5f, 2_000L to 5f))

        assertTrue(points.all { it.y.isFinite() }, "y positions were ${points.map { it.y }}")
    }

    @Test
    fun anAllPositiveSeriesStaysInsideThePlotArea() {
        // Regression guard: the axis for 15..25 used to be 20..30, so the lowest points were
        // drawn below the chart.
        val points = map(series(0L to 15f, 1_000L to 25f, 2_000L to 20f))

        assertTrue(
            points.all { it.y in area.top..area.bottom },
            "y positions were ${points.map { it.y }}, area ${area.top}..${area.bottom}",
        )
    }

    @Test
    fun aPointOnAGridValueSitsExactlyOnThatGridLine() {
        // Regression guard: the grid was drawn against the rounded axis range but the line against
        // the raw data range, so labels and line disagreed. Both now use one mapping.
        val data = LineChartData(listOf(series(0L to 15f, 1_000L to 20f, 2_000L to 25f)))
        val scale = scaleFor(data)
        val gridLineAt20 = measureHorizontalLines(scale, area).single { it.value == 20f }

        val pointAt20 = mapDataToPixels(data, data.series.single(), scale, area, width = 200f)[1]

        assertEquals(gridLineAt20.position, pointAt20.y)
    }

    @Test
    fun timestampsSpanTheFullWidth() {
        val points = map(series(0L to 1f, 1_000L to 2f, 2_000L to 3f))

        assertEquals(listOf(0f, 100f, 200f), points.map { it.x })
    }
}
