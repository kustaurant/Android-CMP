package com.kus.feature.splash.di

import com.kus.feature.splash.ui.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val splashFeatureModule = module {
    viewModelOf(::SplashViewModel)
}