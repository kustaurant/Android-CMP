package com.kus.feature.community.ui.comment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import com.kus.feature.community.ui.model.CommunityPostCommentUi
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kustaurant.shared.core.designsystem.generated.resources.ic_chat
import kustaurant.shared.core.designsystem.generated.resources.ic_comment_reply
import kustaurant.shared.core.designsystem.generated.resources.ic_delete
import kustaurant.shared.core.designsystem.generated.resources.ic_dislike
import kustaurant.shared.core.designsystem.generated.resources.ic_like
import kustaurant.shared.core.designsystem.generated.resources.ic_more_vert
import kustaurant.shared.core.designsystem.generated.resources.ic_user_placeholder
import org.jetbrains.compose.resources.painterResource
import kustaurant.shared.core.designsystem.generated.resources.Res as CoreRes


@Composable
fun CommentTreeItem(
    comment: CommunityPostCommentUi,
    indentLevel: Int,
    modifier: Modifier = Modifier,
    onLike: (Long) -> Unit = {},
    onDislike: (Long) -> Unit = {},
    onDeleteComment: (Long) -> Unit = {},
    onReplyClick: (Long) -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {

        if (indentLevel == 0) {
            RootCommentCard(
                comment = comment,
                onLike = onLike,
                onDislike = onDislike,
                onDeleteComment = onDeleteComment,
                onReplyClick = onReplyClick,
            )
        } else {
            ReplyCommentCard(
                comment = comment,
                onLike = onLike,
                onDislike = onDislike,
                onDeleteComment = onDeleteComment,
            )
        }

        comment.repliesList.forEach { child ->
            CommentTreeItem(
                comment = child,
                indentLevel = indentLevel + 1,
                onLike = onLike,
                onDislike = onDislike,
                onDeleteComment = onDeleteComment,
                onReplyClick = onReplyClick,
            )
        }

        if (indentLevel == 0) {
            HorizontalDivider(
                thickness = 2.dp,
                color = KusTheme.colors.c_E0E0E0,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            )
        }
    }
}

@Composable
private fun RootCommentCard(
    comment: CommunityPostCommentUi,
    onLike: (Long) -> Unit,
    onDislike: (Long) -> Unit,
    onDeleteComment: (Long) -> Unit,
    onReplyClick: (Long) -> Unit,
) {
    var moreExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
            .padding(vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CommentAvatar(url = comment.writerIconUrl)

            Spacer(Modifier.width(8.dp))

            Text(
                text = comment.nickname.orEmpty(),
                style = KusTheme.typography.type13b,
                color = KusTheme.colors.c_323232,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.widthIn(min = 32.dp)
            )

            Spacer(Modifier.width(2.dp))

            Text(
                text = "|",
                style = KusTheme.typography.type12r,
                color = KusTheme.colors.c_E0E0E0
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = comment.timeAgo,
                style = KusTheme.typography.type12r,
                color = KusTheme.colors.c_AAAAAA
            )

            Spacer(Modifier.weight(1f))

            if(comment.isCommentMine) {
                Box {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .padding(end = 10.dp)
                            .noRippleClickable { moreExpanded = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(CoreRes.drawable.ic_more_vert),
                            contentDescription = "대댓글 더보기 아이콘입니다.",
                            tint = KusTheme.colors.c_E0E0E0,
                        )
                    }

                    DropdownMenu(
                        expanded = moreExpanded,
                        onDismissRequest = { moreExpanded = false },
                        shape = RoundedCornerShape(10.dp),
                        containerColor = KusTheme.colors.c_E0E0E0,
                        offset = DpOffset(x = (-120 + 20).dp, y = (-6).dp),
                        modifier = Modifier.width(120.dp)
                    ) {
                        DropdownMenuItem(
                            modifier = Modifier.height(32.dp),
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "삭제하기",
                                        style = KusTheme.typography.type14r,
                                        color = KusTheme.colors.c_666666
                                    )

                                    Icon(
                                        painter = painterResource(CoreRes.drawable.ic_delete),
                                        contentDescription = "삭제하기 아이콘입니다."
                                    )
                                }
                            },
                            onClick = {
                                moreExpanded = false
                                onDeleteComment(comment.commentId)
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = if (comment.isDeleted) "삭제된 댓글입니다." else comment.body,
            style = KusTheme.typography.type14r,
            color = KusTheme.colors.c_323232,
            modifier = Modifier.padding(end = 16.dp, bottom = 8.dp)
        )

        Spacer(Modifier.height(10.dp))

        ReactionRow(
            likeCount = comment.likeCount,
            dislikeCount = comment.dislikeCount,
            onLike = { onLike(comment.commentId) },
            onDislike = { onDislike(comment.commentId) },
            selectedReaction = comment.reactionType,
            showReplyAction = true,
            onReplyClick = { onReplyClick(comment.commentId) }
        )
    }
}

@Composable
private fun ReplyCommentCard(
    comment: CommunityPostCommentUi,
    onLike: (Long) -> Unit,
    onDislike: (Long) -> Unit,
    onDeleteComment: (Long) -> Unit,
) {
    var moreExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(CoreRes.drawable.ic_comment_reply),
            contentDescription = "대댓글 아이콘입니다.",
        )

        Spacer(modifier = Modifier.width(6.dp))

        Box(modifier = Modifier.weight(1f)) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = KusTheme.colors.c_EAEAEA,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 10.dp, horizontal = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CommentAvatar(url = comment.writerIconUrl)

                        Spacer(Modifier.width(8.dp))

                        Text(
                            text = comment.nickname.orEmpty(),
                            style = KusTheme.typography.type13b,
                            color = KusTheme.colors.c_323232,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.widthIn(min = 32.dp)
                        )

                        Spacer(Modifier.width(2.dp))

                        Text(
                            text = "|",
                            style = KusTheme.typography.type12r,
                            color = KusTheme.colors.c_E0E0E0
                        )

                        Spacer(Modifier.width(8.dp))

                        Text(
                            text = comment.timeAgo,
                            style = KusTheme.typography.type12r,
                            color = KusTheme.colors.c_AAAAAA
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = comment.body,
                        style = KusTheme.typography.type13r,
                        color = KusTheme.colors.c_666666
                    )

                    Spacer(Modifier.height(10.dp))

                    ReactionRow(
                        likeCount = comment.likeCount,
                        dislikeCount = comment.dislikeCount,
                        onLike = { onLike(comment.commentId) },
                        onDislike = { onDislike(comment.commentId) },
                        selectedReaction = comment.reactionType,
                    )
                }
            }

            if(comment.isCommentMine) {
                Box(modifier = Modifier.align(Alignment.TopEnd).padding(top = 2.dp, end = 4.dp)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .noRippleClickable { moreExpanded = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(CoreRes.drawable.ic_more_vert),
                            contentDescription = "대댓글 더보기 아이콘입니다.",
                            tint = KusTheme.colors.c_E0E0E0,
                        )
                    }

                    DropdownMenu(

                        expanded = moreExpanded,
                        onDismissRequest = { moreExpanded = false },
                        shape = RoundedCornerShape(10.dp),
                        containerColor = KusTheme.colors.c_E0E0E0,
                        offset = DpOffset(x = (-120 + 22).dp, y = (-6).dp),
                        modifier = Modifier.width(120.dp)
                    ) {
                        DropdownMenuItem(
                            modifier = Modifier.height(32.dp),
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "삭제하기",
                                        style = KusTheme.typography.type14r,
                                        color = KusTheme.colors.c_666666
                                    )
                                    Icon(
                                        painter = painterResource(CoreRes.drawable.ic_delete),
                                        contentDescription = "삭제하기 아이콘입니다."
                                    )
                                }
                            },
                            onClick = {
                                moreExpanded = false
                                onDeleteComment(comment.commentId)
                            }
                        )
                    }
                }
            }
        }
    }

    Spacer(Modifier.height(8.dp))
}

@Composable
private fun CommentAvatar(
    url: String?,
    size: Dp = 25.dp
) {
    if (!url.isNullOrBlank()) {
        KamelImage(
            resource = asyncPainterResource(url),
            contentDescription = "사용자 아이콘입니다.",
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(100.dp))
        )
    } else {
        Image(
            painter = painterResource(CoreRes.drawable.ic_user_placeholder),
            contentDescription = "사용자 기본 아이콘입니다.",
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(100.dp))
        )
    }
}

@Composable
private fun ReactionRow(
    likeCount: Int,
    dislikeCount: Int,
    onLike: () -> Unit,
    onDislike: () -> Unit,
    selectedReaction: String?,
    showReplyAction: Boolean = false,
    onReplyClick: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ReactionIconWithCount(
            painter = painterResource(CoreRes.drawable.ic_like),
            count = likeCount,
            selected = selectedReaction == "LIKE",
            onClick = onLike,
            activeColor = KusTheme.colors.c_43AB38
        )
        ReactionIconWithCount(
            painter = painterResource(CoreRes.drawable.ic_dislike),
            count = dislikeCount,
            selected = selectedReaction == "DISLIKE",
            onClick = onDislike,
            activeColor = KusTheme.colors.c_FF0000
        )

        if (showReplyAction) {
            Image(
                painter = painterResource(CoreRes.drawable.ic_chat),
                contentDescription = "대댓글 작성 아이콘입니다.",
                modifier = Modifier.noRippleClickable { onReplyClick() }
            )
        }
    }
}

@Composable
private fun ReactionIconWithCount(
    painter: Painter,
    count: Int,
    selected: Boolean,
    onClick: () -> Unit,
    activeColor : Color,
) {
    val tint = if (selected) activeColor else KusTheme.colors.c_AAAAAA
    val textColor = if (selected) activeColor else KusTheme.colors.c_AAAAAA

    Row(
        modifier = Modifier.noRippleClickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painter,
            contentDescription = "아이콘 입니다.",
            tint = tint,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = count.toString(),
            style = KusTheme.typography.type12r,
            color = textColor
        )
    }
}