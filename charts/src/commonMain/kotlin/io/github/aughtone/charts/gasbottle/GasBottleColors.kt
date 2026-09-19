package io.github.aughtone.charts.gasbottle

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import io.github.aughtone.charts.theme.ChartColors

@Immutable
data class GasBottleColors(
    val fullGasBottle: Color,
    val emptyGasBottle: Color,
)

val ChartColors.gasBottleColors
    get() = GasBottleColors(
        fullGasBottle = fullGasBottle,
        emptyGasBottle = emptyGasBottle,
    )
