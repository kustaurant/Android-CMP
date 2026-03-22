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
                    restaurantState = UiState.Loading
                )
            }

            runCatching {
                getDrawRestaurantUseCase(
                    locations = _uiState.value.selectedLocations,
                    cuisines = _uiState.value.selectedCuisines,
                )
            }.onSuccess { restaurants ->
                if (restaurants.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            restaurantState = UiState.Failure(
                                UiError.Message("해당 카테고리에 맞는 식당이 없습니다.")
                            ),
                            randomIndex = null,
                            hasPlayedDrawAnimation = false,
                        )
                    }
                    return@onSuccess
                }

                val randomIndex = restaurants.indices.random()

                _uiState.update {
                    it.copy(
                        restaurantState = UiState.Success(restaurants),
                        randomIndex = randomIndex,
                        hasPlayedDrawAnimation = false,
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        restaurantState = UiState.Failure(
                            UiError.Message(
                                throwable.message ?: "해당 카테고리에 맞는 식당이 없습니다."
                            )
                        ),
                        randomIndex = null,
                        hasPlayedDrawAnimation = false,
                    )
                }
            }
        }
    }

    fun redraw() {
        val restaurantState = uiState.value.restaurantState
        if (restaurantState !is UiState.Success) return

        val data = restaurantState.data
        val prevIndex = uiState.value.randomIndex

        if (data.isEmpty()) return

        if (uiState.value.sameDrawCnt >= 2) {
            loadDrawRestaurants()
            _uiState.update { it.copy(sameDrawCnt = 0) }
            return
        }

        if (data.size == 1) {
            _uiState.update {
                it.copy(
                    randomIndex = 0,
                    sameDrawCnt = it.sameDrawCnt + 1,
                    hasPlayedDrawAnimation = false,
                )
            }
            return
        }

        val newIndex = data.indices
            .filter { it != prevIndex }
            .random()

        _uiState.update {
            it.copy(
                randomIndex = newIndex,
                sameDrawCnt = it.sameDrawCnt + 1,
                hasPlayedDrawAnimation = false,
            )
        }
    }

    fun markDrawAnimationPlayed() {
        _uiState.update {
            if (it.hasPlayedDrawAnimation) it
            else it.copy(hasPlayedDrawAnimation = true)
        }
    }
}