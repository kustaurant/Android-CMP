package com.kus.shared.data.tier.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class TierMapRequest(
    val cuisines: String,
    val situations: String,
    val locations: String,
)