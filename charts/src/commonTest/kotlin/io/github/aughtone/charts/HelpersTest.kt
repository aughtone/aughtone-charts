package io.github.aughtone.charts

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HelpersTest {

    @Test
    fun floatMapsLinearlyBetweenRanges() {
        assertEquals(0f, 0f.mapValueToDifferentRange(0f, 10f, 0f, 100f))
        assertEquals(50f, 5f.mapValueToDifferentRange(0f, 10f, 0f, 100f))
        assertEquals(100f, 10f.mapValueToDifferentRange(0f, 10f, 0f, 100f))
    }

    @Test
    fun floatMapsOntoAnInvertedRange() {
        // Canvas y-axes grow downwards, so the output range is routinely reversed.
        assertEquals(100f, 0f.mapValueToDifferentRange(0f, 10f, 100f, 0f))
        assertEquals(0f, 10f.mapValueToDifferentRange(0f, 10f, 100f, 0f))
    }

    @Test
    fun floatExtrapolatesOutsideTheInputRange() {
        assertEquals(-50f, (-5f).mapValueToDifferentRange(0f, 10f, 0f, 100f))
        assertEquals(150f, 15f.mapValueToDifferentRange(0f, 10f, 0f, 100f))
    }

    @Test
    fun doubleMapsLinearlyBetweenRanges() {
        assertEquals(50.0, 5.0.mapValueToDifferentRange(0.0, 10.0, 0.0, 100.0))
        assertEquals(-25.0, (-5.0).mapValueToDifferentRange(-10.0, 10.0, -50.0, 50.0))
    }

    @Test
    fun longMapsWithIntegerTruncation() {
        // Integer division truncates: 5/10 of the output range is 50, but 1/3 is not 33.
        assertEquals(50L, 5L.mapValueToDifferentRange(0L, 10L, 0L, 100L))
        assertEquals(33L, 1L.mapValueToDifferentRange(0L, 3L, 0L, 100L))
    }

    @Test
    fun longMapsToFloatWithoutTruncation() {
        // Kotlin/JS has no distinct Float, so this is 33.333332 on JVM and
        // 33.333333333333336 on JS. Assert within tolerance rather than exactly.
        val actual = 1L.mapValueToDifferentRange(0L, 3L, 0f, 100f)
        assertTrue(abs(actual - 33.3333f) < 0.001f, "was $actual")
    }

    @Test
    fun degenerateInputRangeProducesNonFiniteResult() {
        // inMax == inMin divides by zero. Documented so a future guard is a deliberate change.
        assertTrue(5f.mapValueToDifferentRange(1f, 1f, 0f, 100f).isNaN().not())
        assertTrue(5f.mapValueToDifferentRange(1f, 1f, 0f, 100f).isInfinite())
    }

    @Test
    fun roundFormatsToTwoDecimalsByDefault() {
        assertEquals("1.23", 1.2345.round())
        assertEquals("-1.23", (-1.2345).round())
    }

    @Test
    fun roundRendersIntegralValuesIdenticallyOnEveryPlatform() {
        // Regression guard: Kotlin/JS has no distinct Int or Float at runtime, so a whole
        // number previously labelled "1.0" on JVM/Native and "1" in a browser.
        assertEquals("1", 1.0.round())
        assertEquals("-1", (-1.0).round())
        assertEquals("0", 0.0.round())
        assertEquals("42", 42.0.round())
    }

    @Test
    fun roundHonoursTheRequestedPrecision() {
        assertEquals("1.2", 1.2345.round(1))
        assertEquals("1.235", 1.2345.round(3))
    }

    @Test
    fun roundPassesIntegralTypesThroughUnchanged() {
        assertEquals("42", 42.round())
        assertEquals("42", 42L.round())
    }
}
