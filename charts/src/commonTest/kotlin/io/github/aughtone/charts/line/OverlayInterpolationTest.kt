package io.github.aughtone.charts.line

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals

class OverlayInterpolationTest {

    private val data = LineChartData(
        listOf(
            LineChartSeries(
                dataName = "series",
                lineColor = Color.Red,
                listOfPoints = listOf(
                    LineChartPoint(x = 0L, y = 1f),
                    LineChartPoint(x = 1_000L, y = 3f),
                    LineChartPoint(x = 2_000L, y = 5f),
                ),
            ),
        ),
    )

    @Test
    fun aCursorBetweenSamplesInterpolates() {
        assertEquals(2f, retrieveData(data, timestampCursor = 500L).single().interpolatedValue)
    }

    @Test
    fun aCursorExactlyOnASampleReportsThatSample() {
        // Regression guard: the samples either side of the cursor were the same sample, so the
        // interpolation divided 0 by 0 and the overlay showed NaN. On a chart with a single
        // timestamp that happened on every touch.
        assertEquals(3f, retrieveData(data, timestampCursor = 1_000L).single().interpolatedValue)
    }
}
