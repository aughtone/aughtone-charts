package io.github.aughtone.charts.bar

import androidx.compose.runtime.Immutable

@Immutable
data class BarChartCategory(
    val name: String,
    val entries: List<BarChartEntry>
) {
    // A category filtered down to nothing is a legitimate input, so fall back to zero rather
    // than letting minOf/maxOf throw on an empty list.
    val minY: Float
        get() = entries.minOfOrNull { it.y } ?: 0f
    val maxY: Float
        get() = entries.maxOfOrNull { it.y } ?: 0f
}
