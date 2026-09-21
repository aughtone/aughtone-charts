package io.github.aughtone.charts.bar

import androidx.compose.runtime.Immutable

@Immutable
/**
 * One cluster of bars drawn together under a single label on the x-axis.
 *
 * @param name Label drawn under the cluster on the x-axis.
 * @param entries The bars in this cluster. May be empty, in which case the category
 * contributes zero to the chart's y-axis range.
 */
data class BarChartCategory(
    val name: String,
    val entries: List<BarChartEntry>
) {
    // A category filtered down to nothing is a legitimate input, so fall back to zero rather
    // than letting minOf/maxOf throw on an empty list.
    /** Lowest value among [entries], or `0f` when there are none. */
    val minY: Float
        get() = entries.minOfOrNull { it.y } ?: 0f
    /** Highest value among [entries], or `0f` when there are none. */
    val maxY: Float
        get() = entries.maxOfOrNull { it.y } ?: 0f
}
