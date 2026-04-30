package com.kus.shared.data.tier.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class VisibleBoundsResponse(
    val minLat: Double = 0.0,
    val maxLat: Double = 0.0,
    val minLng: Double = 0.0,
    val maxLng: Double = 0.0,
)
