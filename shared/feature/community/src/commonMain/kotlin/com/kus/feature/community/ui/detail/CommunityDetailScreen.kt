package com.kus.feature.community.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.core.serialization.KusJson
import com.kus.core.serialization.RouteCodec
import com.kus.designsystem.component.KusCommentInput
import com.kus.designsystem.component.KusReplyCommentOverlay
import com.kus.designsystem.component.ReplyConfirmDialog
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import com.kus.feature.community.model.DeleteCommunityEvent
import com.kus.feature.community.model.toModifyPayload
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kustaurant.shared.core.designsystem.generated.resources.Res as CoreRes
import kustaurant.shared.core.designsystem.generated.resources.ic_arrow_back
import kustaurant.shared.core.designsystem.generated.resources.ic_more_vert
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CommunityDetailScreen(
    postId: Long,
    onBackButtonClick: () -> Unit = {},
    onPostModifyClick: (String) -> Unit = {},
    onPostDeleted: () -> Unit = {},
) {
    val viewModel: CommunityDetailViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showReplyConfirmDialog by rememberSaveable { mutableStateOf(false) }
    var showReplyComposer by rememberSaveable { mutableStateOf(false) }

    var pendingReplyParentId by rememberSaveable { mutableStateOf<Long?>(null) }
    var replyText by rememberSaveable { mutableStateOf("") }

    var commentText by rememberSaveable { mutableStateOf("") }
    val isMine = uiState.post?.isPostMine == true
    var moreExpanded by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    BackHandler { onBackButtonClick() }

    LaunchedEffect(postId) {
        viewModel.loadDetail(postId)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is DeleteCommunityEvent.Deleted -> onPostDeleted()
                is DeleteCommunityEvent.Error -> {}
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()){
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(KusTheme.colors.c_FFFFFF)
                        .height(64.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    IconButton(
                        onClick = onBackButtonClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            painter = painterResource(CoreRes.drawable.ic_arrow_back),
                            contentDescription = null
                        )
                    }

                    uiState.post?.category?.displayName?.let {
                        Text(
                            text = it,
                            style = KusTheme.typography.type17sb,
                            color = KusTheme.colors.c_323232,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    if (isMine) {
                        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                            IconButton(onClick = {
                                focusManager.clearFocus(force = true)
                                scope.launch {
                                    kotlinx.coroutines.delay(16)
                                    keyboardController?.hide()
                                    moreExpanded = true
                                }
                            }) {
                                Icon(
                                    painter = painterResource(CoreRes.drawable.ic_more_vert),
                                    contentDescription = null,
                                    tint = KusTheme.colors.c_AAAAAA
                                )
                            }

                            DropdownMenu(
                                expanded = moreExpanded,
                                onDismissRequest = { moreExpanded = false },
                                shape = RoundedCornerShape(10.dp),
                                containerColor = KusTheme.colors.c_E0E0E0,
                                offset = DpOffset(x = (-40).dp, y = 0.dp),
                                modifier = Modifier.width(80.dp)
                            ) {
                                DropdownMenuItem(
                                    modifier = Modifier.height(32.dp),
                                    text = {
                                        Box(
                                            modifier =
                                                Modifier
                                                    .fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "수정하기",
                                                style = KusTheme.typography.type14r,
                                                color = KusTheme.colors.c_666666
                                            )
                                        }
                                    },
                                    onClick = {
                                        moreExpanded = false
                                        val post = uiState.post ?: return@DropdownMenuItem
                                        val json = KusJson.json.encodeToString(post.toModifyPayload())
                                        val encoded = RouteCodec.encode(json)
                                        onPostModifyClick(encoded)
                                    }
                                )
                                DropdownMenuItem(
                                    modifier = Modifier.height(32.dp),
                                    text = {
                                        Box(
                                            modifier =
                                                Modifier
                                                    .fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "삭제하기",
                                                style = KusTheme.typography.type14r,
                                                color = KusTheme.colors.c_666666
                                            )
                                        }
                                    },
                                    onClick = {
                                        moreExpanded = false
                                        viewModel.deletePost(postId)
                                    }
                                )
                            }
                        }
                    }
                }
            },
            contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        ) { inner ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(KusTheme.colors.c_FFFFFF)
                    .padding(inner)
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            awaitFirstDown(pass = PointerEventPass.Final)
                            val up = waitForUpOrCancellation(pass = PointerEventPass.Final)
                            if (up != null) {
                                focusManager.clearFocus(force = true)
                                keyboardController?.hide()
                            }
                        }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(KusTheme.colors.c_FFFFFF)
                ) {
                    when (uiState.phase) {
                        CommunityDetailPhase.Idle, CommunityDetailPhase.Loading -> {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = KusTheme.colors.c_43AB38)
                            }
                        }

                        CommunityDetailPhase.Failure -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "불러오기에 실패했어요.\n 다시 시도해주세요.",
                                    style = KusTheme.typography.type14r,
                                    color = KusTheme.colors.c_666666
                                )
                                Spacer(Modifier.height(12.dp))
                                Text(
                                    text = "다시 시도",
                                    color = KusTheme.colors.c_43AB38,
                                    modifier = Modifier.noRippleClickable {
                                        viewModel.loadDetail(postId)
                                    }
                                )
                            }
                        }

                        CommunityDetailPhase.Success -> {
                            uiState.post?.let { post ->
                                CommunityDetailSuccessContent(
                                    post = post,
                                    onPostLikeReactClick = { viewModel.togglePostLike() },
                                    onScrapClick = { viewModel.toggleScrap() },
                                    onReplyClick = { parentId ->
                                        pendingReplyParentId = parentId
                                        showReplyConfirmDialog = true
                                    },
                                    onDeleteComment = { commentId ->
                                        viewModel.deleteComment(postId, commentId)
                                    },
                                    onCommentReact = { commentId, action ->
                                        viewModel.reactComment(commentId, action)
                                    }
                                )
                            }
                        }
                    }
                }

                ReplyConfirmDialog(
                    visible = showReplyConfirmDialog,
                    content = "대댓글을 작성하시겠습니까?",
                    onConfirm = {
                        showReplyConfirmDialog = false
                        showReplyComposer = true
                    },
                    onDismiss = {
                        showReplyConfirmDialog = false
                        pendingReplyParentId = null
                    }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding()
                ) {
                    KusCommentInput(
                        value = commentText,
                        onValueChange = { commentText = it },
                        placeholder = "댓글을 입력하세요",
                        enabled = uiState.phase == CommunityDetailPhase.Success,
                        onSend = {
                            viewModel.createCommentOrReply(commentText, null)
                            commentText =""
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }

        KusReplyCommentOverlay(
            visible = showReplyComposer,
            value = replyText,
            onValueChange = { replyText = it },
            enabled = uiState.phase == CommunityDetailPhase.Success,
            onSend = { text ->
                val parentId = pendingReplyParentId ?: return@KusReplyCommentOverlay
                viewModel.createCommentOrReply(text, parentId)
                replyText = ""
                pendingReplyParentId = null
                showReplyComposer = false
            },
            onDismiss = {
                showReplyComposer = false
                replyText = ""
                pendingReplyParentId = null
            }
        )
    }
}
