package com.kus.kustaurant.evaluate.remote.mapper

import com.kus.kustaurant.evaluate.remote.response.EvaluationResponse
import com.kus.kustaurant.evaluate.remote.response.StarCommentResponse
import com.kus.shared.domain.model.evaluate.PreviousEvaluation
import com.kus.shared.domain.model.evaluate.PreviousStarComment

fun EvaluationResponse.toDomain(): PreviousEvaluation =
    PreviousEvaluation(
        evaluationScore = evaluationScore,
        evaluationSituations = evaluationSituations ?: emptyList(),
        evaluationImgUrl = evaluationImgUrl,
        evaluationComment = evaluationComment,
        starComments = starComments.map { it.toDomain() },
    )

fun StarCommentResponse.toDomain(): PreviousStarComment =
    PreviousStarComment(
        star = star,
        comment = comment,
    )
