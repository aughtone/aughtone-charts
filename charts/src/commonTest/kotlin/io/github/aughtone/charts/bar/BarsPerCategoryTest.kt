package io.github.aughtone.charts.bar

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals

class BarsPerCategoryTest {

    private fun category(bars: Int) =
        BarChartCategory(name = "c", entries = List(bars) { BarChartEntry(x = "e$it", y = 1f, color = Color.Red) })

    @Test
    fun noCategoriesMeansNoBars() {
        // Regression guard: the count came from the first category, so a chart with none threw.
        assertEquals(0, BarChartData(categories = emptyList()).barsPerCategory())
    }

    @Test
    fun theWidestCategorySetsTheCount() {
        // Regression guard: a later category with more bars than the first indexed past the end
        // of the animation list sized from the first.
        assertEquals(3, BarChartData(listOf(category(1), category(3), category(2))).barsPerCategory())
    }

    @Test
    fun anEmptyFirstCategoryDoesNotHideTheOthers() {
        assertEquals(2, BarChartData(listOf(category(0), category(2))).barsPerCategory())
    }
}
