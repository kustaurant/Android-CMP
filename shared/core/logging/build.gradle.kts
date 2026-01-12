plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
}

kotlin {
    androidLibrary {
        namespace = "com.kus.logging"
        compileSdk = 36
        minSdk = 26
    }

    val xcfName = "shared:core:loggingKit"

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
                implementation(libs.napier.log)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.napier.log)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.napier.log)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(libs.napier.log)
            }
        }
    }
}