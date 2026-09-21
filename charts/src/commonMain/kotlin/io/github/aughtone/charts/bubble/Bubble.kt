package io.github.aughtone.charts.bubble

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.random.Random

/**
 * One bubble in a [BubbleChart], carrying both its data and its live layout state.
 *
 * [position] and [velocity] are mutated by the packing simulation while the chart settles, so
 * an instance is not safe to share between charts. Layout is seeded randomly and bubbles do not
 * come to rest in the same place twice.
 *
 * @param name Label drawn under the value.
 * @param value Value the bubble represents; drives its size relative to the other bubbles.
 * @param icon Icon drawn above the value.
 * @param color Fill color of the bubble.
 * @param radius Current drawn radius, scaled to the chart while laying out.
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
