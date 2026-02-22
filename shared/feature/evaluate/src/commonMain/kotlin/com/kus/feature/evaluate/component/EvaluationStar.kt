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

@Composable
fun EvaluationStar(
    initialRating: Double = 0.5,
    onRatingChanged: (Double) -> Unit = {}
) {
    var currentRating by remember { mutableStateOf(initialRating.toFloat()) }

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
        ) {
            Text(
                text = "$currentRating",
                style = KusTheme.typography.type14b.copy(
                    color = KusTheme.colors.c_000000
                ),
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = "",
                style = KusTheme.typography.type14r.copy(
                    color = KusTheme.colors.c_666666
                )
            )
        }

    }
}