rootProject.name = "Kustaurant"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
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
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":uiShowcase")
include(":composeApp")

include(":core:designSystem")

include(":designSystemApp")

include(":feature:login")
include(":feature:onBoarding")
include(":feature:splash")
include(":feature:home")

include(":data:firstLaunch")
include(":data:network")

include(":domain:firstLaunch")
include(":feature:community")
include(":feature:draw")
include(":feature:tier")
include(":feature:my")
