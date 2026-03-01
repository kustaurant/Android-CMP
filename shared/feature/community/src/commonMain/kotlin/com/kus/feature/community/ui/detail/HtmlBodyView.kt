package com.kus.feature.community.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
expect fun HtmlBodyView(
    html: String,
    modifier: Modifier = Modifier
)
