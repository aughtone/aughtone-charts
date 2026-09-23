@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        // The dependency-skills plugin is not published yet; it comes from a local publish, and only it.
        mavenLocal {
            content { includeGroupAndSubgroups("org.dependencyskills") }
        }
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        mavenLocal()
    }
}

rootProject.name = "AOCharts"

include(":charts")
//include(":compose-multiplatform-charts")
