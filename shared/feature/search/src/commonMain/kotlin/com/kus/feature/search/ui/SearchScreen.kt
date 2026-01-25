package com.kus.feature.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import kustaurant.shared.feature.search.generated.resources.Res
import kustaurant.shared.feature.search.generated.resources.ic_left_chevron
import org.jetbrains.compose.resources.painterResource

@Composable
fun SearchScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(top = 16.dp)
            .background(KusTheme.colors.c_FFFFFF),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp, 0.dp, 20.dp, 16.dp),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_left_chevron),
                contentDescription = "뒤로가기 버튼",
            )
        }
    }
}