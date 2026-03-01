package com.kus.feature.community.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kus.designsystem.component.KusButton
import com.kus.designsystem.theme.KusTheme
import com.kus.domain.community.model.CommunityPost
import com.kus.feature.community.ui.comment.CommentTreeItem
import com.kus.feature.community.model.ReactionAction
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kustaurant.shared.core.designsystem.generated.resources.Res as CoreRes
import kustaurant.shared.core.designsystem.generated.resources.ic_like
import kustaurant.shared.core.designsystem.generated.resources.ic_scrap
import kustaurant.shared.core.designsystem.generated.resources.ic_user_placeholder
import org.jetbrains.compose.resources.painterResource
import kotlin.collections.isNotEmpty
import kotlin.collections.orEmpty

@Composable
fun CommunityDetailSuccessContent(
    post: CommunityPost,
    modifier: Modifier = Modifier,
    onPostLikeReactClick: () -> Unit = {},
    onCommentReact: (Long, ReactionAction) -> Unit = { _, _ -> },
    onScrapClick: () -> Unit = {},
    onReplyClick: (Long) -> Unit = {},
    onDeleteComment: (Long) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(KusTheme.colors.c_FFFFFF),
    ) {
        item { DetailAuthorHeader(post = post) }
        item { Spacer(Modifier.height(10.dp)) }

        item {
            Text(
                text = post.title,
                style = KusTheme.typography.type18b,
                color = KusTheme.colors.c_323232,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item { Spacer(Modifier.height(12.dp)) }

        item {
            HtmlBodyView(
                html = post.body,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item { Spacer(Modifier.height(20.dp)) }

        item {
            DetailStatsRow(
                post = post,
                onLikeClick = { onPostLikeReactClick() },
                onScrapClick = { onScrapClick() }
            )
        }

        item { Spacer(Modifier.height(20.dp)) }

        item { HorizontalDivider(color = KusTheme.colors.c_EAEAEA, thickness = 8.dp) }

        item { Spacer(Modifier.height(8.dp)) }

        val comments = post.comments.orEmpty()

        if (comments.isNotEmpty()) {
            items(
                items = comments,
                key = { it.commentId }
            ) { comment ->
                CommentTreeItem(
                    comment = comment,
                    indentLevel = 0,
                    onReplyClick = onReplyClick,
                    onDeleteComment = onDeleteComment,
                    onLike = { id -> onCommentReact(id, ReactionAction.LIKE) },
                    onDislike = { id -> onCommentReact(id, ReactionAction.DISLIKE) }
                )
            }
        }

        item { Spacer(Modifier.height(12.dp)) }
    }
}

@Composable
private fun DetailAuthorHeader(post: CommunityPost) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!post.writerIconUrl.isNullOrBlank()) {
            KamelImage(
                resource = asyncPainterResource(post.writerIconUrl!!),
                contentDescription = "사용자 이미지입니다.",
                modifier = Modifier.size(25.dp),
                onFailure = {
                    Image(
                        painter = painterResource(CoreRes.drawable.ic_user_placeholder),
                        contentDescription = "기본 프로필 이미지 입니다.",
                        modifier = Modifier.size(25.dp)
                    )
                }
            )
        } else {
            Image(
                painter = painterResource(CoreRes.drawable.ic_user_placeholder),
                contentDescription = "기본 프로필 이미지 입니다.",
                modifier = Modifier.size(25.dp)
            )
        }

        Spacer(Modifier.width(4.dp))

        Text(
            text = post.nickname,
            style = KusTheme.typography.type12r,
            color = KusTheme.colors.c_666666,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .widthIn(min = 32.dp)
                .weight(1f, fill = false)
        )

        Spacer(Modifier.width(4.dp))

        Text(
            text = "|",
            style = KusTheme.typography.type12r,
            color = KusTheme.colors.c_E0E0E0
        )

        Spacer(Modifier.width(2.dp))

        Text(
            text = post.timeAgo ?: "",
            style = KusTheme.typography.type12r,
            color = KusTheme.colors.c_666666
        )
    }
}

@Composable
private fun DetailStatsRow(
    post: CommunityPost,
    onLikeClick: () -> Unit,
    onScrapClick: () -> Unit
) {
    val isLiked = post.myReaction == "LIKE"
    val likeTint = if (isLiked) KusTheme.colors.c_43AB38 else KusTheme.colors.c_AAAAAA

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "조회수 ${post.visitCount}   댓글 ${post.commentCount}   추천 ${post.likeOnlyCount}",
            style = KusTheme.typography.type12r,
            color = KusTheme.colors.c_323232,
        )

        Spacer(Modifier.weight(1f))

        KusButton(
            enabled = true,
            buttonName = post.likeOnlyCount.toString(),
            roundedCornerShape = RoundedCornerShape(100.dp),
            textStyle = KusTheme.typography.type14r,
            contentColor = likeTint,
            containerColor = KusTheme.colors.c_FFFFFF,
            borderColor = likeTint,
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 16.dp),
            leftIcon = painterResource(CoreRes.drawable.ic_like),
            onClick = onLikeClick,
            modifier = Modifier.size(width = 68.dp, height = 29.dp)
        )

        Spacer(modifier = Modifier.width(7.dp))

        KusButton(
            enabled = true,
            buttonName = post.scrapCount.toString(),
            roundedCornerShape = RoundedCornerShape(100.dp),
            textStyle = KusTheme.typography.type14r,
            contentColor = KusTheme.colors.c_AAAAAA,
            containerColor = KusTheme.colors.c_FFFFFF,
            borderColor = KusTheme.colors.c_AAAAAA,
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 16.dp),
            leftIcon = painterResource(CoreRes.drawable.ic_scrap),
            onClick = onScrapClick,
            modifier = Modifier.size(width = 68.dp, height = 29.dp)
        )
    }
}
