package com.kus.feature.detail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.kus.designsystem.component.KusRatingBar
import com.kus.designsystem.component.KusReactionButton
import com.kus.designsystem.component.ReactionType
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.detail.ui.DetailReview
import com.kus.feature.detail.ui.DetailReviewComment
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kustaurant.shared.feature.detail.generated.resources.Res
import kustaurant.shared.feature.detail.generated.resources.img_rest_example
import org.jetbrains.compose.resources.painterResource

@Composable
fun DetailReviewItem(
    review: DetailReview,
    modifier: Modifier = Modifier,
    onReviewLikeClick: (Int) -> Unit = {},
    onReviewDislikeClick: (Int) -> Unit = {},
    onCommentLikeClick: (Int, Int) -> Unit = { _, _ -> },
    onCommentDislikeClick: (Int, Int) -> Unit = { _, _ -> },
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
            onLikeClick = { onReviewLikeClick(review.evalId) },
            onDislikeClick = { onReviewDislikeClick(review.evalId) },
        )

        if (review.evalCommentList.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(start = 22.dp, top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                review.evalCommentList.forEach { comment ->
                    ReviewCommentItem(
                        comment = comment,
                        onLikeClick = { onCommentLikeClick(review.evalId, comment.commentId) },
                        onDislikeClick = { onCommentDislikeClick(review.evalId, comment.commentId) },
                    )
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
    isComment: Boolean = false,
    onLikeClick: () -> Unit = {},
    onDislikeClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth()
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
                modifier = Modifier.padding(top = if (isComment) 2.dp else 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isComment) {
                    KusRatingBar(
                        rating = rating,
                        isEnabled = false,
                        starModifier = Modifier.size(14.dp)
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
    }

    Text(
        text = body,
        modifier = Modifier.padding(top = if (isComment) 4.dp else 8.dp),
        style = KusTheme.typography.type14r.copy(
            color = KusTheme.colors.c_000000
        )
    )

    if (!isComment) {
        KusReactionButton(
            modifier = Modifier.padding(top = 10.dp),
            likeText = likeCount.toString(),
            dislikeText = dislikeCount.toString(),
            selectedType = reactionType.toReactionType(),
            onLikeClick = onLikeClick,
            onDislikeClick = onDislikeClick,
        )
    } else {
        KusReactionButton(
            modifier = Modifier.padding(top = 6.dp),
            likeText = likeCount.toString(),
            dislikeText = dislikeCount.toString(),
            selectedType = reactionType.toReactionType(),
            onLikeClick = onLikeClick,
            onDislikeClick = onDislikeClick,
        )
    }
}

@Composable
private fun ReviewCommentItem(
    comment: DetailReviewComment,
    onLikeClick: () -> Unit = {},
    onDislikeClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(
                color = KusTheme.colors.c_EAEAEA,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
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
            isComment = true,
            onLikeClick = onLikeClick,
            onDislikeClick = onDislikeClick,
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
            resource = asyncPainterResource(url),
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
