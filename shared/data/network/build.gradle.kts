plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
}

kotlin {
    androidLibrary {
        namespace = "com.kus.data.network"
        compileSdk = 36
        minSdk = 26

        withHostTestBuilder {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    jvm("desktop")

    val xcfName = "shared:data:networkKit"

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
    
    sourceSets {
        commonMain {
            dependencies {
                api(libs.koin.core)
                implementation(libs.bundles.ktor)

                implementation(project(":shared:core:config"))
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
                api(libs.datastore.preferences)
                implementation(libs.koin.android)
                implementation(libs.ktor.client.okhttp)
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.testExt.junit)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutinesSwing)
                implementation(libs.oshi.core)
                implementation(libs.ktor.client.okhttp)
            }
        }
    }
}