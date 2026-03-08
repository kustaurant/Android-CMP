package com.kus.feature.my.di

import com.kus.feature.my.ui.MyViewModel
import com.kus.feature.my.ui.subscreen.FeedbackViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val myFeatureModule = module {
    viewModelOf(::MyViewModel)
    viewModelOf(::FeedbackViewModel)
}