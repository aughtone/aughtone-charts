package com.netguru.multiplatform.charts.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Contains the default values used by all charts
 */
object ChartDefaults {
    @Composable
    fun chartColors(
        primary: Color = MaterialTheme.colorScheme.primary,
        surface: Color = Color.Unspecified,
        grid: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
        emptyGasBottle: Color = MaterialTheme.colorScheme.error,
        fullGasBottle: Color = MaterialTheme.colorScheme.primary,
        overlayLine: Color = MaterialTheme.colorScheme.error,
    ) = ChartColors(
        primary = primary,
        surface = surface,
        grid = grid,
        emptyGasBottle = emptyGasBottle,
        fullGasBottle = fullGasBottle,
        overlayLine = overlayLine,
    )
}
