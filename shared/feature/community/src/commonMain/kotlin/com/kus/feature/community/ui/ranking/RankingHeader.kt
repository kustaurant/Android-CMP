package com.kus.feature.community.ui.ranking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kus.designsystem.component.KusChip
import com.kus.designsystem.theme.KusTheme
import com.kus.domain.community.model.RankingSortType


@Composable
fun RankingHeader(
    selectedSort: RankingSortType,
    onSortChange: (RankingSortType) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "맛집 평가에 참여하고 랭킹에 올라보세요!",
            style = KusTheme.typography.type13r,
            color = KusTheme.colors.c_AAAAAA,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(18.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 18.dp)
        ) {
            KusChip(
                chipName = "분기순",
                isSelected = selectedSort == RankingSortType.SEASONAL,
                onClick = { onSortChange(RankingSortType.SEASONAL) },
                modifier = Modifier.height(29.dp)
            )

            KusChip(
                chipName = "누적순",
                isSelected = selectedSort == RankingSortType.CUMULATIVE,
                onClick = { onSortChange(RankingSortType.CUMULATIVE) },
                modifier = Modifier.height(29.dp)
            )
        }
    }
}
