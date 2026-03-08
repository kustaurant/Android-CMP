package com.kus.feature.my.ui.restaurant

import UiError
import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.feature.my.ui.state.CheckedUiState
import com.kus.shared.domain.model.my.EvaluatedResItem
import com.kus.shared.domain.my.usecase.GetEvaluatedResUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckedResViewModel(
    private val getEvaluatedResUseCase: GetEvaluatedResUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckedUiState())
    val uiState = _uiState.asStateFlow()

    fun getEvaluatedRes() = viewModelScope.launch {
        runCatching { getEvaluatedResUseCase() }
            .onSuccess { result ->
                _uiState.update {
                    it.copy(
                        restaurants = UiState.Success(
                            listOf(
                                EvaluatedResItem(
                                    restaurantId = 1,
                                    restaurantName = "쿠쿠 스시",
                                    restaurantImgURL = "https://picsum.photos/300/200?1",
                                    cuisine = "일식",
                                    evaluationScore = 4.5,
                                    evaluationBody = "초밥이 신선하고 직원분들이 친절했어요.",
                                    evaluationItemScores = listOf("맛있어요", "신선해요", "친절해요")
                                ),
                                EvaluatedResItem(
                                    restaurantId = 2,
                                    restaurantName = "마라공방",
                                    restaurantImgURL = "https://picsum.photos/300/200?2",
                                    cuisine = "중식",
                                    evaluationScore = 4.2,
                                    evaluationBody = "마라탕 국물이 진하고 재료 선택이 다양해서 좋았어요.",
                                    evaluationItemScores = listOf("국물이 진해요", "재료가 다양해요", "매콤해요")
                                ),
                                EvaluatedResItem(
                                    restaurantId = 3,
                                    restaurantName = "파스타하우스",
                                    restaurantImgURL = "https://picsum.photos/300/200?3",
                                    cuisine = "양식",
                                    evaluationScore = 3.8,
                                    evaluationBody = "크림 파스타가 부드럽고 분위기가 좋았습니다.",
                                    evaluationItemScores = listOf("분위기 좋아요", "부드러워요", "양이 적당해요")
                                )
                            )
                        )
                    )
                }
            }
            .onFailure { error ->
                _uiState.update {
                    it.copy(restaurants = UiState.Failure(UiError.Network))
                }
            }
    }
}