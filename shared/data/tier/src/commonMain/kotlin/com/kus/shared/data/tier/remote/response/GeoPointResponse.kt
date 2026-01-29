package com.kus.shared.data.tier.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class GeoPointResponse(
    val x: Double,
    val y: Double,
)
