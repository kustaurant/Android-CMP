package com.kus.splash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kustaurant.feature.splash.generated.resources.Res
import kustaurant.feature.splash.generated.resources.ic_splash_logo
import kustaurant.feature.splash.generated.resources.ic_splash_title
import org.jetbrains.compose.resources.painterResource

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(color = Color(0xFF43AB38)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(Res.drawable.ic_splash_logo),
                contentDescription = "Splash logo",
                modifier = Modifier.size(52.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(Res.drawable.ic_splash_title),
                contentDescription = "Splash Title",
                modifier = Modifier.size(width = 202.78.dp, height = 47.38.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "건대생을 위한 맛집 리스트",
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                fontSize = 20.sp
            )
        }
    }
}