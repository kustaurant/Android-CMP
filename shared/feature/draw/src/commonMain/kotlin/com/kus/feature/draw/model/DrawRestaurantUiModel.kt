package com.kus.feature.draw.model

import androidx.compose.runtime.Stable

@Stable
data class DrawRestaurantUiModel(
    val id: Long,
    val categoryLabel: String,
    val name: String,
    val rating: Float,
    val partnershipInfo: String,
    val imageUrl: String,
)