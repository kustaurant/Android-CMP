package com.kus.feature.tier.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
//    private val getTierRestaurantListUseCase: GetTierRestaurantListUseCase,
//    private val getTierRestaurantMapUseCase: GetTierRestaurantMapUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TierUiState())
    val uiState: StateFlow<TierUiState> = _uiState.asStateFlow()

    init {
        loadRestaurant()
    }

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
                categoryChangeList = true,
                categoryChangeMap = true,
            )
        }
        ensureDataForCurrentTab(force = true)
    }

    fun loadRestaurant() {
        _uiState.update { it.copy(selectedCategories = it.filterState.selectedCategoriesForDisplay()) }
        when (_uiState.value.selectedTab) {
            TierTab.LIST -> fetchFirstRestaurants()
            TierTab.MAP -> fetchMap()
        }
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
        mockLoadRestaurantList(1)
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
            // val data = getTierRestaurantMapUseCase(filter.cuisines, filter.situations, filter.locations)
            //val data = mockTierMapData()
            //_uiState.update { cur ->
             //  cur.copy(
            //        mapUiState = cur.mapUiState.copy(map = UiState.Success(data))
             //   )
            //}
        }
    }



    fun onTabSelected(tab: TierTab) {
        _uiState.update { it.copy(selectedTab = tab) }
        ensureDataForCurrentTab()
    }

    private fun ensureDataForCurrentTab(force: Boolean = false) {
        val s = _uiState.value
        val filter = s.filterState.normalized()

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

    fun onBottomSheetHidden() {
        _uiState.update { cur ->
            cur.copy(
                mapUiState = cur.mapUiState.copy(
                    selectedRestaurantId = null
                )
            )
        }
    }


    fun fetchNextRestaurants() {
        val s = _uiState.value.pageState
        if (s.phase != TierPhase.Idle || s.isLastPage) return

        _uiState.update { it.copy(pageState = s.copy(phase = TierPhase.Paging)) }
        loadRestaurantList(requestedPage = s.page + 1)
    }


    private fun mockLoadRestaurantList(requestedPage: Int) {
        val snapshot = _uiState.value
        val page = snapshot.pageState
        if (page.phase != TierPhase.Refreshing && page.phase != TierPhase.Paging) return

        val filter = snapshot.filterState.normalized()
        val isPartnership = filter.isPartnership

        viewModelScope.launch {
            val all = mutableListOf<TierRestaurant>()
            for (i in 1..100) {
                val tier = (i % 4) + 1 // 1~4
                all += TierRestaurant(
                    restaurantId = i.toLong(),
                    restaurantRanking = i,
                    restaurantName = "꾸아 건대점 #$i",
                    restaurantCuisine = listOf("한식", "일식", "중식", "양식", "카페/디저트")[i % 5],
                    restaurantPosition = listOf("건입~중문", "중문~어대", "후문", "정문", "구의역")[i % 5],
                    restaurantImgUrl = "",
                    mainTier = if (isPartnership) -1 else tier,
                    isEvaluated = (i % 3 == 0),
                    isFavorite = (i % 4 == 0),
                    longitude = 127.0 + (i * 0.0001),
                    latitude = 37.0 + (i * 0.0001),
                    partnershipInfo = if (i % 5 == 0) "어디다 학생증 제시하면 10% 할인" else "해당사항 없음",
                    restaurantScore = ((i % 50) / 10.0),
                    isTempTier = false,
                )
            }

            // ✅ 2) 페이지네이션 흉내 (페이지당 30개)
            val pageSize = 30
            val from = (requestedPage - 1) * pageSize
            val to = minOf(from + pageSize, all.size)

            val fetched = if (from in 0 until all.size) {
                all.subList(from, to)
            } else {
                emptyList()
            }

            val last = fetched.isEmpty() || to >= all.size

            // ✅ 3) 기존 로직과 동일하게 merge
            _uiState.update { cur ->
                val currentList = (cur.listState as? UiState.Success)?.data ?: emptyList()
                val merged = if (requestedPage == 1) fetched else currentList + fetched

                cur.copy(
                    listState = UiState.Success(merged),
                    pageState = cur.pageState.copy(
                        phase = TierPhase.Idle,
                        page = requestedPage,
                        isLastPage = last
                    )
                )
            }
        }
    }

    private fun loadRestaurantList(requestedPage: Int) {
        val snapshot = _uiState.value
        val page = snapshot.pageState
        if (page.phase != TierPhase.Refreshing && page.phase != TierPhase.Paging) return

        val filter = snapshot.filterState.normalized()
        val isPartnership = filter.isPartnership

        viewModelScope.launch {
//            runCatching {
//                // 변경: CategoryIdMapper 제거
//                // UseCase에 도메인 타입 그대로 전달
//                getTierRestaurantListUseCase(
//                    cuisines = filter.cuisines,
//                    situations = filter.situations,
//                    locations = filter.locations,
//                    page = requestedPage
//                )
//            }.onSuccess { tierListData ->
//                val fetched = tierListData.map {
//                    TierRestaurant(
//                        restaurantId = it.restaurantId,
//                        restaurantRanking = it.restaurantRanking ?: 0,
//                        restaurantName = it.restaurantName,
//                        restaurantCuisine = it.restaurantCuisine,
//                        restaurantPosition = it.restaurantPosition,
//                        restaurantImgUrl = it.restaurantImgUrl,
//                        mainTier = if (isPartnership) -1 else it.mainTier,
//                        partnershipInfo = it.partnershipInfo,
//                        isFavorite = it.isFavorite,
//                        x = it.x,
//                        y = it.y,
//                        isEvaluated = it.isEvaluated,
//                        restaurantScore = it.restaurantScore.takeIf { s -> !s.isNaN() } ?: 0.0
//                    )
//                }
//
//                val last = fetched.isEmpty()
//
//                _uiState.update { cur ->
//                    val currentList = (cur.listState as? UiState.Success)?.data ?: emptyList()
//                    val merged = if (requestedPage == 1) fetched else currentList + fetched
//
//                    cur.copy(
//                        listState = UiState.Success(merged),
//                        pageState = cur.pageState.copy(
//                            phase = TierPhase.Idle,
//                            page = requestedPage,
//                            isLastPage = last
//                        )
//                    )
//                }
//            }.onFailure { e ->
//                _uiState.update {
//                    it.copy(
//                        listState = UiState.Failure(e.message ?: "loadRestaurantList error"),
//                        pageState = it.pageState.copy(phase = TierPhase.Idle)
//                    )
//                }
//            }
        }
    }
}
