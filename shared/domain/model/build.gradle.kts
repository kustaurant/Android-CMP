plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
}

kotlin {
    androidLibrary {
        namespace = "com.kus.domain.model"
        compileSdk = 36
        minSdk = 26
    }

    val xcfName = "shared:domain:modelKit"

    iosX64 {
        binaries.framework { baseName = xcfName }
    }

    iosArm64 {
        binaries.framework { baseName = xcfName }
    }

    iosSimulatorArm64 {
        binaries.framework { baseName = xcfName }
    }

    jvm("desktop")

    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.naver.maps)
            }
        }

        commonMain {
            dependencies {
            }
        }
    }
}
