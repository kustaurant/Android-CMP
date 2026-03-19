package com.kus.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kustaurant.shared.core.designsystem.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun KusLoadingAnimation(
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/lottie/loading.json").decodeToString()
        )
    }

    Image(
        painter = rememberLottiePainter(
            composition = composition,
            iterations = Compottie.IterateForever
        ),
        contentDescription = "로딩 중",
        modifier = modifier.size(120.dp)
    )
}
