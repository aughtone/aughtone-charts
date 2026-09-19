# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- **Branding & Standardization**:
    - Ecosystem rebranding to "Aught One".
    - Unified iOS Kit naming to `AughtoneChartsKit`.
    - Standardized `namespace` and publication coordinates to `io.github.aughtone`.
- **Dependency Updates**: Bumped Kotlin to `2.4.0`, AGP to `9.2.1`, Compose Multiplatform to `1.11.1`, Material3 to `1.11.0-alpha07`, Coil to `3.5.0`, Koin to `4.2.2`, kotlinx-datetime to `0.8.0`, androidx-ui-tooling-preview to `1.11.3`, vanniktech-maven-publish to `0.37.0` and `aughtone-format` to `3.0.3`.

### Changed
- **`aughtone-format` 2.x to 3.x (major)**: The 3.0.0 release deprecated the `toReadable*` formatting extensions in favour of `formatReadable*`. This library has no call sites for either, so no migration was required here. The dependency is declared `api` in the `charts` module, so it stays on the public classpath and consumers may need to migrate their own call sites.
