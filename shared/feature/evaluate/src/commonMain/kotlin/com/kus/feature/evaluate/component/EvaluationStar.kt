package com.kus.feature.evaluate.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kus.designsystem.component.KusRatingBar
import com.kus.designsystem.theme.KusTheme
import kotlin.math.roundToInt

@Composable
fun EvaluationStar(
    initialRating: Double = 0.0,
    onRatingChanged: (Double) -> Unit = {}
) {
    var currentRating by remember { mutableStateOf(initialRating.toFloat()) }
    val ratingDescription = when ((currentRating * 2).roundToInt()) {
        0 -> "별점을 입력해주세요"
        1 -> "다시 갈 것 같진 않아요"
        2 -> "많이 아쉬워요"
        3 -> "다른데 갈껄"
        4 -> "조금 아쉬워요"
        5 -> "무난했어요"
        6 -> "괜찮았어요"
        7 -> "만족스러웠어요"
        8 -> "무조건 한번 더 올 것 같아요"
        9 -> "행복했습니다"
        10 -> "인생 최고의 식당입니다"
        else -> "별점을 입력해주세요"
    }

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "별점",
            style = KusTheme.typography.type18b.copy(
                color = KusTheme.colors.c_000000
            )
        )

        Text(
            text = "해당 식당에 대해 평점을 남겨주세요 (최소 0.5점)",
            style = KusTheme.typography.type14r.copy(
                color = KusTheme.colors.c_AAAAAA
            ),
            modifier = Modifier.padding(top = 4.dp)
        )

        KusRatingBar(
            rating = currentRating,
            modifier = Modifier
                .padding(top = 12.dp),
            starModifier = Modifier.size(40.dp),
            onRatingChange = { newRating ->
                currentRating = newRating
                onRatingChanged(newRating.toDouble())
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Text(
                text = "$currentRating",
                style = KusTheme.typography.type14b.copy(
                    color = KusTheme.colors.c_000000
                ),
            )

            Text(
                text = ratingDescription,
                style = KusTheme.typography.type14r.copy(
                    color = KusTheme.colors.c_666666
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

    }
}