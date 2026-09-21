# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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
