package com.kus.feature.draw.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.domain.draw.usecase.GetDrawRestaurantUseCase
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DrawResultViewModel(
    private val getDrawRestaurantUseCase: GetDrawRestaurantUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DrawResultUiState())

    val uiState: StateFlow<DrawResultUiState> = _uiState

    fun initialize(
        locations: Set<Location>,
        cuisines: Set<Cuisine>,
    ) {

        if (_uiState.value.initialized) return

        _uiState.update {
            it.copy(
                selectedLocations = locations,
                selectedCuisines = cuisines,
                initialized = true
            )
        }

        loadDrawRestaurants()
    }

    private fun loadDrawRestaurants() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    toastMessage = null
                )
            }

            runCatching {

                getDrawRestaurantUseCase(
                    locations = _uiState.value.selectedLocations,
                    cuisines = _uiState.value.selectedCuisines,
                )

            }.onSuccess { restaurants ->
                val randomIndex = if (restaurants.isNotEmpty()) {
                    restaurants.indices.random()
                } else {
                    0
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        restaurants = restaurants,
                        randomIndex = randomIndex,
                    )
                }

            }.onFailure { throwable ->

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        toastMessage = throwable.message
                            ?: "맛집 데이터를 불러오지 못했습니다."
                    )
                }

            }
        }
    }

    fun redraw() {
        if(uiState.value.sameDrawCnt >= 2) {
            loadDrawRestaurants()
            _uiState.update { it.copy(sameDrawCnt = 0) }
        } else {
            val data = uiState.value.restaurants
            val prevIndex = uiState.value.randomIndex
            val newIndex = data.indices
                .filter { it != prevIndex }
                .random()

            _uiState.update {
                it.copy(
                    randomIndex = newIndex,
                    sameDrawCnt = uiState.value.sameDrawCnt + 1
                )
            }
        }
    }
}