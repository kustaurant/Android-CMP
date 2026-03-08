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
include(":shared:appKit")

include(
    ":shared:core:designSystem",
    ":shared:core:presentation",
    ":shared:core:serialization",
    ":shared:core:logging",
    ":shared:core:config",
    ":shared:core:serialization",
    ":shared:core:presentation",
)

include(
    ":shared:data:firstLaunch",
    ":shared:data:network",
    ":shared:data:tier",
    ":shared:data:auth",
    ":shared:data:home",
    ":shared:data:detail"
)

include(
    ":shared:domain:firstLaunch",
    ":shared:domain:model",
    ":shared:domain:tier",
    ":shared:domain:auth",
    ":shared:domain:home",
    ":shared:domain:detail"
)

include(
    ":shared:feature:login",
    ":shared:feature:onBoarding",
    ":shared:feature:splash",
    ":shared:feature:home",
    ":shared:feature:community",
    ":shared:feature:draw",
    ":shared:feature:tier",
    ":shared:feature:my",
    ":shared:feature:search",
    ":shared:feature:detail",
    ":shared:feature:evaluate"
)
