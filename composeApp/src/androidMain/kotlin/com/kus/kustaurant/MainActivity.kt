package com.kus.kustaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle =
                SystemBarStyle.light(
                    android.graphics.Color.WHITE,
                    android.graphics.Color.WHITE,
                ),
            navigationBarStyle =
                SystemBarStyle.light(
                    android.graphics.Color.WHITE,
                    android.graphics.Color.WHITE,
                ),
        )
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}