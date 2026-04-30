package com.kus.shared.data.tier.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class GeoPointResponse(
    val longitude: Double,
    val latitude: Double,
)

