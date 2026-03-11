package com.kus.shared.domain.model.evaluate

data class PreviousEvaluation(
    val evaluationScore: Double?,
    val evaluationSituations: List<Int>,
    val evaluationImgUrl: String?,
    val evaluationComment: String?,
    val starComments: List<PreviousStarComment>,
)

data class PreviousStarComment(
    val star: Double,
    val comment: String,
)
