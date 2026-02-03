package com.kus.feature.onboarding.di

import com.kus.feature.onboarding.ui.OnboardingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val onboardingFeatureModule = module {
    viewModelOf(::OnboardingViewModel)
}