import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.naver.maps)
            implementation(libs.naver.oauth)

            implementation(libs.google.material)
            implementation(libs.androidx.core.splashscreen)
            implementation(project(":shared:core:config"))
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.navigation.compose)
            implementation(libs.compose.ui.graphics)
            implementation(libs.koin.compose)

            implementation(project(":shared:core:designSystem"))
            implementation(project(":shared:core:logging"))
            implementation(project(":shared:core:config"))
            implementation(project(":shared:core:serialization"))

            implementation(project(":shared:data:network"))
            implementation(project(":shared:data:firstLaunch"))
            implementation(project(":shared:data:tier"))
            implementation(project(":shared:data:auth"))
            implementation(project(":shared:data:community"))

            implementation(project(":shared:feature:community"))
            implementation(project(":shared:feature:draw"))
            implementation(project(":shared:feature:home"))
            implementation(project(":shared:feature:login"))
            implementation(project(":shared:feature:onBoarding"))
            implementation(project(":shared:feature:splash"))
            implementation(project(":shared:feature:tier"))
            implementation(project(":shared:feature:my"))

            implementation(project(":shared:domain:firstLaunch"))
            implementation(project(":shared:domain:tier"))
            implementation(project(":shared:domain:auth"))
            implementation(project(":shared:domain:community"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        iosMain.dependencies {
            implementation(project(":shared:appKit"))

            implementation(project(":shared:domain:auth"))
            implementation(project(":shared:data:auth"))

            implementation(project(":shared:core:config"))
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
                implementation(compose.material3)
                implementation(compose.desktop.currentOs)
                implementation(compose.components.uiToolingPreview)

                implementation(project(":shared:core:designSystem"))
                implementation(project(":shared:core:config"))

                implementation(project(":shared:data:network"))
                implementation(project(":shared:data:firstLaunch"))

                implementation(project(":shared:domain:firstLaunch"))
            }
        }
    }
}

android {
    namespace = "com.kust.kustaurant"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.kust.kustaurant"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.kus.kustaurant.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.kus.kustaurant"
            packageVersion = "1.0.0"
        }
    }
}
