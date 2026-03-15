package com.kus.feature.draw.ui.select

import androidx.lifecycle.ViewModel
import com.kus.shared.domain.model.rule.FilterRule
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class DrawSelectViewModel(
) : ViewModel() {

    private val _uiState = MutableStateFlow(DrawSelectUiState())
    val uiState: StateFlow<DrawSelectUiState> = _uiState

    fun onLocationChipClick(location: Location) {
        _uiState.update { state ->
            state.copy(
                selectedLocations = Location.toggle(
                    current = state.selectedLocations,
                    clicked = location
                )
            )
        }
    }

    fun onCuisineClick(cuisine: Cuisine) {
        _uiState.update { state ->
            state.copy(
                selectedCuisines = FilterRule.toggleWithAllAndExclusive(
                    current = state.selectedCuisines,
                    clicked = cuisine,
                    all = Cuisine.ALL,
                    exclusive = setOf(Cuisine.PARTNERSHIP),
                )
            )
        }
    }
}