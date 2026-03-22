package com.kus.feature.draw.ui.result

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import kustaurant.shared.core.designsystem.generated.resources.img_no_result
import org.jetbrains.compose.resources.painterResource
import kustaurant.shared.core.designsystem.generated.resources.Res as CoreRes

@Composable
fun DrawNoResultScreen(
    innerPadding: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(CoreRes.drawable.img_no_result),
            contentDescription = null,
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "뽑을 수 있는 맛집이 없어요",
            style = KusTheme.typography.type17sb,
            color = KusTheme.colors.c_666666,
            textAlign = TextAlign.Center
        )
    }
}