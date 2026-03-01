package com.kus.feature.community.ui.detail

import com.kus.domain.community.model.CommunityPost

enum class CommunityDetailPhase { Idle, Loading, Success, Failure }

data class CommunityDetailUiState(
    val phase: CommunityDetailPhase = CommunityDetailPhase.Idle,
    val post: CommunityPost? = null
)