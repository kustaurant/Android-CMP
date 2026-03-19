package com.kus.kustaurant

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var keepSplashOnScreen = true
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }
        enableEdgeToEdge(
            statusBarStyle =
                SystemBarStyle.light(
                    Color.TRANSPARENT,
                    Color.TRANSPARENT,
                ),
            navigationBarStyle =
                SystemBarStyle.light(
                    Color.WHITE,
                    Color.WHITE,
                ),
        )
        super.onCreate(savedInstanceState)

        setContent {
            App(
                onSplashLoadingChanged = { isLoading ->
                    keepSplashOnScreen = isLoading
                }
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}