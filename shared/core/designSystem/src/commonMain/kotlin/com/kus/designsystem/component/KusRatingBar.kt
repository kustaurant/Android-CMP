package com.kus.designsystem.component

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import kustaurant.shared.core.designsystem.generated.resources.Res
import kustaurant.shared.core.designsystem.generated.resources.ic_empty_star
import kustaurant.shared.core.designsystem.generated.resources.ic_filled_star
import kustaurant.shared.core.designsystem.generated.resources.ic_half_star
//import kustaurant.core.designsystem.generated.resources.ic_empty_star
//import kustaurant.core.designsystem.generated.resources.ic_filled_star
//import kustaurant.core.designsystem.generated.resources.ic_half_star
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.round

/**
 * 쿠스토랑 별점 바
 * @param modifier : 별점 전체 컴포넌트 modifier
 * @param rating : 초기 rating 값
 * @param isEnabled : 별점 변경 가능 불가능
 * @param onRatingChange : 별점 변경에 따른 함수 반환
 * @param starModifier : 각 별의 modifier
 */
@Composable
fun KusRatingBar(
    modifier: Modifier = Modifier,
    rating: Float = 0f,
    isEnabled: Boolean = true,
    onRatingChange: (Float) -> Unit = {},
    starModifier: Modifier = Modifier.size(24.dp)
) {
    var currentRating by remember { mutableFloatStateOf(rating.coerceIn(0f, 5f)) }
    var containerWidth by remember { mutableIntStateOf(0) }

    fun updateRatingFromPosition(x: Float) {
        if (containerWidth <= 0) return
        val newRating = (x / containerWidth * 5).coerceIn(0.5f, 5f)
        val roundedRating = (round(newRating * 2) / 2).coerceIn(0.5f, 5f)
        if (roundedRating != currentRating) {
            currentRating = roundedRating
            onRatingChange(roundedRating)
        }
    }

    LaunchedEffect(rating, isEnabled) {
        if (!isEnabled) {
            currentRating = rating.coerceIn(0f, 5f)
        }
    }

    Row(
        modifier = modifier
            .onSizeChanged { size ->
                containerWidth = size.width
            }
            .then(
                if (isEnabled) {
                    Modifier.pointerInput(isEnabled) {
                        detectTapGestures { offset ->
                            updateRatingFromPosition(offset.x)
                        }
                    }.pointerInput(isEnabled) {
                        detectDragGestures(
                            onDrag = { change, _ ->
                                updateRatingFromPosition(change.position.x)
                            }
                        )
                    }
                } else {
                    Modifier
                }
            ),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(5) { index ->
            val starIndex = index + 1
            val starRating = currentRating - index

            when {
                starRating >= 1f -> {
                    // 완전히 채워진 별
                    Icon(
                        painter = painterResource(Res.drawable.ic_filled_star),
                        contentDescription = "별점 $starIndex",
                        modifier = starModifier,
                        tint = Color.Unspecified
                    )
                }
                starRating >= 0.5f -> {
                    // 반채워진 별
                    Icon(
                        painter = painterResource(Res.drawable.ic_half_star),
                        contentDescription = "별점 $starIndex",
                        modifier = starModifier,
                        tint = Color.Unspecified
                    )
                }
                else -> {
                    // 빈 별
                    Icon(
                        painter = painterResource(Res.drawable.ic_empty_star),
                        contentDescription = "별점 $starIndex",
                        modifier = starModifier,
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "드래그 가능 별점")
@Composable
fun KusRatingBarInteractivePreview() {
    KusRatingBar(
        modifier = Modifier.padding(16.dp),
        rating = 2.5f,
        isEnabled = true,
        onRatingChange = { },
        starModifier = Modifier.size(40.dp)
    )
}

@Preview(showBackground = true, name = "읽기 전용 별점")
@Composable
fun KusRatingBarReadOnlyPreview() {
    KusRatingBar(
        modifier = Modifier.padding(16.dp),
        rating = 3.5f,
        isEnabled = false
    )
}
