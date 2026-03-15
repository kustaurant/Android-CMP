package com.kus.shared.domain.model.detail

data class ReactionResult(
    val id: Int,
    val reaction: String,
    val likeCount: Int,
    val dislikeCount: Int,
)
