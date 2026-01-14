plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidTarget()

    val xcfName = "shared:feature:splashKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    jvm("desktop")

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.androidx.lifecycle.runtimeCompose)

                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)

                implementation(libs.navigation.compose)

                implementation(libs.lifecycle.viewmodel)

                implementation(libs.kotlinx.coroutines.core)

                implementation(project(":shared:core:designSystem"))
                implementation(project(":shared:data:network"))

                implementation(project(":shared:domain:firstLaunch"))
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)

                implementation(libs.koin.android)
            }
        }

        iosMain {
            dependencies {
            }
        }
    }
}

android {
    namespace = "com.kus.feature.splash"
    compileSdk = 36
    defaultConfig { minSdk = 26 }
}