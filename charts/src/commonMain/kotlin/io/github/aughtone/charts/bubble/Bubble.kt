package io.github.aughtone.charts.bubble

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.random.Random

/**
 * One bubble in a [BubbleChart].
 *
 * [BubbleChart] lays out copies of the bubbles it is given, so an instance passed to a chart is
 * never moved and can be shared between charts. Its [position] and [velocity] are the packing
 * simulation's working state and stay at their initial values on an instance you hold: [position]
 * does not tell you where the bubble was drawn.
 *
 * @param name Label drawn under the value.
 * @param value Number shown in the bubble's default label. It sets the size only through
 * [radius], which defaults to it.
 * @param icon Icon drawn above the value.
 * @param color Fill color of the bubble.
 * @param radius Size of the bubble relative to the others in the same chart; defaults to [value].
 * [BubbleChart] scales every radius to fit the space it has, so only the ratios matter.
 */
data class Bubble(
    val name: String,
    val value: Float,
    val icon: ImageVector,
    val color: Color,
    var radius: Float = value
) {
    var position: Vector = Vector(0f, 0f)
    var velocity: Vector = Vector(
        Random.nextFloat() - 0.5f,
        Random.nextFloat() - 0.5f
    ).normalize()
    private var acceleration: Vector = Vector(0f, 0f)

    /** Accumulates [force] into this bubble's acceleration for the next [update]. */
    fun applyForce(force: Vector) {
        acceleration.add(force)
    }

    /** Advances the bubble one simulation step and clears the accumulated force. */
    fun update() {
        velocity.add(acceleration)
        position.add(velocity)
        acceleration.mult(0f)
    }
}
