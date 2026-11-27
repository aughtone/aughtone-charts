package io.github.aughtone.charts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import io.github.aughtone.charts.pie.PieChartData
import io.github.aughtone.charts.pie.animatedPieSweep
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class AnimationTest {

    private fun pie(vararg values: Double) = values.map { PieChartData("slice", it, Color.Red) }

    @Test
    fun anAnimationPlaysAgainWhenTheDataChanges() = runComposeUiTest {
        // Regression guard: the played flag was keyed on the data but the effect that set it ran
        // once, so new data reset the flag and nothing set it again. The chart stayed animated out.
        var data by mutableStateOf(listOf(1f, 2f))
        var played = false
        setContent { played = StartAnimation(ChartAnimation.Simple(), data) }

        waitForIdle()
        assertTrue(played, "the animation did not play on first composition")

        data = listOf(3f, 4f)
        waitForIdle()
        assertTrue(played, "the animation did not play again after the data changed")
    }

    @Test
    fun aDisabledPieDrawsTheFullCircle() = runComposeUiTest {
        // Regression guard: with animation disabled the segments shared one degree, not 360.
        var sweep = 0f
        setContent { sweep = animatedPieSweep(ChartAnimation.Disabled, pie(1.0, 2.0)) }

        waitForIdle()
        assertEquals(360f, sweep)
    }

    @Test
    fun anAnimatedPieEndsOnTheFullCircle() = runComposeUiTest {
        var sweep = 0f
        setContent { sweep = animatedPieSweep(ChartAnimation.Simple(), pie(1.0, 2.0)) }

        waitForIdle()
        assertEquals(360f, sweep)
    }

    @Test
    fun anAnimatedPieEndsOnTheFullCircleAfterItsDataChanges() = runComposeUiTest {
        var data by mutableStateOf(pie(1.0, 2.0))
        var sweep = 0f
        setContent { sweep = animatedPieSweep(ChartAnimation.Simple(), data) }

        waitForIdle()
        data = pie(3.0, 4.0)
        waitForIdle()
        assertEquals(360f, sweep)
    }
}
