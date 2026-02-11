package com.kus.shared.data.tier.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoPointResponse(
    @SerialName("y") val longitude: Double,
    @SerialName("x") val latitude: Double,
)

