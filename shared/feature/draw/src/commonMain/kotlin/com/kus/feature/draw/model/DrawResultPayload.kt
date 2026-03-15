package com.kus.feature.draw.model

import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location
import kotlinx.serialization.Serializable


@Serializable
data class DrawResultPayload(
    val locations: List<Location>,
    val cuisines: List<Cuisine>,
)