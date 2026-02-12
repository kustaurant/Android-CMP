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
        maven("https://repository.map.naver.com/archive/maven")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}


include(":composeApp")
include(":uiShowcase")

include(":shared:core:designSystem")
include(":shared:core:logging")
include(":shared:core:config")

include(":shared:feature:login")
include(":shared:feature:onBoarding")
include(":shared:feature:splash")
include(":shared:feature:home")
include(":shared:feature:community")
include(":shared:feature:draw")
include(":shared:feature:tier")
include(":shared:feature:my")

include(":shared:data:firstLaunch")
include(":shared:data:network")
include(":shared:data:tier")

include(":shared:domain:firstLaunch")
include(":shared:domain:tier")
include(":shared:domain:model")


include(":shared:core:serialization")
include(":shared:core:presentation")
