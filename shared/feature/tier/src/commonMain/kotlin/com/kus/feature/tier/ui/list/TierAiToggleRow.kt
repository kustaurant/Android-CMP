package com.kus.feature.tier.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import kustaurant.shared.feature.tier.generated.resources.Res
import kustaurant.shared.feature.tier.generated.resources.ic_ai_filter_off
import kustaurant.shared.feature.tier.generated.resources.ic_ai_filter_on
import org.jetbrains.compose.resources.painterResource


@Composable
fun TierAiToggleRow(
    modifier: Modifier = Modifier,
    onTierGuideClick: () -> Unit = {},
) {
    var isAiOn by rememberSaveable { mutableStateOf(false) }

    val iconRes = if (isAiOn) {
        Res.drawable.ic_ai_filter_on
    } else {
        Res.drawable.ic_ai_filter_off
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { isAiOn = !isAiOn }
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )

            Text(
                text = "AI 기반 티어 보기",
                modifier = Modifier.padding(start = 6.dp),
                style = KusTheme.typography.type12m.copy(color = KusTheme.colors.c_AAAAAA)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "티어란?",
            style = KusTheme.typography.type12m.copy(
                color = KusTheme.colors.c_AAAAAA,
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier.clickable(onClick = onTierGuideClick)
        )
    }
}