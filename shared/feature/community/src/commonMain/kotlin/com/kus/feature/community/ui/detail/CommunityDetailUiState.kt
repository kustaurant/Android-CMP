package com.kus.feature.community.ui.detail

import com.kus.feature.community.ui.model.CommunityPostUi

enum class CommunityDetailPhase { Idle, Loading, Success, Failure }

data class CommunityDetailUiState(
    val phase: CommunityDetailPhase = CommunityDetailPhase.Idle,
    val post: CommunityPostUi? = null,
    val toastMessage :String? = null,
)