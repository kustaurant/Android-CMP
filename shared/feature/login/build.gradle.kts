plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.native.cocoapods)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm("desktop")

    cocoapods {
        summary = "Kustaurant iOS Login Kit"
        homepage = "https://kustaurant.com"
        version = "1.0.0"
        ios.deploymentTarget = "13.0"

        framework {
            baseName = "loginKit"
            isStatic = false
        }

        pod("NidThirdPartyLogin") {
            version = "5.1.0"
        }
        pod("NaverBridge", path = rootProject.file("iosNaverBridge"))
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

                implementation(project(":shared:core:designSystem"))
                implementation(project(":shared:core:presentation"))
                implementation(project(":shared:core:logging"))

                implementation(project(":shared:data:network"))

                implementation(project(":shared:data:auth"))
                implementation(project(":shared:domain:auth"))
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
                implementation(libs.naver.oauth)
                implementation(libs.koin.android)
            }
        }

        iosMain {
            dependencies {
            }
        }
    }
}

compose.resources {
    publicResClass = true
    generateResClass = always
}

android {
    namespace = "com.kus.feature.login"

    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}