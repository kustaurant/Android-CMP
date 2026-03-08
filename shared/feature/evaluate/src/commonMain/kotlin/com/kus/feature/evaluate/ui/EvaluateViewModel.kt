package com.kus.feature.evaluate.ui

import UiError
import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.shared.domain.evaluate.usecase.PostEvaluationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EvaluateViewModel(
    private val postEvaluationUseCase: PostEvaluationUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EvaluateUiState())
    val uiState: StateFlow<EvaluateUiState> = _uiState.asStateFlow()

    fun initRestaurant(restaurant: EvaluateRestaurant) {
        _uiState.update { it.copy(restaurant = restaurant) }
    }

    fun updateEvaluationScore(score: Double) {
        _uiState.update { it.copy(evaluation = it.evaluation.copy(evaluationScore = score)) }
    }

    fun updateEvaluationComment(comment: String) {
        _uiState.update { it.copy(evaluation = it.evaluation.copy(evaluationComment = comment)) }
    }

    fun updateEvaluationSituations(situations: List<Int>) {
        _uiState.update { it.copy(evaluation = it.evaluation.copy(evaluationSituations = situations)) }
    }

    fun updateEvaluationImage(imageUrl: String) {
        _uiState.update { it.copy(evaluation = it.evaluation.copy(evaluationImgUrl = imageUrl)) }
    }

    fun updateImageBytes(imageBytes: ByteArray) {
        _uiState.update { it.copy(evaluation = it.evaluation.copy(imageBytes = imageBytes.copyOf())) }
    }

    fun addStarComment(starComment: StarComment) {
        _uiState.update {
            it.copy(evaluation = it.evaluation.copy(starComments = it.evaluation.starComments + starComment))
        }
    }

    fun removeStarComment(index: Int) {
        _uiState.update {
            it.copy(
                evaluation = it.evaluation.copy(
                    starComments = it.evaluation.starComments.filterIndexed { i, _ -> i != index }
                )
            )
        }
    }

    fun submitEvaluation() = viewModelScope.launch {
        val state = _uiState.value
        _uiState.update { it.copy(submitState = UiState.Loading) }
        runCatching {
            postEvaluationUseCase(
                restaurantId = state.restaurant.restaurantId.toLong(),
                evaluationScore = state.evaluation.evaluationScore,
                evaluationSituations = state.evaluation.evaluationSituations.takeIf { it.isNotEmpty() },
                evaluationComment = state.evaluation.evaluationComment.takeIf { it.isNotBlank() },
                imageBytes = state.evaluation.imageBytes,
            )
        }.onSuccess {
            _uiState.update { it.copy(submitState = UiState.Success(Unit)) }
        }.onFailure {
            _uiState.update { it.copy(submitState = UiState.Failure(UiError.Network)) }
        }
    }
}
