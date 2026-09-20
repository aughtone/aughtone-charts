package io.github.aughtone.charts.bar

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals

class BarChartDataTest {

    private fun entry(x: String, y: Float) = BarChartEntry(x = x, y = y, color = Color.Red)

    private fun category(name: String, vararg ys: Pair<String, Float>) =
        BarChartCategory(name = name, entries = ys.map { entry(it.first, it.second) })

    @Test
    fun emptyDataFallsBackToAUnitRange() {
        val data = BarChartData(categories = emptyList())

        assertEquals(0L, data.minX)
        assertEquals(1L, data.maxX)
        assertEquals(0f, data.minY)
        assertEquals(1f, data.maxY)
    }

    @Test
    fun maxXIsTwoSlotsPerCategory() {
        // Each category occupies a pair of grid lines, which is what BarChart's label
        // placement relies on when it takes every second vertical line.
        val data = BarChartData(
            categories = listOf(
                category("a", "one" to 1f),
                category("b", "one" to 2f),
                category("c", "one" to 3f),
            ),
        )

        assertEquals(6L, data.maxX)
    }

    @Test
    fun yRangeAlwaysIncludesZero() {
        val allPositive = BarChartData(
            categories = listOf(category("a", "one" to 5f, "two" to 9f)),
        )
        assertEquals(0f, allPositive.minY)
        assertEquals(9f, allPositive.maxY)

        val allNegative = BarChartData(
            categories = listOf(category("a", "one" to -5f, "two" to -9f)),
        )
        assertEquals(-9f, allNegative.minY)
        assertEquals(0f, allNegative.maxY)
    }

    @Test
    fun yRangeSpansEveryCategory() {
        val data = BarChartData(
            categories = listOf(
                category("a", "one" to -3f),
                category("b", "one" to 7f),
            ),
        )

        assertEquals(-3f, data.minY)
        assertEquals(7f, data.maxY)
    }

    @Test
    fun legendDeduplicatesEntriesAcrossCategories() {
        val data = BarChartData(
            categories = listOf(
                category("a", "series one" to 1f, "series two" to 2f),
                category("b", "series one" to 3f, "series two" to 4f),
            ),
        )

        assertEquals(listOf("series one", "series two"), data.legendData.map { it.name })
    }

    @Test
    fun aCategoryWithNoEntriesContributesZero() {
        // Regression guard: minOf/maxOf over an empty list used to throw NoSuchElementException,
        // so a category filtered down to nothing crashed the chart.
        val data = BarChartData(categories = listOf(BarChartCategory("empty", emptyList())))

        assertEquals(0f, data.minY)
        assertEquals(0f, data.maxY)
    }

    @Test
    fun anEmptyCategoryDoesNotSuppressOthers() {
        val data = BarChartData(
            categories = listOf(
                BarChartCategory("empty", emptyList()),
                category("b", "one" to 7f),
            ),
        )

        assertEquals(0f, data.minY)
        assertEquals(7f, data.maxY)
    }
}
