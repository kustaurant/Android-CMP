plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(project(":shared:core:designSystem"))
                implementation(project(":shared:feature:splash"))
                implementation(project(":shared:feature:search"))
                implementation(project(":shared:feature:detail"))
                implementation(project(":shared:feature:evaluate"))

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
