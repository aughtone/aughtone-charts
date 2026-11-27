package io.github.aughtone.charts.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalTestApi::class)
class ChartDefaultsTest {

    @Test
    fun theDefaultPaletteGivesTheTooltipABackground() = runComposeUiTest {
        // Regression guard: surface defaulted to Color.Unspecified, so the touch tooltip drew its
        // text with no background even with the default palette provided.
        var colors: ChartColors? = null
        var themeSurface = Color.Unspecified
        setContent {
            MaterialTheme {
                colors = ChartDefaults.chartColors()
                themeSurface = MaterialTheme.colorScheme.surfaceContainerHigh
            }
        }

        waitForIdle()
        assertNotEquals(Color.Unspecified, colors!!.surface)
        assertEquals(themeSurface, colors!!.surface)
    }
}
