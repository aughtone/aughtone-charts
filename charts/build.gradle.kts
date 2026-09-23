import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.multiplatformLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.vanniktech.mavenPublish)
    // Ships charts/src/commonMain/skills/SKILL.md in every sources jar, for dependent projects' agents.
    alias(libs.plugins.dependencySkills)
}

group = libs.versions.namespace.get()
version = libs.versions.versionName.get()

kotlin {
    jvmToolchain(17)

    jvm()

    android {
        namespace = libs.versions.namespace.get()
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    // Published as klibs; the consumer's build decides bundling and module system when it links.
    // binaries.executable() is only here because Compose 1.12 refuses to run a web test that
    // loads Skiko without a webpack bundle (CMP-4906). It changes nothing that is published.
    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    // Consumed as a klib by Kotlin Multiplatform apps; no framework is built or published.
    iosArm64()
    iosSimulatorArm64()
//    linuxX64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.jetbrains.compose.foundation)
            implementation(libs.jetbrains.compose.material3)
            implementation(libs.jetbrains.compose.ui)
            implementation(libs.jetbrains.compose.components.resources)
            implementation(libs.jetbrains.compose.ui.tooling.preview)
            implementation(libs.jetbrains.compose.material.icons.extended)

            api(libs.kotlinx.datetime)
            api(libs.kotlinx.serialization.json)
            api(libs.coil.compose)
            api(libs.coil.network.ktor3)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        jvmTest.dependencies {
            // kotlin-reflect is the only way to read Kotlin visibility: an `internal` object
            // compiles to a public class, so Java reflection cannot tell the two apart.
            implementation(kotlin("reflect"))
        }

        androidMain.dependencies {
            implementation(libs.jetbrains.compose.ui.tooling.preview)
            implementation(libs.jetbrains.compose.ui.tooling)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.aughtone.charts.resources"
    generateResClass = always
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)

    val hasInMemoryKey = project.hasProperty("signingInMemoryKey") ||
            project.hasProperty("signingInMemoryKeyId") ||
            project.hasProperty("signing.gnupg.keyName")

    if (hasInMemoryKey && !project.hasProperty("skip-signing")) {
        signAllPublications()
    }

    coordinates(group.toString(), "charts", version.toString())

    pom {
        name = "Aught One Charts"
        description = "Multiplatform charts component."
        inceptionYear = "2025"
        url = "https://github.com/aughtone/aughtone-charts"
        licenses {
            // The combined work is distributed under Apache-2.0. Material derived from
            // netguru/compose-multiplatform-charts remains under its original MIT grant;
            // see NOTICE.md and LICENSE-MIT.md.
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
            license {
                name = "MIT License"
                url = "https://opensource.org/licenses/MIT"
                distribution = "https://opensource.org/licenses/MIT"
            }
        }
        developers {
            developer {
                id = "bpappin"
                name = "bpappin"
                url = "https://github.com/bpappin"
            }

        }
        scm {
            url = "https://github.com/aughtone/aughtone-charts"
            connection = "https://github.com/aughtone/aughtone-charts.git"
            developerConnection = "git@github.com:aughtone/aughtone-charts.git"
        }
    }
}
