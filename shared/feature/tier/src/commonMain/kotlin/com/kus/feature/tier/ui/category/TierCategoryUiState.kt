package com.kus.feature.tier.ui.category

import com.kus.feature.tier.ui.TierFilterState
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location
import com.kus.shared.domain.model.tier.filter.Situation

data class TierCategoryUiState(
    val initial: TierFilterState = TierFilterState(),
    val selectedCuisines: Set<Cuisine> = setOf(Cuisine.ALL),
    val selectedSituations: Set<Situation> = setOf(Situation.ALL),
    val selectedLocations: Set<Location> = setOf(Location.ALL),
    val current: TierFilterState = TierFilterState(),
    val applyEnabled: Boolean = false,
)