import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.multiplatformLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = libs.versions.namespace.get().toString()
version = "${libs.versions.versionName.get().toString()}${
    libs.versions.versionNameSiffix.get().toString()
}"

kotlin {
    jvmToolchain(17)
    jvm()
//    androidTarget {
//        publishLibraryVariants("release")
//        @OptIn(ExperimentalKotlinGradlePluginApi::class)
//        compilerOptions {
//            jvmTarget.set(JvmTarget.JVM_21)
//        }
//    }
    android {
        namespace = libs.versions.namespace.get().toString()
        compileSdk = libs.versions.compileSdk.get().toInt()
//        defaultConfig {
//            minSdk = libs.versions.minSdk.get().toInt()
//        }
//        compileOptions {
//            sourceCompatibility = JavaVersion.VERSION_21
//            targetCompatibility = JavaVersion.VERSION_21
//        }
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
            baseName = "AughtoneCMPChartsKit"
            isStatic = true
            binaryOption(
                "bundleId",
                libs.versions.namespace.get().toString()
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
            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.jetbrains.compose.foundation)
            implementation(libs.jetbrains.compose.material3)
            implementation(libs.jetbrains.compose.ui)
            implementation(libs.jetbrains.compose.components.resources)
            implementation(libs.jetbrains.compose.ui.tooling.preview)
            implementation(libs.jetbrains.compose.material.icons.extended)
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
    packageOfResClass = "com.netguru.multiplatform.charts.resources"
    generateResClass = always
}



//dependencies {
//    debugImplementation(libs.androidx.compose.ui.tooling)
//}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)

    if (!project.hasProperty("skip-signing")) {
        signAllPublications()
    }

    coordinates(group.toString(), "compose-multiplatform-charts", version.toString())

    pom {
        name = "Aughtone Charts"
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
