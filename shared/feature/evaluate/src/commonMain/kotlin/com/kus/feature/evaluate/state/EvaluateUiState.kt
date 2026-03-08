package com.kus.feature.evaluate.state

import UiState
import com.kus.feature.evaluate.model.Evaluation
import com.kus.feature.evaluate.model.EvaluateRestaurant

data class EvaluateUiState(
    val restaurant: EvaluateRestaurant = EvaluateRestaurant.empty(),
    val evaluation: UiState<Evaluation> = UiState.Loading,
    val submitState: UiState<Unit> = UiState.Idle,
)
