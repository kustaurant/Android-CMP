package com.kus.feature.detail.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.kus.designsystem.component.KusRatingBar
import com.kus.designsystem.component.KusReactionButton
import com.kus.designsystem.component.ReactionType
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import com.kus.shared.domain.model.detail.RestaurantReview
import com.kus.shared.domain.model.detail.ReviewComment
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kustaurant.shared.feature.detail.generated.resources.Res
import kustaurant.shared.feature.detail.generated.resources.ic_comment_arrow
import kustaurant.shared.feature.detail.generated.resources.ic_delete
import kustaurant.shared.feature.detail.generated.resources.ic_more
import kustaurant.shared.feature.detail.generated.resources.ic_report
import kustaurant.shared.feature.detail.generated.resources.img_rest_example
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun DetailReviewItem(
    review: RestaurantReview,
    modifier: Modifier = Modifier,
    onReviewLikeClick: (Int) -> Unit = {},
    onReviewDislikeClick: (Int) -> Unit = {},
    onCommentClick: (Int) -> Unit = {},
    onCommentLikeClick: (Int, Int) -> Unit = { _, _ -> },
    onCommentDislikeClick: (Int, Int) -> Unit = { _, _ -> },
    onReviewDeleteClick: (Int) -> Unit = {},
    onReviewReportClick: (Int) -> Unit = {},
    onCommentDeleteClick: (Int, Int) -> Unit = { _, _ -> },
    onCommentReportClick: (Int, Int) -> Unit = { _, _ -> },
) {
    Column(
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        ReviewContent(
            writerIconImgUrl = review.writerIconImgUrl,
            writerNickname = review.writerNickname,
            rating = review.evalScore.toFloat(),
            timeAgo = review.timeAgo,
            body = review.evalBody,
            reactionType = review.reactionType,
            likeCount = review.evalLikeCount,
            dislikeCount = review.evalDislikeCount,
            isMine = review.isEvaluationMine,
            onLikeClick = { onReviewLikeClick(review.evalId) },
            onDislikeClick = { onReviewDislikeClick(review.evalId) },
            onCommentClick = { onCommentClick(review.evalId) },
            onDeleteClick = { onReviewDeleteClick(review.evalId) },
            onReportClick = { onReviewReportClick(review.evalId) },
        )

        if (review.evalCommentList.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                review.evalCommentList.forEach { comment ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.ic_comment_arrow),
                            contentDescription = null
                        )

                        ReviewCommentItem(
                            comment = comment,
                            onLikeClick = { onCommentLikeClick(review.evalId, comment.commentId) },
                            onDislikeClick = {
                                onCommentDislikeClick(
                                    review.evalId,
                                    comment.commentId
                                )
                            },
                            onDeleteClick = {
                                onCommentDeleteClick(
                                    review.evalId,
                                    comment.commentId
                                )
                            },
                            onReportClick = {
                                onCommentReportClick(
                                    review.evalId,
                                    comment.commentId
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewContent(
    writerIconImgUrl: String,
    writerNickname: String,
    rating: Float,
    timeAgo: String,
    body: String,
    reactionType: String,
    likeCount: Int,
    dislikeCount: Int,
    isMine: Boolean,
    isComment: Boolean = false,
    onLikeClick: () -> Unit = {},
    onDislikeClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onReportClick: () -> Unit = {},
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ReviewAvatar(
            url = writerIconImgUrl
        )

        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f)
        ) {
            Text(
                text = writerNickname,
                style = KusTheme.typography.type14m.copy(
                    color = KusTheme.colors.c_000000
                )
            )

            Row(
                modifier = Modifier.padding(top = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isComment) {
                    KusRatingBar(
                        rating = rating,
                        isEnabled = false,
                        starModifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = timeAgo,
                    modifier = Modifier.padding(start = if (isComment) 0.dp else 6.dp),
                    style = KusTheme.typography.type12r.copy(
                        color = KusTheme.colors.c_9BA5B0
                    )
                )
            }
        }

        Box {
            Image(
                painter = painterResource(Res.drawable.ic_more),
                modifier = Modifier.noRippleClickable { isMenuExpanded = true },
                contentDescription = null
            )

            ReviewActionPopup(
                expanded = isMenuExpanded,
                isMine = isMine,
                density = density,
                onDismissRequest = { isMenuExpanded = false },
                onDeleteClick = onDeleteClick,
                onReportClick = onReportClick
            )
        }
    }

    if (body.isNotEmpty()) {
        Text(
            text = body,
            modifier = Modifier.padding(top = 10.dp),
            style = KusTheme.typography.type14r.copy(
                color = KusTheme.colors.c_000000
            )
        )
    }

    if (!isComment) {
        KusReactionButton(
            modifier = Modifier.padding(top = 12.dp),
            likeText = likeCount.toString(),
            dislikeText = dislikeCount.toString(),
            selectedType = reactionType.toReactionType(),
            onLikeClick = onLikeClick,
            onDislikeClick = onDislikeClick,
            onCommentClick = onCommentClick,
        )
    } else {
        KusReactionButton(
            modifier = Modifier.padding(top = 8.dp),
            likeText = likeCount.toString(),
            dislikeText = dislikeCount.toString(),
            selectedType = reactionType.toReactionType(),
            isCommentVisible = false,
            onLikeClick = onLikeClick,
            onDislikeClick = onDislikeClick,
            onCommentClick = onCommentClick,
        )
    }
}

@Composable
private fun ReviewCommentItem(
    comment: ReviewComment,
    onLikeClick: () -> Unit = {},
    onDislikeClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onReportClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(
                color = KusTheme.colors.c_EAEAEA,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(12.dp)
    ) {
        ReviewContent(
            writerIconImgUrl = comment.writerIconImgUrl,
            writerNickname = comment.writerNickname,
            rating = 0f,
            timeAgo = comment.timeAgo,
            body = comment.commentBody,
            reactionType = comment.reactionType,
            likeCount = comment.commentLikeCount,
            dislikeCount = comment.commentDislikeCount,
            isMine = comment.isCommentMine,
            isComment = true,
            onLikeClick = onLikeClick,
            onDislikeClick = onDislikeClick,
            onDeleteClick = onDeleteClick,
            onReportClick = onReportClick,
        )
    }
}

@Composable
private fun ReviewActionPopup(
    expanded: Boolean,
    isMine: Boolean,
    density: Density,
    onDismissRequest: () -> Unit,
    onDeleteClick: () -> Unit,
    onReportClick: () -> Unit,
) {
    var shouldRender by remember { mutableStateOf(expanded) }
    val alpha by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        animationSpec = tween(durationMillis = 220),
        finishedListener = { value ->
            if (!expanded && value == 0f) {
                shouldRender = false
            }
        }
    )

    val positionProvider = remember(density) {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ): IntOffset {
                val x = anchorBounds.right - popupContentSize.width
                val y = anchorBounds.bottom + with(density) { 4.dp.roundToPx() }
                return IntOffset(x, y)
            }
        }
    }

    LaunchedEffect(expanded) {
        if (expanded) {
            shouldRender = true
        }
    }

    if (!shouldRender) return

    Popup(
        onDismissRequest = onDismissRequest,
        popupPositionProvider = positionProvider,
        properties = PopupProperties(focusable = true)
    ) {
        val (icon, text, actionClick) = if (isMine) {
            Triple(Res.drawable.ic_delete, "삭제하기", onDeleteClick)
        } else {
            Triple(Res.drawable.ic_report, "신고하기", onReportClick)
        }

        Surface(
            shape = RoundedCornerShape(10.dp),
            color = KusTheme.colors.c_E0E0E0,
            modifier = Modifier.alpha(alpha)
        ) {
            ReviewPopUpItem(
                icon = icon,
                text = text,
                onClick = {
                    onDismissRequest()
                    actionClick()
                }
            )
        }
    }
}

@Composable
private fun ReviewPopUpItem(
    icon: DrawableResource,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .noRippleClickable(onClick = onClick)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(38.dp)
    ) {
        Text(
            text = text,
            style = KusTheme.typography.type14r.copy(
                color = KusTheme.colors.c_666666
            ),
        )
        Image(
            painter = painterResource(icon),
            contentDescription = null,
        )
    }
}

@Composable
private fun ReviewAvatar(
    url: String,
) {
    val size = 40.dp
    if (url.startsWith("https")) {
        KamelImage(
            resource = { asyncPainterResource(url) },
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(size)
                .clip(RoundedCornerShape(100.dp)),
            onFailure = {
                Image(
                    painter = painterResource(Res.drawable.img_rest_example),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(size)
                        .clip(RoundedCornerShape(100.dp))
                )
            }
        )
    } else {
        Image(
            painter = painterResource(Res.drawable.img_rest_example),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(size)
                .clip(RoundedCornerShape(100.dp))
        )
    }
}

private fun String.toReactionType(): ReactionType? {
    return when (uppercase()) {
        "LIKE" -> ReactionType.Like
        "DISLIKE" -> ReactionType.DisLike
        else -> null
    }
}
