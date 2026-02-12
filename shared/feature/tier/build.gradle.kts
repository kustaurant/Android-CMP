
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlin.native.cocoapods)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization) 
}

kotlin {
    androidTarget()

    val xcfName = "shared:feature:tierKit"

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

    cocoapods {
        version = "1.0.0"
        summary = "tierKit"
        homepage = "https://your.domain"
        ios.deploymentTarget = "12.0"
        podfile = rootProject.file("iosApp/Podfile")
        pod("NMapsMap") {
            version = "3.19.0"
        }

        pod("NMapsGeometry") {
            version = "1.0.2"
        }
    }

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
                implementation(libs.kamel.image.default)

                implementation(libs.kotlinx.serialization.json)
                implementation(libs.compose.ui.backhandler)

                implementation(project(":shared:core:designSystem"))
                implementation(project(":shared:core:logging"))
                implementation(project(":shared:core:serialization"))

                implementation(project(":shared:core:presentation"))

                implementation(project(":shared:domain:model"))
                implementation(project(":shared:domain:tier"))

                implementation(project(":shared:data:network"))
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
                implementation(libs.naver.maps)
                implementation(libs.androidx.activity.compose)
                implementation(libs.koin.android)
            }
        }

        iosMain {
            dependencies {
                implementation(project(":shared:core:designSystem"))
            }
        }
    }
}

compose.resources {
    publicResClass = true
    generateResClass = always
}

android {
    namespace = "com.kus.feature.tier"

    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}