package io.github.aughtone.charts.grid

import androidx.compose.runtime.Immutable

@Immutable
/**
 * One grid line: where it sits, and the data value it marks.
 *
 * @param position Offset within the canvas, in pixels.
 * @param value Data value this line marks, passed to the axis label composable.
 */
data class LineParameters(
    val position: Float,
    val value: Number,
)
