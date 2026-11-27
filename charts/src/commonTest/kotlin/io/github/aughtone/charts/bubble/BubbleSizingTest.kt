package io.github.aughtone.charts.bubble

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertTrue

class BubbleSizingTest {

    private fun bubble(radius: Float) =
        Bubble(name = "b", value = radius, icon = Icons.Filled.WbSunny, color = Color.Red)

    private fun Bubble.scaled(smallest: Float, largest: Float) =
        withRadiusRelativeTo(smallest, largest, minRadiusPossible = MIN, maxRadiusPossible = MAX)

    @Test
    fun distinctRadiiSpanTheAvailableSizes() {
        assertEquals(MIN, bubble(10f).scaled(smallest = 10f, largest = 50f).radius)
        assertEquals(MAX, bubble(50f).scaled(smallest = 10f, largest = 50f).radius)
        assertEquals((MIN + MAX) / 2f, bubble(30f).scaled(smallest = 10f, largest = 50f).radius)
    }

    @Test
    fun aSingleBubbleIsDrawnAtTheMaximumSize() {
        // Regression guard: with one bubble the smallest and largest radius are equal, so scaling
        // across the range divided zero by zero and gave the bubble a NaN radius.
        assertEquals(MAX, bubble(42f).scaled(smallest = 42f, largest = 42f).radius)
    }

    @Test
    fun bubblesSharingOneRadiusAreAllDrawnAtTheMaximumSize() {
        val sizes = List(3) { bubble(7f) }.map { it.scaled(smallest = 7f, largest = 7f).radius }
        assertEquals(listOf(MAX, MAX, MAX), sizes)
    }

    @Test
    fun scalingWorksOnACopyAndLeavesTheCallersBubbleAlone() {
        val original = bubble(10f)
        val scaled = original.scaled(smallest = 10f, largest = 50f)

        assertNotSame(original, scaled)
        assertEquals(10f, original.radius)
    }

    @Test
    fun theSmallestBubbleIsNeverLargerThanTheLargest() {
        // Regression guard: the minimum radius was fixed at 40 while the maximum was a quarter of
        // the chart, so below 160 dp the smallest value got the largest bubble.
        for (size in listOf(60f, 120f, 159f, 160f, 200f, 320f, 640f)) {
            val (min, max) = bubbleRadiusBounds(size)
            assertTrue(min < max, "at $size the bounds were $min..$max")
        }
    }

    @Test
    fun aLargeChartKeepsTheStockBounds() {
        assertEquals(BubbleDefaults.MINIMUM_BUBBLE_RADIUS to 160f, bubbleRadiusBounds(640f))
    }

    private companion object {
        const val MIN = 40f
        const val MAX = 120f
    }
}
