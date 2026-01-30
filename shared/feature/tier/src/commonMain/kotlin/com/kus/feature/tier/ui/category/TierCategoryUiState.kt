package com.kus.feature.tier.ui.category

import com.kus.feature.tier.ui.TierFilterState
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location
import com.kus.shared.domain.model.tier.filter.Situation

data class TierCategoryUiState(
    val cuisines: Set<Cuisine> = setOf(Cuisine.ALL),
    val situations: Set<Situation> = setOf(Situation.ALL),
    val locations: Set<Location> = setOf(Location.ALL),
    val initial: TierFilterState = TierFilterState(),
)
