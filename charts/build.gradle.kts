import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "io.github.aughtone"
version = "${libs.versions.versionName.get().toString()}${
    libs.versions.versionNameSiffix.get().toString()
}"

kotlin {
    jvm()
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    // See: https://kotlinlang.org/docs/js-project-setup.html
    js(IR) {
        browser {
            generateTypeScriptDefinitions()
        }
        useEsModules() // Enables ES2015 modules
        // binaries.executable()
    }
    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework {
            baseName = "ChartsKit"
            isStatic = true
            binaryOption(
                "bundleId",
                libs.versions.applicationId.get().toString()
            ) //"app.occurrence"
            binaryOption(
                "bundleShortVersionString",
                libs.versions.versionName.get().toString()
            ) //"1.0.0"
//            binaryOption("bundleVersion", libs.versions.versionCode.get().toString()) //"1"
        }
    }
//    linuxX64()

    sourceSets {
        commonMain.dependencies {
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material3)
            api(compose.ui)
            api(compose.materialIconsExtended)
            api(compose.components.resources)
            api(compose.components.uiToolingPreview)
            api(libs.aughtone.format.datetime)
            api(libs.kotlinx.datetime)
            api(libs.kotlinx.serialization.json)
            api(libs.coil.compose)
            api(libs.coil.network.ktor3)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.aughtone.charts"
    generateResClass = always
}

android {
    namespace = "io.github.aughtone.charts"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    debugImplementation(libs.androidx.compose.ui.tooling)
}

mavenPublishing {
    publishToMavenCentral()

    if (!project.hasProperty("skip-signing")) {
        signAllPublications()
    }

    coordinates(group.toString(), "charts", version.toString())

    pom {
        name = "Aught One Charts"
        description = "Multiplatform charts component."
        inceptionYear = "2025"
        url = "https://github.com/aughtone/aughtone-charts"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "bpappin"
                name = "Brill pappin"
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
