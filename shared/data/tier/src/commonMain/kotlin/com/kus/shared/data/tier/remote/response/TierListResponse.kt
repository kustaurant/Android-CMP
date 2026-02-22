package com.kus.shared.data.tier.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class TierListResponse(
    val restaurants: List<RestaurantResponse>
)