package com.kus.feature.evaluate.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kus.designsystem.component.KusChip
import com.kus.designsystem.theme.KusTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EvaluationKeyword(
    selectedSituations: List<Int>,
    onSituationChanged: (List<Int>) -> Unit
) {
    val situations = listOf(
        "혼밥", "2~4인", "5인 이상", "단체 회식",
        "배달", "야식", "친구 초대", "데이트", "소개팅"
    )

    Column(
        modifier = Modifier.padding(top = 30.dp)
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "키워드",
            style = KusTheme.typography.type18b.copy(
                color = KusTheme.colors.c_000000
            )
        )

        Text(
            text = "방문한 식당에 어울리는 키워드를 골라주세요)",
            style = KusTheme.typography.type14r.copy(
                color = KusTheme.colors.c_AAAAAA
            ),
            modifier = Modifier.padding(top = 4.dp)
        )

        FlowRow(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            situations.forEachIndexed { index, situation ->
                val situationIndex = index + 1
                KusChip(
                    chipName = situation,
                    isSelected = selectedSituations.contains(situationIndex),
                    onClick = {
                        val newList = if (selectedSituations.contains(situationIndex)) {
                            selectedSituations - situationIndex
                        } else {
                            selectedSituations + situationIndex
                        }
                        onSituationChanged(newList)
                    }
                )
            }
        }
    }
}