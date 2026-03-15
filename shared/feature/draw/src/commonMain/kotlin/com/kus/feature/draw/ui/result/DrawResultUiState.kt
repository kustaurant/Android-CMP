package com.kus.feature.draw.ui.result

import com.kus.domain.draw.model.DrawRestaurant
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location

data class DrawResultUiState(
    val selectedLocations: Set<Location> = emptySet(),
    val selectedCuisines: Set<Cuisine> = emptySet(),
    val restaurants: List<DrawRestaurant> = emptyList(),
    val isLoading: Boolean = false,
    val toastMessage: String? = null,
    val initialized: Boolean = false,
    val randomIndex : Int? = null,
    val sameDrawCnt : Int = 0,
)