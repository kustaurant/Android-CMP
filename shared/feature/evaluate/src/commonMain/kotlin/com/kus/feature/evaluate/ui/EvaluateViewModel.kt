package com.kus.feature.evaluate.ui

import UiError
import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.feature.evaluate.model.Evaluation
import com.kus.feature.evaluate.model.EvaluateRestaurant
import com.kus.feature.evaluate.model.StarComment
import com.kus.feature.evaluate.state.EvaluateUiState
import com.kus.shared.domain.evaluate.usecase.GetEvaluationUseCase
import com.kus.shared.domain.evaluate.usecase.PostEvaluationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EvaluateViewModel(
    private val getEvaluationUseCase: GetEvaluationUseCase,
    private val postEvaluationUseCase: PostEvaluationUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EvaluateUiState())
    val uiState: StateFlow<EvaluateUiState> = _uiState.asStateFlow()

    fun getPreviousEvaluation(restaurantId: Long) = viewModelScope.launch {
        _uiState.update { it.copy(evaluation = UiState.Loading) }
        runCatching {
            getEvaluationUseCase(restaurantId)
        }.onSuccess { previous ->
            _uiState.update {
                it.copy(
                    evaluation = UiState.Success(
                        Evaluation(
                            evaluationScore = previous.evaluationScore ?: 0.0,
                            evaluationSituations = previous.evaluationSituations,
                            evaluationImgUrl = previous.evaluationImgUrl ?: "",
                            evaluationComment = previous.evaluationComment ?: "",
                            starComments = previous.starComments.map { StarComment(it.star, it.comment) },
                            imageBytes = null,
                        )
                    )
                )
            }
        }.onFailure {
            _uiState.update { it.copy(evaluation = UiState.Failure(UiError.Network)) }
        }
    }

    fun initRestaurant(restaurant: EvaluateRestaurant) {
        _uiState.update { it.copy(restaurant = restaurant) }
    }

    private fun updateEvaluation(transform: (Evaluation) -> Evaluation) {
        val current = _uiState.value.evaluation
        if (current !is UiState.Success) return
        _uiState.update { it.copy(evaluation = UiState.Success(transform(current.data))) }
    }

    fun updateEvaluationScore(score: Double) {
        updateEvaluation { it.copy(evaluationScore = score) }
    }

    fun updateEvaluationComment(comment: String) {
        updateEvaluation { it.copy(evaluationComment = comment) }
    }

    fun updateEvaluationSituations(situations: List<Int>) {
        updateEvaluation { it.copy(evaluationSituations = situations) }
    }

    fun updateEvaluationImage(imageUrl: String) {
        updateEvaluation { it.copy(evaluationImgUrl = imageUrl) }
    }

    fun updateImageBytes(imageBytes: ByteArray) {
        updateEvaluation { it.copy(imageBytes = imageBytes.copyOf()) }
    }

    fun addStarComment(starComment: StarComment) {
        updateEvaluation { it.copy(starComments = it.starComments + starComment) }
    }

    fun removeStarComment(index: Int) {
        updateEvaluation { it.copy(starComments = it.starComments.filterIndexed { i, _ -> i != index }) }
    }

    fun submitEvaluation() = viewModelScope.launch {
        val evaluationState = _uiState.value.evaluation
        if (evaluationState !is UiState.Success) return@launch
        val evaluation = evaluationState.data
        val restaurantId = _uiState.value.restaurant.restaurantId.toLong()

        _uiState.update { it.copy(submitState = UiState.Loading) }
        runCatching {
            postEvaluationUseCase(
                restaurantId = restaurantId,
                evaluationScore = evaluation.evaluationScore,
                evaluationSituations = evaluation.evaluationSituations.takeIf { it.isNotEmpty() },
                evaluationComment = evaluation.evaluationComment.takeIf { it.isNotBlank() },
                imageBytes = evaluation.imageBytes,
            )
        }.onSuccess {
            _uiState.update { it.copy(submitState = UiState.Success(Unit)) }
        }.onFailure {
            _uiState.update { it.copy(submitState = UiState.Failure(UiError.Network)) }
        }
    }
}
