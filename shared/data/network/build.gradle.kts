import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import java.util.Properties
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.buildkonfig)
}

extensions.configure<BuildKonfigExtension>("buildkonfig") {
    packageName = "com.kus.core.config"
    exposeObjectWithName = "BuildKonfig"

    val secretsProps = Properties().apply {
        val f = rootProject.file("secrets/sdks.properties")
        if (f.exists()) f.inputStream().use { load(it) }
    }

    fun resolveKey(
        key: String,
        defaultValue: String? = null,
        required: Boolean = false,
    ): String {
        val fromProps = secretsProps.getProperty(key)
        val fromEnv = System.getenv(key)
        val resolved = (fromEnv ?: fromProps ?: defaultValue)?.replace("\"", "")

        if (required && resolved.isNullOrBlank()) {
            error("$key is missing. Set it in secrets/sdks.properties or environment variable.")
        }
        return resolved ?: ""
    }

    defaultConfigs {
        buildConfigField(Type.STRING, "API_BASE_URL", resolveKey("API_BASE_URL", required = true))
    }
}


kotlin {
    androidLibrary {
        namespace = "com.kus.network"
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