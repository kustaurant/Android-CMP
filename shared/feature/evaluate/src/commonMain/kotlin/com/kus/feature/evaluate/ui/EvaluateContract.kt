package com.kus.feature.evaluate.ui

import UiState

data class EvaluateUiState(
    val restaurant: EvaluateRestaurant = EvaluateRestaurant.empty(),
    val evaluation: Evaluation = Evaluation.empty(),
    val submitState: UiState<Unit> = UiState.Idle,
)

data class EvaluateRestaurant(
    val restaurantId: Int,
    val restaurantName: String,
    val mainTier: Int,
    val restaurantCuisine: String,
    val restaurantCuisineImgUrl: String,
    val restaurantPosition: String,
    val restaurantAddress: String,
    val situationList: ArrayList<String>,
    val partnershipInfo: String,
) {
    companion object {
        fun empty() = EvaluateRestaurant(
            restaurantId = 0,
            restaurantName = "",
            mainTier = 0,
            restaurantCuisine = "",
            restaurantCuisineImgUrl = "",
            restaurantPosition = "",
            restaurantAddress = "",
            situationList = arrayListOf(),
            partnershipInfo = "",
        )
    }
}

data class Evaluation(
    val evaluationScore: Double,
    val evaluationSituations: List<Int>,
    val evaluationImgUrl: String,
    val evaluationComment: String,
    val starComments: List<StarComment>,
    val imageBytes: ByteArray? = null,
) {
    companion object {
        fun empty() = Evaluation(
            evaluationScore = 0.0,
            evaluationSituations = emptyList(),
            evaluationImgUrl = "",
            evaluationComment = "",
            starComments = emptyList(),
            imageBytes = null,
        )
    }
}

data class StarComment(
    val star: Double,
    val comment: String,
)
