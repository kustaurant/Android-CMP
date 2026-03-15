package com.kus.feature.tier.ui.category

import androidx.lifecycle.ViewModel
import com.kus.feature.tier.ui.TierFilterState
import com.kus.shared.domain.model.rule.FilterRule
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location
import com.kus.shared.domain.model.tier.filter.Situation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TierCategorySelectViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TierCategoryUiState())
    val uiState: StateFlow<TierCategoryUiState> = _uiState.asStateFlow()
    private var isInit = false

    fun initIfNeeded(initial: TierFilterState) {
        if (isInit) return
        isInit = true

        val normalized = initial.normalized()

        val cuisines = normalized.cuisines.ifEmpty { setOf(Cuisine.ALL) }
        val situations = normalized.situations.ifEmpty { setOf(Situation.ALL) }
        val locations = normalized.locations.ifEmpty { setOf(Location.ALL) }

        val current = TierFilterState(
            cuisines = cuisines,
            situations = situations,
            locations = locations,
        ).normalized()

        _uiState.value = TierCategoryUiState(
            initial = normalized,
            selectedCuisines = cuisines,
            selectedSituations = situations,
            selectedLocations = locations,
            current = current,
            applyEnabled = computeApplyEnabled(current, normalized, cuisines, situations, locations),
        )
    }

    fun toggleCuisine(item: Cuisine) {
        val all = Cuisine.ALL
        val exclusive = setOf(Cuisine.PARTNERSHIP)

        _uiState.update { s ->
            val next = FilterRule.toggleWithAllAndExclusive(
                current = s.selectedCuisines,
                clicked = item,
                all = all,
                exclusive = exclusive,
            )
            reduce(s.copy(selectedCuisines = next))
        }
    }

    fun toggleSituation(item: Situation) {
        _uiState.update { s ->
            val next = FilterRule.toggleWithAll(
                current = s.selectedSituations,
                clicked = item,
                all = Situation.ALL
            )
            reduce(s.copy(selectedSituations = next))
        }
    }

    fun toggleLocation(item: Location) {
        _uiState.update { s ->
            val next = FilterRule.toggleWithAll(
                current = s.selectedLocations,
                clicked = item,
                all = Location.ALL
            )
            reduce(s.copy(selectedLocations = next))
        }
    }

    fun buildResult(): TierFilterState = _uiState.value.current

    private fun reduce(s: TierCategoryUiState): TierCategoryUiState {
        val current = TierFilterState(
            cuisines = s.selectedCuisines,
            situations = s.selectedSituations,
            locations = s.selectedLocations,
        ).normalized()

        return s.copy(
            current = current,
            applyEnabled = computeApplyEnabled(
                current = current,
                initial = s.initial,
                cuisines = s.selectedCuisines,
                situations = s.selectedSituations,
                locations = s.selectedLocations,
            )
        )
    }

    private fun computeApplyEnabled(
        current: TierFilterState,
        initial: TierFilterState,
        cuisines: Set<Cuisine>,
        situations: Set<Situation>,
        locations: Set<Location>,
    ): Boolean {
        val hasChanges = current != initial
        val isAllGroupsSelected = cuisines.isNotEmpty() && situations.isNotEmpty() && locations.isNotEmpty()
        return hasChanges && isAllGroupsSelected
    }
}
