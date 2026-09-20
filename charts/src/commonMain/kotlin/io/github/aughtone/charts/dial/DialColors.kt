package io.github.aughtone.charts.dial

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import io.github.aughtone.charts.theme.ChartColors

@Immutable
/**
 * The colors [Dial] and [PercentageDial] draw with.
 *
 * @param progressBarColor Color of the arc representing the value.
 * @param progressBarBackgroundColor Color of the remainder of the arc.
 * @param gridScaleColor Color of the scale lines around the arc.
 */
data class DialColors(
    val progressBarColor: Color,
    val progressBarBackgroundColor: Color,
    val gridScaleColor: Color,
)

/** Narrows a full [ChartColors] palette to the subset the dial charts use. */
val ChartColors.dialColors
    get() = DialColors(
        progressBarColor = primary,
        progressBarBackgroundColor = grid,
        gridScaleColor = grid
    )
