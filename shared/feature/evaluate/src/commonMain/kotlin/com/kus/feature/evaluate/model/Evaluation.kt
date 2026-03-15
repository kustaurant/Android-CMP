package com.kus.feature.evaluate.model

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
