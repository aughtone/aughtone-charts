---
name: io-github-aughtone-charts
description: >-
  Draw charts and graphs in Compose Multiplatform from common Kotlin code, on
  Android, iOS, desktop (JVM) and the web (Kotlin/JS and Kotlin/Wasm): a bar or
  column chart with grouped bars per category, a line chart, time series or area
  chart with shaded fill and dashed lines, a pie or donut chart, a packed bubble
  chart, a gauge, meter or speedometer-style dial, a percentage dial, a tank,
  cylinder or fill-level gauge, a sparkline or mini bar chart, and a progress arc
  or progress ring. Data goes in as categories of labelled values, time-stamped
  series, slices or plain lists of numbers. Axis labels, legends, value labels
  and the touch tooltip are composables you can restyle or hide; colors come from
  one chart palette or from your Material 3 theme; charts animate in, and line and
  bar charts show values on touch. Use instead of drawing chart geometry on a
  Canvas by hand. A fork of Netguru compose-multiplatform-charts. Called from
  Kotlin only.
license: Apache-2.0
metadata:
  version: "0.0.2-alpha1"
  repository: https://github.com/aughtone/aughtone-charts
---

# Aught One Charts

## What it solves

You have numbers in a Compose Multiplatform app and you want them on screen: revenue by quarter as grouped bars, a sensor reading over time as a line with shaded area, a share breakdown as a donut, a fill level as a gauge or a tank, progress as an arc or ring, a trend as a sparkline. This library draws them as composables that share one theme, so charts across a screen look like one system, and the same code renders on Android, on the desktop, on iOS and in a browser.

It covers the drawing — axes, grid, bars, lines, donut segments, a packed bubble cloud, a half-circle dial, a cylinder gauge, and small axis-free sparklines — and the labels, legends and touch tooltip around it. It does not fetch, aggregate or reshape data: you hand it values already computed.

## How it is meant to be used

Provide a palette once, high in your theme, then call charts anywhere below it:

```kotlin
CompositionLocalProvider(LocalChartColors provides ChartDefaults.chartColors()) {
    BarChart(
        data = BarChartData(
            categories = listOf(
                BarChartCategory("Q1", listOf(BarChartEntry(x = "Revenue", y = 12f, color = Color.Green))),
                BarChartCategory("Q2", listOf(BarChartEntry(x = "Revenue", y = 19f, color = Color.Green))),
            ),
        ),
        modifier = Modifier.fillMaxWidth().height(240.dp),
    )
}
```

`ChartDefaults.chartColors()` derives the palette from the current Material 3 theme; construct a `ChartColors` to choose one yourself. Axis ranges come from the data. Bar colors live on `BarChartEntry` and line colors on `LineChartSeries`; a chart's `colors` parameter styles only its grid and touch tooltip.

Time series take each point's `x` as a timestamp in epoch milliseconds:

```kotlin
LineChart(
    lineChartData = LineChartData(
        series = listOf(
            LineChartSeries(
                dataName = "Temperature",
                lineColor = Color.Blue,
                listOfPoints = readings.map { LineChartPoint(x = it.epochMillis, y = it.celsius) },
            ),
        ),
    ),
    xAxisLabel = { GridDefaults.XAxisLabel(formatTime(it as Long)) },
)
```

Every label is a composable parameter whose default is public, so varying one means wrapping the default rather than reproducing it, and `GridDefaults.NoLabel` hides any label that takes a single value. `BarChartWithLegend`, `LineChartWithLegend` and `PieChartWithLegend` draw the same chart with a legend, and `ChartLegend` places one on its own. The legend variants do not take every parameter of their base chart: `BarChartWithLegend` has no `overlayDataEntryLabel`, and `LineChartWithLegend` has no `roundMinMaxClosestTo`.

## Invariants and traps

**A palette is not optional.** Symptom: a blank or half-drawn chart, with no grid and an invisible tooltip, and nothing thrown. `BarChart`, `LineChart`, `Dial` and `GasBottle`, with their variants, read `LocalChartColors`, which defaults to `Color.Unspecified` for every value; provide a palette above them. `PieChart` and `BubbleChart` take their colors from the data.

**`LineChart` draws no time labels unless you pass them.** Symptom: nothing under the time axis, and a tooltip with no heading. `xAxisLabel` and `overlayHeaderLabel` default to `GridDefaults.NoLabel`, on `LineChartWithLegend` too, because the chart cannot know how the reader wants time shown. Each is given a timestamp as a `Long` in epoch milliseconds; format it and wrap the stock label. The ticks fall on round times counted from the epoch — whole hours, or multiples of a minute or second step for short windows — which is UTC, so in a zone offset by half an hour, hour ticks land on half past the hour local time.

**The y-axis rounds out to multiples of 10 by default.** Symptom: small or narrow data drawn as a near-flat line or stubby bars in a thin band of the chart. `roundMinMaxClosestTo`, on `LineChart` and on `BarChartConfig`, defaults to 10, and the lower bound rounds down and the upper bound up to a multiple of it, so values between 0 and 1, or a reading drifting between 20.1 and 20.4, get an axis of 0 to 10 or 20 to 30. Pass a smaller step; it is an `Int`, and values below 1 are treated as 1. `LineChartWithLegend` has no such parameter and always uses 10.

**`ChartAnimation.Sequenced` throws on the single-value charts.** Symptom: an `UnsupportedOperationException` as soon as the chart composes. `Dial`, `PercentageDial`, `GasBottle`, `PieChart` and `PieChartWithLegend` reject it, since there is only one value to stagger; use `ChartAnimation.Simple()` or `ChartAnimation.Disabled`.

**The percentage charts do not share a scale.** Symptom: a nearly empty bottle, or an arc that is always full. `GasBottle` and `PercentageDial` take 0 to 100; `ArcProgressBar` takes `0f..1f`. All three clamp, so a value on the wrong scale draws as a near-empty or full gauge rather than failing. `GasBottle` also draws anything from 1 to 5 as 5, so a small value stays visible.

**The per-chart color classes have no default arguments.** Symptom: a compile error that `surface` was not passed. `BarChartColors(grid = Color.LightGray)` does not compile; the same holds for `LineChartColors`, `DialColors` and `GasBottleColors`. Narrow a full palette instead: `ChartColors(...).barChartColors`.

**A bubble's size comes from `radius`, not `value`.** Symptom: bubbles whose sizes disagree with the numbers in them. `radius` defaults to `value`, and only the ratios between radii matter: the largest bubble is drawn at the maximum size and the smallest at the minimum. A single bubble, or bubbles that share one radius, are all drawn at the maximum size; an empty list draws nothing.

**A `Bubble`'s `position` is not where it was drawn.** Symptom: every bubble you hold reports a position at the origin. `BubbleChart` lays out copies, so your instances are never moved and one list can be shared between charts. The layout is seeded at random whenever the chart first composes or its bubbles, size or spacing change.

**The simple charts ignore the chart palette.** Symptom: sparklines in the theme's primary color whatever `LocalChartColors` says. `SimpleLineChart`, `SimpleBarChart` and `ArcProgressBar` take their default colors from `MaterialTheme` and draw no axes, grid or labels. The line and bar versions scale to the data unless `adaptToData = false`, which expects values already normalised to `0f..1f`.

**`BubbleChartPreview()` and `bubbleChartSampleData()` generate random data.** Symptom: a bubble chart whose values change on every composition. They are public development helpers, not for production code.

## What moved, and what it used to be called

The previous release is `0.0.1`. These behave differently in `0.0.2-alpha1` with the same signatures:

- **`LineChart` and `LineChartWithLegend` no longer label time by default.** In `0.0.1` the x-axis and tooltip heading printed raw epoch milliseconds; they now draw nothing unless given a label.
- **The y-axis contains the data.** In `0.0.1` a positive minimum rounded up, so a series from 15 to 25 got an axis of 20 to 30 with its lowest points outside the chart, and the line was drawn against the raw data range while the grid used the rounded one. The lower bound now rounds down, a zero-width range is widened, and the grid, bars and line share one mapping. Axis labels differ for ranges that do not cross zero.
- **The grid keeps a margin.** `BarChart` and `LineChart` now keep `GridDefaults.HORIZONTAL_LINES_OFFSET` clear above and below; in `0.0.1` the offset was accepted and ignored.
- **Transparency is kept.** Line, fill and bubble colors are faded by the entry animation rather than replaced by it, so a `Color.Transparent` series is hidden where `0.0.1` drew it opaque black.
- **Short time windows get ticks.** Windows too short for whole hours now tick in minutes or seconds; `0.0.1` drew none.
- **Inputs that crashed or drew `NaN` now draw**: a series with one timestamp, a constant series, an empty series, bars that are all zero, a bar chart with no categories or with categories of different widths, and a single bubble.

**This library was Netguru's compose-multiplatform-charts.** It is a fork of that project (MIT, Copyright (c) 2022 Netguru), published as `com.netguru.multiplatform-charts:multiplatform-charts`, with `-android` and `-desktop` variants, whose code lived in `com.netguru.multiplatform.charts`. Since `0.0.1` the coordinate is `io.github.aughtone:charts` and every package is under `io.github.aughtone.charts`: an import of `com.netguru.multiplatform.charts.bar.BarChart` no longer resolves, and is `io.github.aughtone.charts.bar.BarChart`. Type and parameter names otherwise follow upstream, so code written against it differs only in its imports — though it draws differently, for the reasons above, and upstream also printed raw epoch milliseconds on the time axis.

**The `*Defaults` objects are public since `0.0.1`.** `GridDefaults`, `BarChartDefaults`, `BubbleDefaults`, `DialDefaults` and `PieDefaults` were `internal` upstream; they can be called and wrapped. `GridDefaults.NoLabel` is new in `0.0.2-alpha1`.

**`Number.round` stopped emitting a trailing `.0` in `0.0.1`.** A whole number formats as `1`, where upstream produced `1.0` on JVM and Android. It shows in the default bubble and pie value labels.

## What it is not for

It is called from Kotlin only. Every chart is a `@Composable`, which Swift and JavaScript cannot call, and the library publishes nothing for them: no Apple framework, XCFramework, Swift package or pod, and no npm package, TypeScript definitions or `@JsExport`ed API. Its iOS, JavaScript and Wasm artifacts are klibs for Kotlin consumers. A Compose Multiplatform app uses it from Kotlin on every platform; a SwiftUI or UIKit app, or a JavaScript or TypeScript app, gets nothing from it, and its data classes are not a model to share with native Swift code.

It draws charts; it does not analyse data. There is no aggregation, downsampling, statistics, date bucketing or currency formatting — compute those before building the data classes.

It is not an interactive charting toolkit. `LineChart`, `BarChart` and their legend variants show a value tooltip on touch, and that is the extent of it: no zooming, panning, selection, brushing or drill-down, and no export to an image.

It exposes no accessibility semantics. Charts draw to a `Canvas` and tell a screen reader nothing, so describe the data yourself alongside the chart when that matters.
