package com.kus.shared.domain.my.di

import com.kus.shared.domain.my.usecase.GetEvaluatedResUseCase
import com.kus.shared.domain.my.usecase.GetFavoriteResUseCase
import com.kus.shared.domain.my.usecase.GetMyCommentsUseCase
import com.kus.shared.domain.my.usecase.GetMyInfoUseCase
import com.kus.shared.domain.my.usecase.GetMyPostsUseCase
import com.kus.shared.domain.my.usecase.GetMyScrapsUseCase
import com.kus.shared.domain.my.usecase.PostFeedbackUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val myDomainModule = module {
    singleOf(::GetMyInfoUseCase)
    singleOf(::PostFeedbackUseCase)

    singleOf(::GetMyCommentsUseCase)
    singleOf(::GetMyPostsUseCase)
    singleOf(::GetMyScrapsUseCase)

    singleOf(::GetFavoriteResUseCase)
    singleOf(::GetEvaluatedResUseCase)
}
