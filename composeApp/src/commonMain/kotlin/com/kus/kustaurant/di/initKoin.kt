package com.kus.kustaurant.di

import com.kus.data.firstLaunch.di.firstLaunchDataModule
import com.kus.domain.firstLaunch.di.firstLaunchDomainModule
import com.kus.feature.onboarding.di.onboardingModule
import com.kus.feature.splash.di.splashModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    config: KoinAppDeclaration? = null,
    additionalModules: List<Module> = emptyList(),
) {
    startKoin {
        config?.invoke(this)

        modules(
            // domain
            firstLaunchDomainModule,

            // data (repository 등 공통)
            firstLaunchDataModule,

            // feature
            splashModule,
            onboardingModule,
        )

        modules(additionalModules)
    }
}