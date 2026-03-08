package com.kus.kustaurant.detail.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteResponse(
    val isFavorite: Boolean,
    val count: Long,
)
