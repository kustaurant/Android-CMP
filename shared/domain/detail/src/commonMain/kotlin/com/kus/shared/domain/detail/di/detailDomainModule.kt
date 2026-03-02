package com.kus.shared.domain.detail.di

import com.kus.shared.domain.detail.usecase.GetRestaurantDetailUseCase
import com.kus.shared.domain.detail.usecase.GetRestaurantReviewsUseCase
import com.kus.shared.domain.detail.usecase.PutEvaluationReactionUseCase
import com.kus.shared.domain.detail.usecase.PutCommentReactionUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val detailDomainModule = module {
    singleOf(::GetRestaurantDetailUseCase)
    singleOf(::GetRestaurantReviewsUseCase)
    singleOf(::PutCommentReactionUseCase)
    singleOf(::PutEvaluationReactionUseCase)
}
