package com.kus.designSystemShowcase.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kus.designsystem.component.KusChip
@Composable
fun KusChipPreviewScreen(
    modifier: Modifier = Modifier
) {
    var isAffiliateSelected by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxSize()
    ) {
        KusChip(
            text = "일식",
            selected = true,
            isSelectable = false,
            onClick = {}
        )

        KusChip(
            text = "제휴업체",
            selected = isAffiliateSelected,
            isSelectable = true,
            onClick = { isAffiliateSelected = !isAffiliateSelected }
        )
    }
}
