package com.kus.kustaurant.di

import com.kus.data.firstLaunch.di.firstLaunchDataModule
import com.kus.data.network.di.networkModule
import com.kus.domain.firstLaunch.di.firstLaunchDomainModule
import com.kus.feature.onboarding.di.onboardingFeatureModule
import com.kus.feature.splash.di.splashFeatureModule
import com.kus.feature.tier.di.tierFeatureModule
import com.kus.shared.data.tier.di.tierDataModule
import com.kus.shared.domain.tier.di.tierDomainModule
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
            tierDomainModule,

            // data (repository 등 공통)
            firstLaunchDataModule,
            networkModule,
            tierDataModule,

            // feature
            splashFeatureModule,
            onboardingFeatureModule,
            tierFeatureModule,
        )

        modules(additionalModules)
    }
}