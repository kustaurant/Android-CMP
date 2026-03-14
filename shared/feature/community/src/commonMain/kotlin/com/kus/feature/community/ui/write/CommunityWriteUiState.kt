package com.kus.feature.community.ui.write

import com.kus.domain.community.model.PostCategory

enum class CommunityWritePhase { Idle, Loading }
enum class PostFinishState { IDLE, ERR, MODIFY_OK, CREATE_OK }

data class CommunityWriteUiState(
    val phase: CommunityWritePhase = CommunityWritePhase.Idle,

    val isEditMode: Boolean = false,
    val editPostId: Long? = null,

    val category: PostCategory? = null,
    val title: String = "",
    val html: String = "",

    val nickname: String = "",
    val writerIconUrl: String? = null,
    val timeAgo: String = "",
    val totalLikes: Long = 0L,
    val commentCount: Long = 0L,

    val isImageUploading: Boolean = false,
    val isSendReady: Boolean = false,
    val finishState: PostFinishState = PostFinishState.IDLE,
    val toastMessage: String? = null,

    val initialHtmlToSet: String? = null,
)