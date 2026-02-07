package com.kus.feature.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme

@Composable
fun DetailRestInfoTag(
    situationList: ArrayList<String>,
) {
    LazyRow(
        contentPadding = PaddingValues(end = 4.dp)
    ) {
        items(situationList) { situation ->
            val tagText = if (situation.startsWith("#")) situation else "#$situation"
            Text(
                text = tagText,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .background(
                        color = KusTheme.colors.c_F5F5F5,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                style = KusTheme.typography.type12r.copy(
                    color = KusTheme.colors.c_666666
                )
            )
        }
    }
}
