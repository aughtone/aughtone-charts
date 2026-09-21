package io.github.aughtone.charts.bubble

import kotlin.math.sqrt

/**
 * A mutable 2D vector used by the bubble chart's packing simulation.
 *
 * Operations mutate the receiver and return it, so calls can be chained. The companion holds
 * the non-mutating alternatives.
 *
 * @param x Horizontal component.
 * @param y Vertical component.
 */
data class Vector(var x: Float, var y: Float) {
    /** Returns the length of this vector. */
    fun mag(): Float {
        return sqrt((magSq().toDouble()).toFloat())
    }

    private fun magSq(): Float {
        return x * x + y * y
    }

    /** Adds [v] to this vector in place, and returns this. */
    fun add(v: Vector): Vector {
        x += v.x
        y += v.y
        return this
    }

    /** Subtracts [v] from this vector in place, and returns this. */
    fun sub(v: Vector): Vector {
        x -= v.x
        y -= v.y
        return this
    }

    /** Replaces both components in place, and returns this. */
    operator fun set(x: Float, y: Float): Vector {
        this.x = x
        this.y = y
        return this
    }

    /** Scales this vector by [n] in place, and returns this. */
    fun mult(n: Float): Vector {
        x *= n
        y *= n
        return this
    }

    /** Divides this vector by [n] in place, and returns this. */
    operator fun div(n: Float): Vector {
        x /= n
        y /= n
        return this
    }

    /** Scales this vector to unit length in place, and returns this. A zero vector is unchanged. */
    fun normalize(): Vector {
        val m = mag()
        if (m != 0f && m != 1f) {
            div(m)
        }
        return this
    }

    /** Caps this vector's length at [max] in place, and returns this. */
    fun limit(max: Float): Vector {
        if (magSq() > max * max) {
            normalize()
            mult(max)
        }
        return this
    }

    /** Sets this vector's length to [len] in place, keeping its direction, and returns this. */
    fun setMag(len: Float): Vector {
        normalize()
        mult(len)
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Vector) {
            return false
        }
        return x == other.x && y == other.y
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    companion object {
        /** Returns `v1 - v2` as a new vector, leaving both operands untouched. */
        fun sub(v1: Vector, v2: Vector): Vector = Vector(v1.x - v2.x, v1.y - v2.y)

        /** Returns the distance between the points [v1] and [v2]. */
        fun dist(v1: Vector, v2: Vector): Float {
            val dx = v1.x - v2.x
            val dy = v1.y - v2.y
            return sqrt((dx * dx + dy * dy).toDouble()).toFloat()
        }
    }
}
