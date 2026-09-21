package io.github.aughtone.charts.bar

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
/**
 * A single bar within a [BarChartCategory].
 *
 * @param x Name of the series this bar belongs to. Entries sharing a name across categories are
 * treated as the same series, and appear once in the legend.
 * @param y Value the bar represents. May be negative.
 * @param color Color the bar is drawn in.
 */
data class BarChartEntry(
    val x: String,
    val y: Float,
    val color: Color
)
