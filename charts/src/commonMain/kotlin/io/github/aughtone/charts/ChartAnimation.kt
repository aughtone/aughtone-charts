package io.github.aughtone.charts

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween

/**
 * How a chart animates its data in when it first composes, or when its data changes.
 *
 * @see Disabled
 * @see Simple
 * @see Sequenced
 */
sealed class ChartAnimation {
    /** Draws the data immediately, with no animation. */
    object Disabled : ChartAnimation()

    /**
     * Animates every data series together with one shared spec.
     *
     * @param animationSpec Supplies the spec used for all series.
     */
    class Simple(
        val animationSpec: () -> AnimationSpec<Float> = {
            tween(DEFAULT_DURATION, DEFAULT_DELAY)
        },
    ) : ChartAnimation()

    /**
     * Staggers the series, so each one starts after the previous.
     *
     * Not supported by the single-value charts; see the individual chart documentation.
     *
     * @param animationSpec Supplies the spec for a series, given its index.
     */
    class Sequenced(
        val animationSpec: (dataSeriesIndex: Int) -> AnimationSpec<Float> = { index ->
            tween(DEFAULT_DURATION, index * DEFAULT_DELAY)
        },
    ) : ChartAnimation()

    private companion object {
        const val DEFAULT_DURATION = 300
        const val DEFAULT_DELAY = 100
    }
}
