package io.github.aughtone.charts.gasbottle

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import io.github.aughtone.charts.theme.ChartColors

@Immutable
/**
 * The colors [GasBottle] draws with. The fill is interpolated between the two as the value moves.
 *
 * @param fullGasBottle Color at 100 percent.
 * @param emptyGasBottle Color at 0 percent.
 */
data class GasBottleColors(
    val fullGasBottle: Color,
    val emptyGasBottle: Color,
)

/** Narrows a full [ChartColors] palette to the subset [GasBottle] uses. */
val ChartColors.gasBottleColors
    get() = GasBottleColors(
        fullGasBottle = fullGasBottle,
        emptyGasBottle = emptyGasBottle,
    )
