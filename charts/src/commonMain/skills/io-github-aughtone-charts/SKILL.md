---
name: io-github-aughtone-charts
description: >-
  Chart composables for Compose Multiplatform — bar, line, pie, bubble, dial,
  gas-bottle, and lightweight sparkline and progress-arc charts — on Android,
  JVM desktop, iOS and the web through Kotlin/JS and Kotlin/Wasm. Use instead of
  drawing chart geometry onto a Canvas by hand, or reaching for an Android-only
  charting library that will not build for the other targets. Each chart is a
  composable that takes a data class and draws itself, and its axis, legend and
  value labels are composable parameters with public defaults you can wrap.
  Consumed from Kotlin only; it cannot be called from Swift or JavaScript.
license: Apache-2.0
metadata:
  version: "0.0.2-alpha1"
  repository: https://github.com/aughtone/aughtone-charts
---

# Aught One Charts

## What it solves

You have numbers in a Compose Multiplatform app and you want them on screen: revenue by category, a sensor reading over time, a share breakdown, a fill level, a progress arc. This library draws them as composables that share one theme, so charts across a screen look like one system rather than several, and the same code renders on Android, on the desktop, on iOS and in a browser.

It covers the drawing: axes, grid, bars, lines with a shaded area, donut segments, a packed bubble cloud, a half-circle dial, a cylinder gauge, and small axis-free sparklines. It does not fetch, aggregate or reshape your data — you hand it values you have already computed.

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

`ChartDefaults.chartColors()` derives a palette from the current `MaterialTheme`; construct a `ChartColors` directly to choose one yourself. Axis ranges are derived from the data, not set by you. Bar colors live on `BarChartEntry` and line colors on `LineChartSeries`; the `colors` parameter only styles the grid and the touch overlay.

Time series take epoch milliseconds as the x value:

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
)
```

Every label is a composable parameter whose default is public, so varying one label means wrapping the default rather than reproducing it:

```kotlin
BarChart(
    data = data,
    yAxisLabel = { value -> GridDefaults.YAxisLabel("$value kg") },
)
```

`BarChartWithLegend`, `LineChartWithLegend` and `PieChartWithLegend` draw the same chart with a legend below it, and `ChartLegend` places one on its own.

## Invariants and traps

**A palette is not optional.** `LocalChartColors` defaults to `Color.Unspecified` for every value. Without a `CompositionLocalProvider` above the chart, the grid and overlays draw with unspecified colors and the chart looks broken rather than throwing. This is the usual reason a first chart appears blank or half-drawn.

**`ChartAnimation.Sequenced` throws on the single-value charts.** `Dial`, `PercentageDial`, `GasBottle` and `PieChart` raise `UnsupportedOperationException` for it, because there is only one value to stagger. Use `ChartAnimation.Simple()` or `ChartAnimation.Disabled`.

**The per-chart color classes have no default arguments.** `BarChartColors(grid = Color.LightGray)` does not compile, because `surface` is required too; the same holds for `LineChartColors`, `DialColors` and `GasBottleColors`. Narrow a full palette instead: `ChartColors(...).barChartColors`.

**A bubble's size comes from `radius`, not `value`.** `radius` defaults to `value`, and only the ratios between radii matter: the chart scales them so the largest bubble is drawn at the maximum size and the smallest at the minimum. Pass `radius` explicitly and the size no longer follows the number shown in the label. A single bubble, or bubbles that all share one radius, are all drawn at the maximum size, and an empty list draws nothing.

**A `Bubble`'s `position` is not where it was drawn.** `BubbleChart` lays out copies of the bubbles you pass, so your own instances are never moved: their `position` stays at the origin, and sharing one list between two charts is safe. The layout is seeded at random whenever the chart is first composed or its bubbles, size or spacing change, so do not rely on where a bubble lands.

**The simple charts do not use this library's palette.** `SimpleLineChart`, `SimpleBarChart` and `ArcProgressBar` take their default colors from `MaterialTheme`, not `LocalChartColors`, and draw no axes, grid or labels. `ArcProgressBar` takes `progress` in `0f..1f`. The line and bar versions scale to the data unless `adaptToData = false`, which expects values already normalised to `0f..1f`.

**`BubbleChartPreview()` and `bubbleChartSampleData()` generate random data.** They are public development helpers, not something to call from production code.

## What moved, and what it used to be called

**This library was Netguru's compose-multiplatform-charts.** It is a fork of that project (MIT, Copyright (c) 2022 Netguru), which was published as `com.netguru.multiplatform-charts:multiplatform-charts:1.0.0`, with `-android` and `-desktop` variants, and whose code lived in `com.netguru.multiplatform.charts`. As of `0.0.1` the coordinate is `io.github.aughtone:charts` and every package is under `io.github.aughtone.charts`: an import of `com.netguru.multiplatform.charts.bar.BarChart` no longer resolves, and is `io.github.aughtone.charts.bar.BarChart`. Type names, parameter names and chart behaviour otherwise follow the upstream shape, so code written against upstream differs from code written against this library only in its imports.

**The `*Defaults` objects became public in `0.0.1`.** `GridDefaults`, `BarChartDefaults`, `BubbleDefaults`, `DialDefaults` and `PieDefaults` were `internal` upstream, so a caller could replace a label but not reference the default it was varying. They can now be called and wrapped.

**`Number.round` stopped emitting a trailing `.0` in `0.0.1`.** A whole number now formats as `1`, where upstream produced `1.0` on JVM and Android. It shows in the default bubble and pie value labels; pass your own label composable if you need the old form.

## What it is not for

It cannot be called from Swift or JavaScript. It ships no Apple framework and no npm package: iOS and web apps use it from Kotlin, through Compose Multiplatform. Its data classes are not a model to share with native Swift code.

It draws charts; it does not analyse data. There is no aggregation, downsampling, statistics, date bucketing or currency formatting — compute those before building the data classes.

It is not an interactive charting toolkit. `LineChart` and `BarChart` show a value overlay on touch, and that is the extent of it: no zooming, panning, selection, brushing or drill-down, and no export to an image.

It exposes no accessibility semantics. Charts draw to a `Canvas` and tell a screen reader nothing, so describe the data yourself alongside the chart when that matters.
