package com.kus.feature.onBoarding.di

import com.kus.feature.onBoarding.ui.OnboardingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val onboardingModule = module {
    viewModelOf(::OnboardingViewModel)
}