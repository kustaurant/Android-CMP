package com.kus.feature.my.di

import com.kus.feature.my.ui.MyViewModel
import com.kus.feature.my.ui.community.MyArticleViewModel
import com.kus.feature.my.ui.community.MyCommentViewModel
import com.kus.feature.my.ui.community.MyScrapViewModel
import com.kus.feature.my.ui.editprofile.EditProfileViewModel
import com.kus.feature.my.ui.restaurant.CheckedResViewModel
import com.kus.feature.my.ui.restaurant.FavoriteResViewModel
import com.kus.feature.my.ui.feedback.FeedbackViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val myFeatureModule = module {
    viewModelOf(::MyViewModel)
    viewModelOf(::FeedbackViewModel)
    viewModelOf(::FavoriteResViewModel)
    viewModelOf(::CheckedResViewModel)
    viewModelOf(::MyArticleViewModel)
    viewModelOf(::MyCommentViewModel)
    viewModelOf(::MyScrapViewModel)
    viewModelOf(::EditProfileViewModel)
}
