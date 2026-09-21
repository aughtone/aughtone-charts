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

/**
 * Linearly maps this value from one range onto another.
 *
 * Used to turn data values into canvas coordinates. The output range may be inverted
 * (`outMin` greater than `outMax`), which is how values map onto a downward-growing y-axis.
 * Values outside the input range extrapolate rather than clamp.
 *
 * @param inMin Lower bound of the range this value is in.
 * @param inMax Upper bound of the range this value is in. Equal bounds divide by zero.
 * @param outMin Value returned when this equals [inMin].
 * @param outMax Value returned when this equals [inMax].
 */
fun Float.mapValueToDifferentRange(
    inMin: Float,
    inMax: Float,
    outMin: Float,
    outMax: Float,
) = (this - inMin) * (outMax - outMin) / (inMax - inMin) + outMin

/**
 * Linearly maps this value onto another range, in whole units.
 *
 * The arithmetic is integer division and truncates; use the [Float] output overload where the
 * fractional part matters.
 *
 * @param inMin Lower bound of the range this value is in.
 * @param inMax Upper bound of the range this value is in. Equal bounds divide by zero.
 * @param outMin Value returned when this equals [inMin].
 * @param outMax Value returned when this equals [inMax].
 */
fun Long.mapValueToDifferentRange(
    inMin: Long,
    inMax: Long,
    outMin: Long,
    outMax: Long,
) = (this - inMin) * (outMax - outMin) / (inMax - inMin) + outMin

/**
 * Linearly maps this value onto a floating-point range, keeping the fractional part.
 *
 * @param inMin Lower bound of the range this value is in.
 * @param inMax Upper bound of the range this value is in. Equal bounds divide by zero.
 * @param outMin Value returned when this equals [inMin].
 * @param outMax Value returned when this equals [inMax].
 */
fun Long.mapValueToDifferentRange(
    inMin: Long,
    inMax: Long,
    outMin: Float,
    outMax: Float,
) = (this - inMin) * (outMax - outMin) / (inMax - inMin) + outMin

/**
 * Formats this number as a label, rounded to [decimals] places.
 *
 * A trailing `.0` is dropped, so a whole number renders as `1` rather than `1.0`. This is
 * deliberate: Kotlin/JS has no distinct `Int` or `Float` at runtime, and dropping the suffix is
 * the only form that renders identically on every target. Integral types are returned unchanged.
 *
 * @param decimals Number of decimal places to keep.
 * @return The formatted value, or `"-"` if this is not a finite number.
 */
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
