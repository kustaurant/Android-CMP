import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm("desktop")

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)

            implementation(libs.google.material)
            implementation(libs.androidx.core.splashscreen)
        }


        val desktopMain by getting {
            dependencies {
                implementation(project(":core:designSystem"))
                implementation(project(":feature:splash"))

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
                implementation(compose.material3)

                implementation(compose.desktop.currentOs)

                implementation(compose.components.uiToolingPreview)
            }
        }

        val desktopTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.kus.designSystemShowcase.MainKt"
    }
}
