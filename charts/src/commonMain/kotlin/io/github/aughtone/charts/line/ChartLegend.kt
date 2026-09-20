package io.github.aughtone.charts.line

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import io.github.aughtone.charts.ChartAnimation
import io.github.aughtone.charts.bar.BarChartConfig
import io.github.aughtone.charts.grid.GridDefaults

@Composable
/**
 * A standalone legend, laid out as an adaptive grid.
 *
 * The chart composables ending in `WithLegend` draw this themselves; use it directly to place a
 * legend somewhere else in your own layout.
 *
 * @param legendData Entries to show, typically [GridChartData.legendData].
 * @param modifier Modifier applied to the legend.
 * @param animation Animation used to fade the entries in.
 * @param config Supplies the corner radius of each entry's color symbol.
 * @param legendItemLabel Composable to show for each entry. The color symbol drawn to its left
 * is not customizable.
 */
fun ChartLegend(
    legendData: List<LegendItemData>,
    modifier: Modifier = Modifier,
    animation: ChartAnimation = ChartAnimation.Simple(),
    config: BarChartConfig = BarChartConfig(),
    legendItemLabel: @Composable (String) -> Unit = GridDefaults.LegendItemLabel,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(200.dp),
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
    ) {
        items(legendData.count()) { index ->
            LegendItem(
                data = legendData[index],
                index = index,
                animation = animation,
                config = config,
                legendItemLabel = legendItemLabel,
            )
        }
    }
}

@Composable
private fun LegendItem(
    data: LegendItemData,
    index: Int,
    animation: ChartAnimation,
    config: BarChartConfig,
    legendItemLabel: @Composable (String) -> Unit,
) {
    var animationPlayed by remember(animation) {
        mutableStateOf(animation is ChartAnimation.Disabled)
    }

    LaunchedEffect(key1 = true) {
        animationPlayed = true // to play animation only once
    }

    val alpha = when (animation) {
        ChartAnimation.Disabled -> 1f
        is ChartAnimation.Simple -> animateFloatAsState(
            targetValue = if (animationPlayed) 1f else 0f,
            animationSpec = animation.animationSpec(),
        ).value
        is ChartAnimation.Sequenced -> animateFloatAsState(
            targetValue = if (animationPlayed) 1f else 0f,
            animationSpec = animation.animationSpec(index),
        ).value
    }

    Row(modifier = Modifier.alpha(alpha), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(data.selectSymbolSize())
                .drawBehind {
                    when (data.symbolShape) {
                        SymbolShape.LINE ->
                            drawLine(
                                strokeWidth = size.height,
                                pathEffect = if (data.dashed) dashedPathEffect else null,
                                color = data.color,
                                start = Offset(0f, size.height / 2),
                                end = Offset(size.width, size.height / 2)
                            )
                        SymbolShape.RECTANGLE ->
                            drawRoundRect(
                                color = data.color,
                                cornerRadius = CornerRadius(
                                    config.cornerRadius.toPx(),
                                    config.cornerRadius.toPx(),
                                )
                            )
                    }
                }
        )

        Spacer(modifier = Modifier.width(8.dp))
        legendItemLabel(data.name)
    }
}

@Immutable
/**
 * One entry in a chart legend.
 *
 * @param name Text shown for the entry.
 * @param symbolShape Shape of the color symbol drawn beside the text.
 * @param color Color of that symbol, matching the series it stands for.
 * @param dashed Whether the symbol is drawn dashed, matching a dashed line series.
 */
data class LegendItemData(
    val name: String,
    val symbolShape: SymbolShape,
    val color: Color,
    val dashed: Boolean,
)

@Composable
private fun LegendItemData.selectSymbolSize() = when (symbolShape) {
    SymbolShape.LINE -> DpSize(width = 16.dp, height = 4.dp)
    SymbolShape.RECTANGLE -> DpSize(width = 12.dp, height = 12.dp)
}

/** Shape of the color symbol drawn beside a legend entry. */
enum class SymbolShape {
    /** A short horizontal bar, used for line series. */
    LINE,
    /** A filled square, used for bar series. */
    RECTANGLE
}
