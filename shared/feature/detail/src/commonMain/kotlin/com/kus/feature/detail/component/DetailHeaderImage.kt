package com.kus.feature.detail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.kus.designsystem.theme.KusTheme
import kustaurant.shared.feature.detail.generated.resources.Res
import kustaurant.shared.feature.detail.generated.resources.img_rest_example
import org.jetbrains.compose.resources.painterResource

@Composable
fun DetailHeaderImage(
    imageHeight: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(imageHeight)
    ) {
        Image(
            painter = painterResource(Res.drawable.img_rest_example),
            contentDescription = "식당 이미지 사진",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, KusTheme.colors.c_000000)
                    )
                )
        )
    }
}