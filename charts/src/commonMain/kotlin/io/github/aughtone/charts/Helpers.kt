package io.github.aughtone.charts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.math.pow
import kotlin.math.roundToInt

internal fun Double.mapValueToDifferentRange(
    inMin: Double,
    inMax: Double,
    outMin: Double,
    outMax: Double,
) = (this - inMin) * (outMax - outMin) / (inMax - inMin) + outMin

fun Float.mapValueToDifferentRange(
    inMin: Float,
    inMax: Float,
    outMin: Float,
    outMax: Float,
) = (this - inMin) * (outMax - outMin) / (inMax - inMin) + outMin

fun Long.mapValueToDifferentRange(
    inMin: Long,
    inMax: Long,
    outMin: Long,
    outMax: Long,
) = (this - inMin) * (outMax - outMin) / (inMax - inMin) + outMin

fun Long.mapValueToDifferentRange(
    inMin: Long,
    inMax: Long,
    outMin: Float,
    outMax: Float,
) = (this - inMin) * (outMax - outMin) / (inMax - inMin) + outMin

fun Number.round(decimals: Int = 2): String {
    return when (this) {
        is Double,
        is Float,
        -> try {
            ((this.toDouble() * 10.0.pow(decimals)).roundToInt() / 10.0.pow(decimals))
                .toString()
                .withoutTrailingZero()
        } catch (e: IllegalArgumentException) {
            "-"
        }
        else -> {
            this.toString()
        }
    }
}

/**
 * Drops a trailing `.0` so a whole number labels identically on every target.
 *
 * Kotlin/JS has no distinct Int or Float at runtime: `42` arrives here as a Double and takes the
 * floating-point branch above, where the JVM would have passed it through as an Int. Normalising
 * towards `1` rather than `1.0` is the only direction that agrees on both, and it reads better on
 * an axis. Exponent and non-finite forms are untouched.
 */
private fun String.withoutTrailingZero(): String =
    if (endsWith(".0")) dropLast(2) else this

@Composable
internal fun StartAnimation(animation: ChartAnimation, data: Any): Boolean {
    var animationPlayed by remember(data) {
        mutableStateOf(animation is ChartAnimation.Disabled)
    }
    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    return animationPlayed
}
