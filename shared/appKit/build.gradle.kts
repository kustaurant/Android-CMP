plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlin.native.cocoapods)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Kustaurant iOS App Kit"
        homepage = "https://kustaurant.com"
        version = "1.0.0"
        ios.deploymentTarget = "13.0"

        framework {
            baseName = "appKit"
            isStatic = false
        } 

        pod("NMapsMap") {
            version = "3.23.0"
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

                api(project(":shared:feature:login"))
                api(project(":shared:feature:tier"))
                api(project(":shared:feature:community"))
                api(project(":shared:data:community"))
            }
        }
    }
}

android {
    namespace = "com.kus.appkit"

    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}