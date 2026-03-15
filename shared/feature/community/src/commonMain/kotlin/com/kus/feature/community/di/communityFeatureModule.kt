package com.kus.feature.community.di

import com.kus.feature.community.ui.CommunityViewModel
import com.kus.feature.community.ui.detail.CommunityDetailViewModel
import com.kus.feature.community.ui.write.CommunityWriteViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val communityFeatureModule = module {
    viewModelOf(::CommunityViewModel)
    viewModelOf(::CommunityDetailViewModel)
    viewModelOf(::CommunityWriteViewModel)
}