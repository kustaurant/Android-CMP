import com.codingfeline.buildkonfig.compiler.FieldSpec.Type
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.androidLibrary)
}

extensions.configure<BuildKonfigExtension>("buildkonfig") {
    packageName = "com.kus.core.config"
    exposeObjectWithName = "BuildKonfig"

    val secretsProps = Properties().apply {
        val f = rootProject.file("secrets/sdks.properties")
        if (f.exists()) f.inputStream().use { load(it) }
    }

    fun resolveKey(key: String, defaultValue: String? = null, required: Boolean = false): String {
        val fromProps = secretsProps.getProperty(key)
        val fromEnv = System.getenv(key)
        val resolved = (fromEnv ?: fromProps ?: defaultValue)
            ?.trim()
            ?.removeSurrounding("\"")
            ?.trim()

        if (required && resolved.isNullOrBlank()) {
            error("$key is missing. Set it in secrets/sdks.properties or environment variable.")
        }
        return resolved.orEmpty()
    }

    defaultConfigs {
        buildConfigField(Type.STRING, "API_BASE_URL", resolveKey("API_BASE_URL", required = true))

        buildConfigField(
            Type.STRING,
            "NAVER_MAP_CLIENT_ID",
            resolveKey("NAVER_MAP_CLIENT_ID", required = true)
        )

        buildConfigField(
            Type.STRING,
            "NAVER_CLIENT_ID",
            resolveKey("NAVER_CLIENT_ID", required = true)
        )
        buildConfigField(
            Type.STRING,
            "NAVER_CLIENT_SECRET",
            resolveKey("NAVER_CLIENT_SECRET", required = true) // 가능하면 서버로 빼는 게 원칙
        )

        buildConfigField(
            Type.STRING,
            "NAVER_IOS_URL_SCHEME",
            resolveKey("NAVER_IOS_URL_SCHEME", defaultValue = "kustaurant")
        )
    }
}


kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm("desktop")

    sourceSets {
        commonMain.dependencies { }
    }
}


android {
    namespace = "com.kus.core.config"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}