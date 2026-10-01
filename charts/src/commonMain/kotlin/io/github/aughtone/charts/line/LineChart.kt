package io.github.aughtone.charts.line

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import io.github.aughtone.charts.ChartAnimation
import io.github.aughtone.charts.StartAnimation
import io.github.aughtone.charts.grid.GridDefaults
import io.github.aughtone.charts.grid.LineParameters
import io.github.aughtone.charts.grid.YAxisLabels
import io.github.aughtone.charts.grid.alignCenterToOffsetHorizontal
import io.github.aughtone.charts.grid.axisscale.TimestampXAxisScale
import io.github.aughtone.charts.grid.axisscale.YAxisScale
import io.github.aughtone.charts.grid.drawChartGrid
import io.github.aughtone.charts.grid.measureChartGrid
import io.github.aughtone.charts.grid.plotArea
import io.github.aughtone.charts.theme.ChartColors
import io.github.aughtone.charts.theme.ChartTheme

/**
 * The dash pattern used when [LineChartSeries.dashedLine] is set: 5 pixels on, 5 off.
 */
val dashedPathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)

/**
 * Classic line chart with some shade below the line in the same color (albeit with a lot of
 * transparency) as the line and floating balloon on touch/click to show values for that particular
 * x-axis value.
 *
 * Color, shape and whether the line is dashed for each of the lines is specified in the
 * [LegendItemData] class. Even though that this class is used, this particular composable does not
 * show the legend. For this, [LineChartWithLegend] must be used.
 *
 * @param lineChartData Data to portray
 * @param modifier Modifier applied to the chart.
 * @param colors Colors used are [LineChartColors.grid], [LineChartColors.surface] and
 * [LineChartColors.overlayLine].
 * @param xAxisLabel Composable for each label under the time axis. It is given the tick's
 * timestamp as a `Long`, in epoch milliseconds. Draws nothing by default, since the chart cannot
 * know how the reader wants time shown; wrap [GridDefaults.XAxisLabel] around a formatted time to
 * show one.
 * @param yAxisLabel Composable to mark the values on the y-axis.
 * @param overlayHeaderLabel Composable heading the overlay balloon. It is given the timestamp under
 * the cursor as a `Long`, in epoch milliseconds. Draws nothing by default; wrap
 * [GridDefaults.OverlayHeaderLabel] around a formatted time to show one.
 * @param overlayDataEntryLabel Composable to show the value of each line in the overlay balloon
 * for that specific x-axis value
 * @param animation Animation to use
 * @param maxVerticalLines Roughly how many vertical grid lines to draw across the time axis. See
 * [TimestampXAxisScale] for how their times are chosen.
 * @param maxHorizontalLines Roughly how many horizontal grid lines to draw. Their spacing is rounded
 * to 1, 2 or 5 times a power of ten, so there can be a few more. See [YAxisScale].
 * @param roundMinMaxClosestTo Multiple the y-axis bounds are rounded out to: the minimum down and
 * the maximum up, so the axis always contains the data.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LineChart(
    lineChartData: LineChartData,
    modifier: Modifier = Modifier,
    colors: LineChartColors = ChartTheme.colors.lineChartColors,
    xAxisLabel: @Composable (value: Any) -> Unit = GridDefaults.NoLabel,
    yAxisLabel: @Composable (value: Any) -> Unit = GridDefaults.YAxisLabel,
    overlayHeaderLabel: @Composable (value: Any) -> Unit = GridDefaults.NoLabel,
    overlayDataEntryLabel: @Composable (dataName: String, value: Any) -> Unit = GridDefaults.OverlayDataEntryLabel,
    animation: ChartAnimation = ChartAnimation.Simple(),
    maxVerticalLines: Int = GridDefaults.NUMBER_OF_GRID_LINES,
    maxHorizontalLines: Int = GridDefaults.NUMBER_OF_GRID_LINES,
    roundMinMaxClosestTo: Int = GridDefaults.ROUND_MIN_MAX_CLOSEST_TO,
) {
    var touchPositionX by remember { mutableStateOf(-1f) }
    var verticalGridLines by remember { mutableStateOf(emptyList<LineParameters>()) }
    var horizontalGridLines by remember { mutableStateOf(emptyList<LineParameters>()) }
    val horizontalLinesOffset: Dp = GridDefaults.HORIZONTAL_LINES_OFFSET

    val animationPlayed = StartAnimation(animation, lineChartData)

    val alpha = when (animation) {
        ChartAnimation.Disabled -> lineChartData.series.indices.map { 1f }
        is ChartAnimation.Simple -> lineChartData.series.indices.map {
            animateFloatAsState(
                targetValue = if (animationPlayed) 1f else 0f,
                animationSpec = animation.animationSpec()
            ).value
        }
        is ChartAnimation.Sequenced -> lineChartData.series.indices.map {
            animateFloatAsState(
                targetValue = if (animationPlayed) 1f else 0f,
                animationSpec = animation.animationSpec(it)
            ).value
        }
    }

    Row(modifier = modifier) {
        YAxisLabels(
            horizontalGridLines = horizontalGridLines,
            yAxisMarkerLayout = yAxisLabel,
        )

        Spacer(modifier = Modifier.size(4.dp, 0.dp))

        // main chart
        Column(Modifier.fillMaxSize()) {
            BoxWithConstraints(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .drawBehind {
                        // One scale for the grid and the line, so the line sits against its own labels.
                        val yAxisScale = YAxisScale(
                            min = lineChartData.minY,
                            max = lineChartData.maxY,
                            maxTickCount = maxHorizontalLines - 1,
                            roundClosestTo = roundMinMaxClosestTo,
                        )
                        val lines = measureChartGrid(
                            xAxisScale = TimestampXAxisScale(
                                min = lineChartData.minX,
                                max = lineChartData.maxX,
                                maxTicksCount = maxVerticalLines - 1

                            ),
                            yAxisScale = yAxisScale,
                            horizontalLinesOffset = horizontalLinesOffset
                        )
                        verticalGridLines = lines.verticalLines
                        horizontalGridLines = lines.horizontalLines
                        drawChartGrid(lines, colors.grid)

                        drawLineChart(
                            lineChartData = lineChartData,
                            yAxisScale = yAxisScale,
                            area = plotArea(size.height, horizontalLinesOffset.toPx()),
                            alpha = alpha,
                        )
                    }
                    // Touch input
                    .pointerInput(Unit) {
                        while (true) {
                            awaitPointerEventScope {
                                val event = awaitPointerEvent(pass = PointerEventPass.Initial)

                                touchPositionX = if (
                                    shouldIgnoreTouchInput(
                                        event = event,
                                        containerSize = size
                                    )
                                ) {
                                    -1f
                                } else {
                                    event.changes[0].position.x
                                }

                                event.changes.any {
                                    it.consume()
                                    true
                                }
                            }
                        }
                    }
            ) {
                // Overlay
                LineChartOverlayInformation(
                    lineChartData = lineChartData,
                    positionX = touchPositionX,
                    containerSize = with(LocalDensity.current) {
                        Size(
                            maxWidth.toPx(),
                            maxHeight.toPx()
                        )
                    },
                    colors = colors,
                    overlayHeaderLayout = overlayHeaderLabel,
                    overlayDataEntryLayout = overlayDataEntryLabel,
                )
            }

            Box(Modifier.fillMaxWidth()) {
                for (gridLine in verticalGridLines) {
                    Box(
                        modifier = Modifier
                            .alignCenterToOffsetHorizontal(gridLine.position)
                    ) {
                        xAxisLabel(gridLine.value.toLong())
                    }
                }
            }
        }
    }
}

private fun shouldIgnoreTouchInput(event: PointerEvent, containerSize: IntSize): Boolean {
    if (event.changes.isEmpty() ||
        event.type != PointerEventType.Move
    ) {
        return true
    }
    if (event.changes[0].position.x < 0 ||
        event.changes[0].position.x > containerSize.width
    ) {
        return true
    }
    if (event.changes[0].position.y < 0 ||
        event.changes[0].position.y > containerSize.height
    ) {
        return true
    }
    return false
}
