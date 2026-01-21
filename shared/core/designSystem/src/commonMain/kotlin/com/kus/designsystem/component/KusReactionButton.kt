package com.kus.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import kustaurant.shared.core.designsystem.generated.resources.Res
//import kustaurant.core.designsystem.generated.resources.ic_dislike
//import kustaurant.core.designsystem.generated.resources.ic_like
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * 좋아요/싫어요 반응 타입
 */
enum class ReactionType {
    Like,
    DisLike
}

/**
 * 쿠스토랑 반응 버튼
 * @param modifier : 버튼 전체 컴포넌트 modifier
 * @param likeText : 좋아요 개수
 * @param dislikeText : 싫어요 개수
 * @param selectedType : 현재 선택된 타입 (null 선택 안됨)
 * @param enabled : 클릭 가능 여부
 * @param onLikeClick : 좋아요 클릭 시 호출되는 함수
 * @param onDislikeClick : 싫어요 클릭 시 호출되는 함수
 */
@Composable
fun KusReactionButton(
    modifier: Modifier = Modifier,
    likeText: String,
    dislikeText: String,
    selectedType: ReactionType? = null,
    enabled: Boolean = true,
    onLikeClick: () -> Unit = {},
    onDislikeClick: () -> Unit = {},
) {
    val isLikeSelected = selectedType == ReactionType.Like
    val isDislikeSelected = selectedType == ReactionType.DisLike

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 좋아요 버튼
        ReactionButtonItem(
            iconRes = vectorResource(Res.drawable.ic_like),
            text = likeText,
            selected = isLikeSelected,
            enabled = enabled,
            onClick = onLikeClick,
            contentDescription = "좋아요"
        )

        // 싫어요 버튼
        ReactionButtonItem(
            iconRes = vectorResource(Res.drawable.ic_dislike),
            text = dislikeText,
            selected = isDislikeSelected,
            enabled = enabled,
            onClick = onDislikeClick,
            contentDescription = "싫어요"
        )
    }
}

@Composable
private fun ReactionButtonItem(
    iconRes: ImageVector,
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    contentDescription: String
) {
    val iconColor = if (selected) {
        KusTheme.colors.c_43AB38
    } else {
        KusTheme.colors.c_AAAAAA
    }

    val textColor = if (selected) {
        KusTheme.colors.c_43AB38
    } else {
        KusTheme.colors.c_AAAAAA
    }

    Row(
        modifier = Modifier
            .then(
                if (enabled) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = iconRes,
            contentDescription = contentDescription,
            modifier = Modifier.size(16.dp),
            tint = iconColor
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            style = KusTheme.typography.type14r.copy(
                color = textColor
            )
        )
    }
}

@Preview(showBackground = true, name = "클릭 인터랙션 테스트")
@Composable
fun KusReactionButtonInteractivePreview() {
    KusTheme {
        var selectedType by remember { mutableStateOf<ReactionType?>(null) }
        var likeCount by remember { mutableIntStateOf(123) }
        var dislikeCount by remember { mutableIntStateOf(45) }

        KusReactionButton(
            likeText = likeCount.toString(),
            dislikeText = dislikeCount.toString(),
            selectedType = selectedType,
            enabled = true,
            onLikeClick = {
                if (selectedType == ReactionType.Like) {
                    // 이미 선택된 경우 취소
                    selectedType = null
                    likeCount--
                } else {
                    // 좋아요 선택
                    if (selectedType == ReactionType.DisLike) {
                        // 싫어요가 선택되어 있었다면 취소
                        dislikeCount--
                    }
                    selectedType = ReactionType.Like
                    likeCount++
                }
            },
            onDislikeClick = {
                if (selectedType == ReactionType.DisLike) {
                    // 이미 선택된 경우 취소
                    selectedType = null
                    dislikeCount--
                } else {
                    // 싫어요 선택
                    if (selectedType == ReactionType.Like) {
                        // 좋아요가 선택되어 있었다면 취소
                        likeCount--
                    }
                    selectedType = ReactionType.DisLike
                    dislikeCount++
                }
            }
        )
    }
}
