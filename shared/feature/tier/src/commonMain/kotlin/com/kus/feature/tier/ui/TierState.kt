package com.kus.feature.tier.ui

import UiState
import com.kus.feature.tier.ui.map.TierMapUiState
import com.kus.shared.domain.model.tier.TierRestaurant
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location
import com.kus.shared.domain.model.tier.filter.Situation
import kotlinx.serialization.Serializable

enum class TierPhase { Idle, Refreshing, Paging }

data class TierPageState(
    val phase: TierPhase = TierPhase.Idle,
    val page: Int = 1,
    val isLastPage: Boolean = false,
)

@Serializable
data class TierFilterState(
    val cuisines: Set<Cuisine> = setOf(Cuisine.ALL),
    val situations: Set<Situation> = setOf(Situation.ALL),
    val locations: Set<Location> = setOf(Location.ALL),
    val isAiTierViewEnabled: Boolean = false,
) {
    fun normalized(): TierFilterState = copy(
        cuisines = Cuisine.normalize(cuisines),
        situations = Situation.normalize(situations),
        locations = Location.normalize(locations),
    )

    val isPartnership: Boolean
        get() = cuisines.contains(Cuisine.PARTNERSHIP)

    fun selectedCategoriesForDisplay(): Set<String> {
        val labels = mutableListOf<String>()

        if (!cuisines.contains(Cuisine.ALL)) {
            labels += cuisines.map { it.toLabel() }
        }


        if (!situations.contains(Situation.ALL)) {
            labels += situations.map { it.toLabel() }
        }


        if (!locations.contains(Location.ALL)) {
            labels += locations.map { it.toLabel() }
        }

        return if (labels.isEmpty()) setOf("전체") else labels.toSet()
    }

}

data class TierUiState(
    val selectedTab: TierTab = TierTab.LIST,

    val isExpanded: Boolean = false,

    val filterState: TierFilterState = TierFilterState(),
    val selectedCategories: Set<String> = setOf("전체"),

    val listState: UiState<List<TierRestaurant>> = UiState.Loading,

    val pageState: TierPageState = TierPageState(),
    val tierListLastPosition: Int = 0,

    val categoryChangeList: Boolean = true,
    val categoryChangeMap: Boolean = true,

    val mapUiState: TierMapUiState = TierMapUiState(),
    val toastMessage: String? = null,
)
