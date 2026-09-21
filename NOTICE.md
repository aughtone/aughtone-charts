# NOTICE

Aught One Charts
Copyright 2025 The Aught One Authors

This product is licensed under the Apache License, Version 2.0. A copy of that license is included in the [LICENSE](LICENSE) file at the root of this repository, and is also available at http://www.apache.org/licenses/LICENSE-2.0

This is not an original work. It is a fork of [compose-multiplatform-charts](https://github.com/netguru/compose-multiplatform-charts), and most of the `charts` module is derived from it.

## How this notice is distributed

Where the packaging format carries arbitrary files, this notice and both license texts are included in the artifact itself, under `META-INF`: `LICENSE-APACHE.md`, `LICENSE-MIT.md` and `NOTICE.md`. That covers the JVM jar and the Android archive. The Apache text is named `LICENSE-APACHE.md` rather than `LICENSE` because the Android build strips `META-INF/LICENSE` by exact name under its default packaging rules.

Kotlin/Native and Kotlin/JS artifacts are klibs, and the klib format provides no route for arbitrary files of this kind. For those targets the licensing is carried by the published Maven metadata instead: every POM and Gradle module file declares both Apache-2.0 and MIT, and this repository holds the full texts. This is a deliberate choice, made because no packaging mechanism exists for the alternative.

## Netguru compose-multiplatform-charts

The chart implementations under `io.github.aughtone.charts` are derived from that project. That code is:

Copyright (c) 2022 Netguru

It is distributed under the MIT License, a copy of which is included in the [LICENSE-MIT.md](LICENSE-MIT.md) file at the root of this repository, and is also available at https://opensource.org/licenses/MIT. It remains under that license, and the copyright and permission notice above carries with any copy or substantial portion of it.

The original `com.netguru.multiplatform.charts` package was renamed to `io.github.aughtone.charts`. Aside from that rename, build and dependency modernization, and the `io.github.aughtone.charts.simple` package added here, the derived files remain substantially the work of the original authors.
