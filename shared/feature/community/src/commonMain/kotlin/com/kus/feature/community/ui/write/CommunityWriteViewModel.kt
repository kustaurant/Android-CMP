package com.kus.feature.community.ui.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.domain.community.model.PostCategory
import com.kus.domain.community.model.UploadImageException
import com.kus.domain.community.usecase.PatchPostModifyUseCase
import com.kus.domain.community.usecase.PostCommunityPostCreateUseCase
import com.kus.domain.community.usecase.PostCommunityUploadImageUseCase
import com.kus.feature.community.model.CommunityPostModifyPayload
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommunityWriteViewModel(
    private val postCreateUseCase: PostCommunityPostCreateUseCase,
    private val postUploadImageUseCase: PostCommunityUploadImageUseCase,
    private val patchPostModifyUseCase: PatchPostModifyUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CommunityWriteUiState())
    val uiState: StateFlow<CommunityWriteUiState> = _uiState.asStateFlow()

    fun init(payload: CommunityPostModifyPayload?) {
        if (payload == null) {
            _uiState.update {
                it.copy(
                    isEditMode = false,
                    editPostId = null,
                    category = null,
                    title = "",
                    html = "",
                    nickname = "",
                    writerIconUrl = null,
                    timeAgo = "",
                    totalLikes = 0L,
                    commentCount = 0L,
                    isSendReady = false,
                    finishState = PostFinishState.IDLE,
                    toastMessage = null,
                    initialHtmlToSet = "",
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    isEditMode = true,
                    editPostId = payload.postId,
                    category = payload.category,
                    title = payload.title,
                    html = payload.body,
                    totalLikes = payload.totalLikes,
                    commentCount = payload.commentCount,
                    isSendReady = computeSendReady(
                        category = payload.category,
                        title = payload.title,
                        html = payload.body
                    ),
                    finishState = PostFinishState.IDLE,
                    toastMessage = null,
                    initialHtmlToSet = payload.body,
                )
            }
        }
    }

    fun consumeInitialHtml() {
        _uiState.update { it.copy(initialHtmlToSet = null) }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun onEditorReady() {
        _uiState.update { it.copy(phase = CommunityWritePhase.Idle) }
    }

    fun consumeFinishState() {
        _uiState.update { it.copy(finishState = PostFinishState.IDLE) }
    }

    fun onTitleChange(title: String) {
        _uiState.update { s ->
            val next = s.copy(title = title)
            next.copy(isSendReady = computeSendReady(next.category, next.title, next.html))
        }
    }

    fun onCategoryChange(category: PostCategory) {
        _uiState.update { s ->
            val next = s.copy(category = category)
            next.copy(isSendReady = computeSendReady(next.category, next.title, next.html))
        }
    }

    fun onHtmlChange(html: String) {
        _uiState.update { s ->
            val next = s.copy(html = html)
            next.copy(isSendReady = computeSendReady(next.category, next.title, next.html))
        }
    }

    fun submit() {
        val s = _uiState.value
        if (!s.isSendReady) return

        viewModelScope.launch {
            _uiState.update { it.copy(phase = CommunityWritePhase.Loading) }
            var editPostId = 0L
            runCatching {
                if (s.isEditMode) {
                    val postId = s.editPostId ?: error("editPostId null")

                    patchPostModifyUseCase(
                        postId = postId.toString(),
                        title = s.title,
                        postCategory = s.category ?: PostCategory.FREE,
                        content = s.html
                    )
                    editPostId = postId
                    PostFinishState.MODIFY_OK
                } else {
                    val res = postCreateUseCase(
                        title = s.title,
                        postCategory = s.category ?: PostCategory.FREE,
                        content = s.html
                    )
                    editPostId = res.postId
                    PostFinishState.CREATE_OK
                }
            }.onSuccess { finish ->
                _uiState.update {
                    it.copy(
                        phase = CommunityWritePhase.Idle,
                        finishState = finish,
                        editPostId = editPostId
                    )
                }
            }.onFailure { e ->
                _uiState.update {
                    it.copy(
                        phase = CommunityWritePhase.Idle,
                        finishState = PostFinishState.ERR,
                        toastMessage = e.message ?: "게시글 처리 중 오류가 발생했습니다."
                    )
                }
            }
        }
    }

    suspend fun uploadImageAndGetUrl(imagePath: String): String? {
        _uiState.update { it.copy(isImageUploading = true) }

        return runCatching {
            postUploadImageUseCase(imagePath)
        }.onSuccess {
            _uiState.update { it.copy(isImageUploading = false) }
        }.onFailure { e ->
            _uiState.update { it.copy(isImageUploading = false) }
            val msg = when (e) {
                is UploadImageException.TooLarge -> "이미지는 1MB 이하만 업로드 가능합니다."
                is UploadImageException.ReadFailed -> "이미지를 읽을 수 없습니다."
                else -> e.message ?: "업로드 중 오류가 발생했습니다."
            }
            _uiState.update { it.copy(toastMessage = msg) }
        }.getOrNull()
    }

    private fun computeSendReady(category: PostCategory?, title: String, html: String): Boolean {
        if (category == null || category == PostCategory.ALL) return false
        if (title.isBlank()) return false

        val cleaned = html
            .replace(Regex("<p><br></p>|<br>|<p></p>"), "")
            .trim()
        return cleaned.isNotEmpty()
    }
}