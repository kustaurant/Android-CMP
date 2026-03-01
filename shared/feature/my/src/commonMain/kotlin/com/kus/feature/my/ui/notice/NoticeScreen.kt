package com.kus.feature.my.ui.notice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import com.kus.feature.my.component.WebView
import kustaurant.shared.feature.my.generated.resources.Res
import kustaurant.shared.feature.my.generated.resources.ic_left_chevron
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun NoticeScreen(
    onBackClick: () -> Unit,
) {
    var isLoading by remember { mutableStateOf(true) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KusTheme.colors.c_FFFFFF),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "공지사항",
                style = KusTheme.typography.type17sb,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            Icon(
                painter = painterResource(Res.drawable.ic_left_chevron),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 20.dp)
                    .noRippleClickable(onBackClick)
                    .padding(horizontal = 4.dp),
            )
        }

        WebView(
            url = "https://kustaurant.com/notice",
            onLoadingChanged = { loading ->
                isLoading = loading
            },
            modifier = Modifier.weight(1f)
        )
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
