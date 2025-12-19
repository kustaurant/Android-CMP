package com.kus.onboarding.di

import com.kus.onboarding.ui.OnboardingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val onboardingModule = module {
    viewModelOf(::OnboardingViewModel)
}