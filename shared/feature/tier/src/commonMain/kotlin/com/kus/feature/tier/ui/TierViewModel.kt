package com.kus.feature.tier.ui

import UiError
import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.feature.tier.ui.map.MapCameraState
import com.kus.shared.domain.model.tier.TierRestaurant
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location
import com.kus.shared.domain.model.tier.filter.Situation
import com.kus.shared.domain.tier.usecase.GetTierRestaurantListUseCase
import com.kus.shared.domain.tier.usecase.GetTierRestaurantMapUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class TierViewModel(
   private val getTierRestaurantListUseCase: GetTierRestaurantListUseCase,
    private val getTierRestaurantMapUseCase: GetTierRestaurantMapUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TierUiState())
    val uiState: StateFlow<TierUiState> = _uiState.asStateFlow()

    fun setShowBottomSheet(show: Boolean) {
        _uiState.update { it.copy(mapUiState = it.mapUiState.copy(isShowBottomSheet = show)) }
    }

    fun setTierListLastPosition(position: Int) {
        _uiState.update { it.copy(tierListLastPosition = position) }
    }

    fun setCategory(
        cuisines: Set<Cuisine>,
        situations: Set<Situation>,
        locations: Set<Location>,
    ) {
        val newFilter = TierFilterState(
            cuisines = cuisines.ifEmpty { setOf(Cuisine.ALL) },
            situations = situations.ifEmpty { setOf(Situation.ALL) },
            locations = locations.ifEmpty { setOf(Location.ALL) },
        ).normalized()

        _uiState.update {
            it.copy(
                filterState = newFilter,
                selectedCategories = newFilter.selectedCategoriesForDisplay(),
                pageState = it.pageState.copy(page = 1),
                tierListLastPosition = 0,
                categoryChangeList = true,
                categoryChangeMap = true,
            )
        }

        ensureDataForCurrentTab(force = true)
    }

    fun fetchFirstRestaurants() {
        _uiState.update {
            it.copy(
                categoryChangeList = false,
                pageState = it.pageState.copy(
                    phase = TierPhase.Refreshing,
                    page = 1,
                    isLastPage = false
                ),
                listState = UiState.Loading,
            )
        }
        loadRestaurantList(1)
    }


    private fun fetchMap() {
        val filter = _uiState.value.filterState.normalized()

        _uiState.update { cur ->
            cur.copy(
                categoryChangeMap = false,
                mapUiState = cur.mapUiState.copy(map = UiState.Loading)
            )
        }

        viewModelScope.launch {
            val data = getTierRestaurantMapUseCase(filter.cuisines, filter.situations, filter.locations)
            _uiState.update { cur ->
                cur.copy(
                    mapUiState = cur.mapUiState.copy(map = UiState.Success(data))
                )
            }
        }
    }

    fun onCameraIdle(camera: MapCameraState) {
        _uiState.update { cur ->
            cur.copy(mapUiState = cur.mapUiState.copy(cameraState = camera))
        }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun onTabSelected(tab: TierTab) {
        _uiState.update { it.copy(selectedTab = tab) }
        ensureDataForCurrentTab()
    }

    private fun ensureDataForCurrentTab(force: Boolean = false) {
        val s = _uiState.value

        when (s.selectedTab) {
            TierTab.LIST -> {
                val need = force ||
                        s.categoryChangeList ||
                        s.listState !is UiState.Success

                if (need) {
                    fetchFirstRestaurants()
                }
            }

            TierTab.MAP -> {
                val need = force ||
                        s.categoryChangeMap ||
                        s.mapUiState.map !is UiState.Success

                if (need) {
                    fetchMap()
                }
            }
        }
    }

    fun onRestaurantMarkerClicked(id: Long) {
        _uiState.update { cur ->
            cur.copy(
                mapUiState = cur.mapUiState.copy(
                    isShowBottomSheet = true,
                    selectedRestaurantId = id
                )
            )
        }
    }

    fun onMapTapped() {
        _uiState.update { cur ->
            cur.copy(
                mapUiState = cur.mapUiState.copy(
                    isShowBottomSheet = false
                )
            )
        }
    }

    fun toggleAiTier() {
        _uiState.update {
            it.copy(
                isAITier = !it.isAITier,
                scrollToTopTrigger = it.scrollToTopTrigger + 1,
                tierListLastPosition = 0,
                categoryChangeList = false,
                pageState = it.pageState.copy(
                    phase = TierPhase.Refreshing,
                    page = 1,
                    isLastPage = false
                ),
                listState = UiState.Loading
            )
        }

        loadRestaurantList(requestedPage = 1)
    }

    fun fetchNextRestaurants() {
        val s = _uiState.value.pageState
        if (s.phase != TierPhase.Idle || s.isLastPage) return

        _uiState.update { it.copy(pageState = s.copy(phase = TierPhase.Paging)) }
        loadRestaurantList(requestedPage = s.page + 1)
    }

    private fun loadRestaurantList(requestedPage: Int) {
        val snapshot = _uiState.value
        val page = snapshot.pageState
        if (page.phase != TierPhase.Refreshing && page.phase != TierPhase.Paging) return

        val filter = snapshot.filterState.normalized()
        val isPartnership = filter.isPartnership
        val isAiTier = snapshot.isAITier

        viewModelScope.launch {
            runCatching {
                getTierRestaurantListUseCase(
                    cuisines = filter.cuisines,
                    situations = filter.situations,
                    locations = filter.locations,
                    page = requestedPage,
                    isAiTier = isAiTier,
                )
            }.onSuccess { tierListData ->
                val fetched = tierListData.map {
                    TierRestaurant(
                        restaurantId = it.restaurantId,
                        restaurantRanking = it.restaurantRanking,
                        restaurantName = it.restaurantName,
                        restaurantCuisine = it.restaurantCuisine,
                        restaurantPosition = it.restaurantPosition,
                        restaurantImgUrl = it.restaurantImgUrl,
                        mainTier = if (isPartnership) -1 else it.mainTier,
                        partnershipInfo = it.partnershipInfo,
                        isFavorite = it.isFavorite,
                        longitude = it.longitude,
                        latitude = it.latitude,
                        isEvaluated = it.isEvaluated,
                        restaurantScore = it.restaurantScore.takeIf { s -> !s.isNaN() } ?: 0.0,
                        isTempTier = it.isTempTier
                    )
                }

                val last = fetched.isEmpty()

                _uiState.update { cur ->
                    val currentList = (cur.listState as? UiState.Success)?.data ?: emptyList()
                    val merged = if (requestedPage == 1) fetched else currentList + fetched
                    val deduped = merged.distinctBy { it.restaurantId }

                    cur.copy(
                        listState = UiState.Success(deduped),
                        pageState = cur.pageState.copy(
                            phase = TierPhase.Idle,
                            page = requestedPage,
                            isLastPage = last
                        )
                    )
                }
            }.onFailure { e ->
                _uiState.update {
                    it.copy(
                        listState = UiState.Failure(
                            UiError.Message(e.message ?: "음식점 리스트를 불러오는데 오류가 발생했습니다.")
                        ),
                        pageState = it.pageState.copy(phase = TierPhase.Idle),
                        toastMessage = "음식점 리스트를 불러오는데 오류가 발생했습니다."
                    )
                }
            }
        }
    }
}
