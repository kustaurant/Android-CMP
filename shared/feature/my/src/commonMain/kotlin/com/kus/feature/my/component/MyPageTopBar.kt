package com.kus.feature.my.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import kustaurant.shared.feature.my.generated.resources.Res
import kustaurant.shared.feature.my.generated.resources.ic_left_chevron
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun MyPageTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(KusTheme.colors.c_FFFFFF)
                .statusBarsPadding()
                .padding(vertical = 14.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = title,
                style = KusTheme.typography.type17sb,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            Icon(
                painter = painterResource(Res.drawable.ic_left_chevron),
                contentDescription = null,
                modifier = Modifier
                    .noRippleClickable(onBackClick)
                    .padding(horizontal = 4.dp),
            )
        }
        HorizontalDivider(thickness = 1.dp, color = KusTheme.colors.c_E0E0E0)
    }
}
