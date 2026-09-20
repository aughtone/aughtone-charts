package io.github.aughtone.charts

import io.github.aughtone.charts.bar.BarChartDefaults
import io.github.aughtone.charts.bubble.BubbleDefaults
import io.github.aughtone.charts.dial.DialDefaults
import io.github.aughtone.charts.grid.GridDefaults
import io.github.aughtone.charts.pie.PieDefaults
import io.github.aughtone.charts.theme.ChartDefaults
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Every chart composable takes its labels as parameters defaulted to one of the `*Defaults`
 * objects, so a caller can replace a label outright. Replacing is not the same as adjusting:
 * to render "the stock Y-axis label with three decimals" a caller must be able to reference
 * the default it is varying, otherwise every visual property has to be copied out of this
 * library's source and then kept in step with it by hand.
 *
 * These objects are the defaults named in the public signatures, so they belong to the public
 * API. This test fails while any of them is `internal`.
 *
 * Note this can only run on JVM: kotlin-reflect reads Kotlin visibility, which bytecode alone
 * does not carry. It is also why the assertion lives here rather than in commonTest, where
 * `internal` is visible anyway and the problem would be invisible.
 */
class PublicApiTest {

    private fun assertPublic(type: KClass<*>) {
        assertEquals(
            KVisibility.PUBLIC,
            type.visibility,
            "${type.simpleName} is part of the public API surface: it is the declared default " +
                "for a parameter callers can pass, so callers must be able to reference it.",
        )
    }

    @Test
    fun gridDefaultsIsPublic() = assertPublic(GridDefaults::class)

    @Test
    fun barChartDefaultsIsPublic() = assertPublic(BarChartDefaults::class)

    @Test
    fun bubbleDefaultsIsPublic() = assertPublic(BubbleDefaults::class)

    @Test
    fun dialDefaultsIsPublic() = assertPublic(DialDefaults::class)

    @Test
    fun pieDefaultsIsPublic() = assertPublic(PieDefaults::class)

    @Test
    fun chartDefaultsIsPublic() = assertPublic(ChartDefaults::class)
}
