package com.kus.feature.community.ui.detail

import GetSessionAvailabilityUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.designsystem.util.stripHtml
import com.kus.domain.community.model.LikeEvent
import com.kus.domain.community.usecase.DeleteCommunityCommentUseCase
import com.kus.domain.community.usecase.DeleteCommunityPostUseCase
import com.kus.domain.community.usecase.GetCommunityPostDetailUseCase
import com.kus.domain.community.usecase.PostCommunityPostCommentReactUseCase
import com.kus.domain.community.usecase.PostCommunityPostCommentReplyUseCase
import com.kus.domain.community.usecase.PostCommunityPostDetailScrapUseCase
import com.kus.domain.community.usecase.PostPostLikeUseCase
import com.kus.feature.community.model.CommunityPostModifyPayload
import com.kus.feature.community.model.DeleteCommunityEvent
import com.kus.feature.community.model.ReactionAction
import com.kus.feature.community.ui.mapper.toUiModel
import com.kus.feature.community.ui.model.CommunityPostCommentUi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommunityDetailViewModel(
    private val getCommunityPostDetailUseCase: GetCommunityPostDetailUseCase,
    private val postPostScrapUseCase: PostCommunityPostDetailScrapUseCase,
    private val postPostLikeUseCase: PostPostLikeUseCase,
    private val postCreateCommentReplyUseCase: PostCommunityPostCommentReplyUseCase,
    private val postCommentReactUseCase: PostCommunityPostCommentReactUseCase,
    private val deletePostUseCase: DeleteCommunityPostUseCase,
    private val deleteCommentUseCase: DeleteCommunityCommentUseCase,
    private val getSessionAvailabilityUseCase: GetSessionAvailabilityUseCase,
) : ViewModel() {
    private val _events = MutableSharedFlow<DeleteCommunityEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<DeleteCommunityEvent> = _events.asSharedFlow()

    private val _uiState = MutableStateFlow(CommunityDetailUiState())
    val uiState: StateFlow<CommunityDetailUiState> = _uiState.asStateFlow()

    private var currentPostId: Long? = null

    fun loadDetail(postId: Long) {
        if (currentPostId == postId && _uiState.value.phase == CommunityDetailPhase.Success) return
        currentPostId = postId

        viewModelScope.launch {
            fetchDetail(postId)
        }
    }

    private fun fetchDetail(postId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(phase = CommunityDetailPhase.Loading) }

            val isLoggedIn = getSessionAvailabilityUseCase()
            runCatching {
                getCommunityPostDetailUseCase(postId, isLoggedIn)
            }.onSuccess { post ->
                _uiState.update {
                    it.copy(phase = CommunityDetailPhase.Success, post = post.toUiModel())
                }
            }.onFailure {
                _uiState.update { it.copy(phase = CommunityDetailPhase.Failure) }
            }
        }
    }

    fun applyEditResult(payload: CommunityPostModifyPayload) {
        _uiState.update { state ->
            val currentPost = state.post ?: return@update state

            if (currentPost.postId != payload.postId) {
                return@update state
            }

            state.copy(
                post = currentPost.copy(
                    category = payload.category,
                    title = payload.title,
                    body = payload.body
                )
            )
        }
    }

    fun toggleScrap() {
        viewModelScope.launch {
            val current = _uiState.value.post ?: return@launch
            val postId = current.postId

            val nextScrap = !current.isScrapped

            runCatching { postPostScrapUseCase(postId, nextScrap) }
                .onSuccess { info ->
                    _uiState.update { state ->
                        val post = state.post ?: return@update state
                        state.copy(
                            post = post.copy(
                                isScrapped = info.isScrapped,
                                scrapCount = info.postScrapCount.toLong()
                            )
                        )
                    }
                }
                .onFailure {
                }
        }
    }

    fun togglePostLike() {
        viewModelScope.launch {
            val current = _uiState.value.post ?: return@launch
            val postId = current.postId

            val currentReaction = current.myReaction
            val next: LikeEvent? = when (currentReaction) {
                "LIKE" -> null
                "DISLIKE" -> LikeEvent.LIKE
                else -> LikeEvent.LIKE
            }

            runCatching { postPostLikeUseCase(postId, next) }
                .onSuccess { info ->
                    _uiState.update { state ->
                        val post = state.post ?: return@update state
                        state.copy(
                            post = post.copy(
                                myReaction = info.reactionType,
                                likeOnlyCount = info.likeCount.toLong(),
                            )
                        )
                    }
                }
                .onFailure {
                }
        }
    }

    fun createCommentOrReply(
        content: String,
        parentCommentId: Long?,
    ) {
        viewModelScope.launch {
            val currentPost = _uiState.value.post ?: return@launch
            val postId = currentPost.postId

            runCatching { postCreateCommentReplyUseCase(content, postId, parentCommentId) }
                .onSuccess { created ->
                    val createdUi = created.toUiModel()  // 변환
                    _uiState.update { state ->
                        val post = state.post ?: return@update state
                        val currentComments = post.comments.orEmpty()

                        val newComments =
                            if (createdUi.parentCommentId == null) {
                                listOf(createdUi) + currentComments
                            } else {
                                val parentId = createdUi.parentCommentId
                                val (updated, inserted) = insertReply(
                                    list = currentComments,
                                    parentId = parentId,
                                    child = createdUi
                                )
                                if (inserted) updated else listOf(createdUi) + currentComments
                            }

                        state.copy(
                            post = post.copy(
                                comments = newComments,
                                commentCount = post.commentCount + 1
                            )
                        )
                    }
                }
                .onFailure { }
        }
    }

    private fun insertReply(
        list: List<CommunityPostCommentUi>,
        parentId: Long,
        child: CommunityPostCommentUi,
    ): Pair<List<CommunityPostCommentUi>, Boolean> {
        var inserted = false
        val updated = list.map { c ->
            when {
                c.commentId == parentId -> {
                    inserted = true
                    c.copy(repliesList = listOf(child) + c.repliesList)
                }

                else -> {
                    val (newReplies, did) = insertReply(c.repliesList, parentId, child)
                    if (did) {
                        inserted = true
                        c.copy(repliesList = newReplies)
                    } else c
                }
            }
        }
        return updated to inserted
    }

    fun reactComment(commentId: Long, action: ReactionAction) {
        viewModelScope.launch {
            val post = _uiState.value.post ?: return@launch
            val comments = post.comments.orEmpty()
            val currentReaction = findCommentById(comments, commentId)?.reactionType

            val likeEvent: LikeEvent? = when (action) {
                ReactionAction.LIKE ->
                    if (currentReaction == "LIKE") null else LikeEvent.LIKE

                ReactionAction.DISLIKE ->
                    if (currentReaction == "DISLIKE") null else LikeEvent.DISLIKE
            }

            runCatching { postCommentReactUseCase(commentId, likeEvent) }
                .onSuccess { resp ->
                    _uiState.update { state ->
                        val p = state.post ?: return@update state
                        val cur = p.comments.orEmpty()
                        val updated = cur.map {
                            updateCommentTree(
                                comment = it,
                                targetId = commentId,
                                likeCount = resp.likeCount,
                                dislikeCount = resp.dislikeCount,
                                reactionType = resp.reactionType
                            )
                        }
                        state.copy(post = p.copy(comments = updated))
                    }
                }
        }
    }

    private fun findCommentById(
        comments: List<CommunityPostCommentUi>,
        targetId: Long,
    ): CommunityPostCommentUi? {
        comments.forEach { c ->
            if (c.commentId == targetId) return c
            findCommentById(c.repliesList, targetId)?.let { return it }
        }
        return null
    }

    private fun updateCommentTree(
        comment: CommunityPostCommentUi,
        targetId: Long,
        likeCount: Int,
        dislikeCount: Int,
        reactionType: String?,
    ): CommunityPostCommentUi {
        if (comment.commentId == targetId) {
            return comment.copy(
                likeCount = likeCount,
                dislikeCount = dislikeCount,
                reactionType = reactionType
            )
        }
        if (comment.repliesList.isEmpty()) return comment
        val updatedReplies = comment.repliesList.map {
            updateCommentTree(it, targetId, likeCount, dislikeCount, reactionType)
        }
        return comment.copy(repliesList = updatedReplies)
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun deletePost(postId: Long) {
        viewModelScope.launch {
            runCatching { deletePostUseCase(postId) }
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            toastMessage = "게시글이 삭제되었어요."
                        )
                    }
                    _events.tryEmit(DeleteCommunityEvent.Deleted)
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            toastMessage = "게시글 삭제에 실패했어요."
                        )
                    }
                    _events.tryEmit(DeleteCommunityEvent.Error(e.message ?: "삭제에 실패했어요."))
                }
        }
    }

    fun deleteComment(commentId: Long) {
        viewModelScope.launch {
            runCatching { deleteCommentUseCase(commentId) }
                .onSuccess {
                    _uiState.update { state ->
                        val post = state.post ?: return@update state
                        val comments = post.comments.orEmpty()
                        val updatedComments = removeOrMarkComment(comments, commentId)
                        state.copy(
                            post = post.copy(
                                comments = updatedComments,
                                commentCount = post.commentCount - 1
                            )
                        )
                    }
                }
                .onFailure { }
        }
    }

    private fun removeOrMarkComment(
        comments: List<CommunityPostCommentUi>,
        targetId: Long,
    ): List<CommunityPostCommentUi> {
        return comments.mapNotNull { comment ->
            when {
                comment.commentId == targetId && comment.repliesList.isEmpty() -> null

                comment.commentId == targetId && comment.repliesList.isNotEmpty() -> {
                    comment.copy(
                        body = "삭제된 댓글입니다.",
                        isCommentMine = false,
                        isDeleted = true,
                    )
                }

                else -> {
                    val updatedReplies = comment.repliesList.filter { it.commentId != targetId }
                    if (comment.isDeleted && updatedReplies.isEmpty()) {
                        null
                    } else {
                        comment.copy(repliesList = updatedReplies)
                    }
                }
            }
        }
    }

    fun currentPayload(): CommunityPostModifyPayload? {
        val post = uiState.value.post ?: return null
        return CommunityPostModifyPayload(
            postId = post.postId,
            title = post.title,
            body = post.body.stripHtml(),
            totalLikes = post.likeOnlyCount,
            commentCount = post.commentCount,
            category = post.category,
        )
    }
}