package com.kus.domain.community.di

import com.kus.domain.community.usecase.DeleteCommunityCommentUseCase
import com.kus.domain.community.usecase.DeleteCommunityPostUseCase
import com.kus.domain.community.usecase.GetCommunityPostDetailUseCase
import com.kus.domain.community.usecase.GetCommunityPostListUseCase
import com.kus.domain.community.usecase.GetCommunityRankingListUseCase
import com.kus.domain.community.usecase.PatchPostModifyUseCase
import com.kus.domain.community.usecase.PostCommunityPostCommentReactUseCase
import com.kus.domain.community.usecase.PostCommunityPostCommentReplyUseCase
import com.kus.domain.community.usecase.PostCommunityPostCreateUseCase
import com.kus.domain.community.usecase.PostCommunityPostDetailScrapUseCase
import com.kus.domain.community.usecase.PostCommunityUploadImageUseCase
import com.kus.domain.community.usecase.PostPostLikeUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val communityDomainModule = module {
    singleOf(::DeleteCommunityCommentUseCase)
    singleOf(::DeleteCommunityPostUseCase)
    singleOf(::GetCommunityPostDetailUseCase)
    singleOf(::GetCommunityPostListUseCase)
    singleOf(::GetCommunityRankingListUseCase)
    singleOf(::PatchPostModifyUseCase)
    singleOf(::PostCommunityPostCommentReactUseCase)
    singleOf(::PostCommunityPostCommentReplyUseCase)
    singleOf(::PostCommunityPostCreateUseCase)
    singleOf(::PostCommunityPostDetailScrapUseCase)
    singleOf(::PostCommunityUploadImageUseCase)
    singleOf(::PostPostLikeUseCase)
}