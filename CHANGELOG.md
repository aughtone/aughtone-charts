# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- **Kotlin/Wasm target**: The library now publishes `charts-wasm-js`, so Compose Multiplatform apps targeting `wasmJs` — Compose's main web target — can depend on it. The common tests run on it as well.
- **Sub-hour time ticks**: `TimestampXAxisScale` now steps down through minutes and then seconds when a window is too short for whole-hour ticks, so a live chart over a few minutes gets grid lines, and time labels wherever it passes an x-axis label. Ticks fall on round times in UTC.
- **`GridDefaults.NoLabel`**: a label that draws nothing, for hiding any label.
- **An agent skill, shipped inside every sources jar** at `commonMain/skills/io-github-aughtone-charts/SKILL.md`, for the coding agents of projects that depend on the library: what it draws, the traps that compile and are wrong — a missing palette, the default y-axis rounding, `ChartAnimation.Sequenced` on single-value charts — and what changed since `0.0.1`.

### Changed
- **Version scheme**: Pre-release suffixes are back, reversing the change recorded under `0.0.1`. Alpha releases are now numbered `0.0.x-alphaN` rather than relying on a `0.x` major/minor alone, so successive alphas order correctly against a published release.
- **Grid padding is honoured**: `measureChartGrid` accepted `horizontalLinesOffset` but ignored it. The grid, bars and line are now drawn inside it, so `BarChart` and `LineChart` keep `GridDefaults.HORIZONTAL_LINES_OFFSET` clear above and below. The offset is capped at half the canvas height.
- **Y-axis rounding**: `YAxisScale` now rounds its lower bound down and its upper bound up, and widens a zero-width range by one step. Axis labels differ from earlier releases for ranges that do not cross zero.
- **`LineChart` draws no time labels by default**: its x-axis label and overlay header printed each timestamp as raw epoch milliseconds. They now default to `GridDefaults.NoLabel`, in `LineChartWithLegend` too; pass a label that formats the timestamp to show times.

### Removed
- **iOS framework binary**: The module no longer builds an `AOChartsKit` framework. It was never published, and the charts cannot be called from Swift; iOS consumers use the klib from Kotlin, as before.

### Fixed
- **A single bubble, or bubbles sharing one radius, got a `NaN` radius**: `BubbleChart` scales radii between the smallest and largest in the list, and with no range between them the scaling divided zero by zero. They are now drawn at the maximum size.
- **`Bubble` documentation claimed the chart mutated the caller's instances**: the `0.0.1` KDoc said `position` and `velocity` were changed by the layout and that an instance was unsafe to share between charts. The chart lays out copies, so neither was true. The documentation now says so, and notes that `position` does not tell you where a bubble was drawn.
- **`LineChart`'s line did not match its y-axis labels**: the grid was drawn against the rounded axis range but the line against the raw data range, and a positive minimum rounded up, so a series running from 15 to 25 got an axis of 20 to 30 and was read against the wrong labels. The grid, the line and bars now share one mapping.
- **`LineChart` crashed with a single timestamp**: x was mapped with integer division by the time range. The point is now centred.
- **A series whose value never changes was not drawn**: y was mapped through 0 / 0. The axis is now widened around the value.
- **`BarChart` with every value zero drew `NaN` bar positions**: the axis now runs from zero up one step.
- **`LineChart`'s overlay showed `NaN` when touched exactly on a sample**, which on a chart with a single timestamp was every touch.
- **An empty `LineChartSeries` crashed the chart**, although it was documented as allowed.
- **`BarChart` crashed with no categories, or with a category wider than the first**: its animation values were sized from the first category, so an empty chart threw and a later category with more bars indexed past the end. They are now sized from the widest category.
- **Transparent colors drew opaque**: line, fill and bubble colors had their alpha replaced by the animation's rather than scaled by it, so a `Color.Transparent` line drew black.
- **Degenerate settings crashed or produced `NaN`**: a tick count of zero threw in `TimestampXAxisScale`, and a rounding step of zero gave `YAxisScale` `NaN` bounds.
- **Documentation**: `YAxisScale` claimed its axis always contained the data and that it produced at most `maxTickCount` intervals; `measureChartGrid` described an offset it ignored; and KDoc on 18 declarations sat between an annotation and the declaration, and now sits above the annotation as it does elsewhere.

## [0.0.1] - 2026-09-21

First release of this fork. See [NOTICE.md](NOTICE.md) for its provenance and licensing.

### Added
- **Licensing and attribution**: Added `NOTICE.md` recording that most of the `charts` module derives from [compose-multiplatform-charts](https://github.com/netguru/compose-multiplatform-charts), Copyright (c) 2022 Netguru, under the MIT License. Renamed the MIT text to `LICENSE-MIT.md` to distinguish it from the Apache `LICENSE` covering the combined work, and declared both licences in the published POM.
- **Notices in published artifacts**: The JVM jar and Android archive now carry `LICENSE-APACHE.md`, `LICENSE-MIT.md` and `NOTICE.md` under `META-INF`. klibs rely on the Maven metadata instead, as the format provides no route for such files; `NOTICE.md` records this.
- **Test suite**: 30 tests covering the range-mapping helpers, axis scales and bar chart data, running on JVM, Kotlin/JS and iOS, plus 6 JVM tests asserting the defaults objects stay part of the public API.
- **API documentation**: KDoc on every public declaration in `commonMain`; 44 had none.
- **Simple charts in the README**: `SimpleLineChart`, `SimpleBarChart` and `ArcProgressBar` were undocumented despite being this fork's own additions.
- **Branding & Standardization**:
    - Ecosystem rebranding to "Aught One".
    - Unified iOS Kit naming to `AughtoneChartsKit`.
    - Standardized `namespace` and publication coordinates to `io.github.aughtone`.
- **Dependency Updates**: Bumped Kotlin to `2.4.0`, AGP to `9.2.1`, Compose Multiplatform to `1.11.1`, Material3 to `1.11.0-alpha07`, Coil to `3.5.0`, Koin to `4.2.2`, kotlinx-datetime to `0.8.0`, androidx-ui-tooling-preview to `1.11.3`, and vanniktech-maven-publish to `0.37.0`.

### Changed
- **Chart defaults are public**: `GridDefaults`, `BarChartDefaults`, `BubbleDefaults`, `DialDefaults` and `PieDefaults` were `internal`, so a caller could replace a label parameter but not reference the default it was varying. They are now public and can be wrapped.
- **Version scheme**: Dropped the `-alphaN` suffix. Alpha status is indicated by a `0.x` major/minor, so the coordinate is `io.github.aughtone:charts:0.0.1`. The `versionNameSiffix` catalog key is removed.

### Removed
- **`aughtone-format` dependency**: The `charts` module declared `api(libs.aughtone.format.datetime)` but had no call sites for it. Because it was an `api` dependency it sat on the public classpath, so every consumer inherited it — and its major-version churn — for an API this library never used. Removed, along with its version catalog entries.

### Fixed
- **Y-axis maximum truncated fractional bounds**: `YAxisScale` rounded through `toInt()`, so a maximum of `40.7` produced an axis maximum of `40` and left the topmost data point outside the plotted area. Bounds now round away from zero on the float value.
- **Empty bar chart category crashed**: reading `BarChartData.minY` or `maxY` threw `NoSuchElementException` when any category had no entries. An empty category now contributes `0f`.
- **`Number.round` rendered whole numbers differently per platform**: Kotlin/JS has no distinct `Int` or `Float` at runtime, so a whole number rendered as `1` in a browser and `1.0` elsewhere. All targets now render `1`. This affects the bubble and pie value labels.
- **Apache licence stripped from the Android archive**: the packaged text was named `META-INF/LICENSE`, which the Android build removes by exact name under its default packaging rules, so the archive shipped the MIT notice without the Apache licence. Renamed to `LICENSE-APACHE.md`.
- **README corrections**: absolute `file://` links replaced with relative paths; the licence section no longer claims MIT for the whole library; screenshot links made repo-relative, since a leading slash resolved against `github.com`; the pie chart screenshot's alt text corrected; the theming example now passes the required `BarChartColors.surface` argument.
