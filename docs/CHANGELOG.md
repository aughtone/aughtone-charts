# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- **Branding & Standardization**:
    - Ecosystem rebranding to "Aught One".
    - Unified iOS Kit naming to `AughtoneChartsKit`.
    - Standardized `namespace` and publication coordinates to `io.github.aughtone`.
- **Dependency Updates**: Bumped Kotlin to `2.4.0`, AGP to `9.2.1`, Compose Multiplatform to `1.11.1`, Material3 to `1.11.0-alpha07`, Coil to `3.5.0`, Koin to `4.2.2`, kotlinx-datetime to `0.8.0`, androidx-ui-tooling-preview to `1.11.3`, and vanniktech-maven-publish to `0.37.0`.

### Changed
- **Version scheme**: Dropped the `-alphaN` suffix. Alpha status is indicated by a `0.x` major/minor, so the coordinate is now `io.github.aughtone:charts:0.0.1`. The `versionNameSiffix` catalog key is removed.

### Removed
- **`aughtone-format` dependency**: The `charts` module declared `api(libs.aughtone.format.datetime)` but had no call sites for it. Because it was an `api` dependency it sat on the public classpath, so every consumer inherited it — and its major-version churn — for an API this library never used. Removed, along with its version catalog entries. All targets build unchanged.
