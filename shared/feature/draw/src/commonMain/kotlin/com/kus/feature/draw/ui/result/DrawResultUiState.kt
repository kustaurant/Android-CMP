package com.kus.feature.draw.ui.result

import UiState
import com.kus.domain.draw.model.DrawRestaurant
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location

data class DrawResultUiState(
    val selectedLocations: Set<Location> = emptySet(),
    val selectedCuisines: Set<Cuisine> = emptySet(),
    val restaurantState: UiState<List<DrawRestaurant>> = UiState.Idle,
    val randomIndex: Int? = null,
    val sameDrawCnt: Int = 0,
    val initialized: Boolean = false,
    val hasPlayedDrawAnimation: Boolean = false,
)