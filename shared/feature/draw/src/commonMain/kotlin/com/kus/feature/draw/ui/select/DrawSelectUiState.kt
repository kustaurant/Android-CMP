package com.kus.feature.draw.ui.select

import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location

data class DrawSelectUiState(
    val selectedLocations: Set<Location> = setOf(Location.ALL),
    val selectedCuisines: Set<Cuisine> = setOf(Cuisine.ALL),
)