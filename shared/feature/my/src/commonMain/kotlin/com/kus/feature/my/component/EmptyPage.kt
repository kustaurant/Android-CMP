package com.kus.feature.my.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import kustaurant.shared.core.designsystem.generated.resources.Res
import kustaurant.shared.core.designsystem.generated.resources.ic_kus_disable
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun EmptyPage(
    title: String,
    comment: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KusTheme.colors.c_FFFFFF)
            .navigationBarsPadding(),
    ) {
        MyPageTopBar(
            title = title,
            onBackClick = onBackClick,
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_kus_disable),
                contentDescription = null,
                tint = KusTheme.colors.c_AAAAAA,
                modifier = Modifier.size(width = 62.dp, height = 70.dp),
            )

            Spacer(Modifier.height(26.dp))

            Text(
                text = comment,
                style = KusTheme.typography.type15sb,
                color = KusTheme.colors.c_AAAAAA,
            )
        }
    }
}