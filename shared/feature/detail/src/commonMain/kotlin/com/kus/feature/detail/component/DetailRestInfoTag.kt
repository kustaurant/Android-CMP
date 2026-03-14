package com.kus.feature.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailRestInfoTag(
    situationList: List<String>,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth()
            .padding(top = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        situationList.forEach { situation ->
            val tagText = if (situation.startsWith("#")) situation else "#$situation"
            Text(
                text = tagText,
                modifier = Modifier
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
