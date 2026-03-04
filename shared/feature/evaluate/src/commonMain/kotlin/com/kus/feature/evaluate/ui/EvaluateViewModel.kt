package com.kus.feature.evaluate.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EvaluateViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(EvaluateUiState())
    val uiState: StateFlow<EvaluateUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = EvaluateUiState(
            restaurant = EvaluateRestaurant(
                restaurantId = 1,
                restaurantName = "제주곤이칼국수 건대점",
                mainTier = 1,
                restaurantCuisine = "한식",
                restaurantCuisineImgUrl = "https://kustaurant.s3.ap-northeast-2.amazonaws.com/common/cuisine-icon/카페디저트.svg",
                restaurantPosition = "건입~중문",
                restaurantAddress = "서울시 광진구 어딘가 222-22, 304호",
                situationList = arrayListOf("혼밥", "배달"),
                partnershipInfo = "학생증 제시 시에 전메뉴 10% 할인 대박!!!! 학생증 제시 시에 전메뉴 10% 할인 대박!!!! 학생증 제시 시에 전메뉴 10% 할인 대박!!!! 학생증 제시 시에 전메뉴 10% 할인 대박!!!!"
            ),
            evaluation = Evaluation(
                evaluationScore = 4.5,
                evaluationSituations = listOf(1, 3, 7),
                evaluationImgUrl = "https://picsum.photos/seed/evaluation1/400/300",
                evaluationComment = "오 좀 맛있는데?",
                starComments = listOf(
                    StarComment(
                        star = 4.5,
                        comment = "인생 최고의 식당입니다."
                    ),
                    StarComment(
                        star = 5.0,
                        comment = "다시 와도 좋을 것 같아요."
                    ),
                    StarComment(
                        star = 4.0,
                        comment = "가격 대비 훌륭합니다."
                    )
                )
            )
        )
    }

    fun updateEvaluationScore(score: Double) {
        _uiState.value = _uiState.value.copy(
            evaluation = _uiState.value.evaluation.copy(evaluationScore = score)
        )
    }

    fun updateEvaluationComment(comment: String) {
        _uiState.value = _uiState.value.copy(
            evaluation = _uiState.value.evaluation.copy(evaluationComment = comment)
        )
    }

    fun updateEvaluationSituations(situations: List<Int>) {
        _uiState.value = _uiState.value.copy(
            evaluation = _uiState.value.evaluation.copy(evaluationSituations = situations)
        )
    }

    fun updateEvaluationImage(imageUrl: String) {
        _uiState.value = _uiState.value.copy(
            evaluation = _uiState.value.evaluation.copy(evaluationImgUrl = imageUrl)
        )
    }

    fun updateImageBytes(imageBytes: ByteArray) {
        _uiState.value = _uiState.value.copy(
            evaluation = _uiState.value.evaluation.copy(imageBytes = imageBytes.copyOf())
        )
    }

    fun addStarComment(starComment: StarComment) {
        val updatedComments = _uiState.value.evaluation.starComments + starComment
        _uiState.value = _uiState.value.copy(
            evaluation = _uiState.value.evaluation.copy(starComments = updatedComments)
        )
    }

    fun removeStarComment(index: Int) {
        val updatedComments = _uiState.value.evaluation.starComments.filterIndexed { i, _ -> i != index }
        _uiState.value = _uiState.value.copy(
            evaluation = _uiState.value.evaluation.copy(starComments = updatedComments)
        )
    }

    fun submitEvaluation() {
        // TODO: API 호출 로직 구현
        // 평가 데이터를 서버로 전송
    }
}
