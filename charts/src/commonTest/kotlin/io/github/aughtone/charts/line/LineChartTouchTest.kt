package io.github.aughtone.charts.line

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import io.github.aughtone.charts.ChartAnimation
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class LineChartTouchTest {

    @Test
    fun everyPointerOfATouchIsConsumed() = runComposeUiTest {
        // Regression guard: the chart consumed only the first pointer change of each event, so a
        // second finger reached the containers around it.
        val seenByParent = mutableListOf<Boolean>()
        val data = LineChartData(
            listOf(
                LineChartSeries(
                    dataName = "series",
                    lineColor = Color.Red,
                    listOfPoints = listOf(LineChartPoint(0L, 1f), LineChartPoint(1_000L, 2f)),
                ),
            ),
        )
        setContent {
            Box(
                Modifier
                    .size(300.dp)
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent(PointerEventPass.Main)
                                if (event.type == PointerEventType.Move && event.changes.size > 1) {
                                    seenByParent += event.changes.map { it.isConsumed }
                                }
                            }
                        }
                    },
            ) {
                LineChart(
                    lineChartData = data,
                    modifier = Modifier.fillMaxSize().testTag("chart"),
                    colors = LineChartColors(Color.Gray, Color.White, Color.Red),
                    animation = ChartAnimation.Disabled,
                )
            }
        }

        onNodeWithTag("chart").performTouchInput {
            down(0, Offset(100f, 100f))
            down(1, Offset(150f, 100f))
            moveBy(0, Offset(10f, 0f))
            moveBy(1, Offset(10f, 0f))
            up(0)
            up(1)
        }
        waitForIdle()

        assertTrue(seenByParent.isNotEmpty(), "no two-pointer move reached the parent")
        assertTrue(seenByParent.all { it }, "the parent saw unconsumed changes: $seenByParent")
    }
}
