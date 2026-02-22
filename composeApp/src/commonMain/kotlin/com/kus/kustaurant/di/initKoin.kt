package com.kus.kustaurant.di

import com.kus.core.config.di.configModule
import com.kus.data.firstLaunch.di.firstLaunchDataModule
import com.kus.data.auth.di.authDataModule
import com.kus.data.network.di.networkModule
import com.kus.domain.firstLaunch.di.firstLaunchDomainModule
import com.kus.domain.auth.di.authDomainModule
import com.kus.feature.login.di.featureLoginModule
import com.kus.feature.onboarding.di.onboardingFeatureModule
import com.kus.feature.search.di.searchFeatureModule
import com.kus.feature.tier.di.tierFeatureModule
import com.kus.shared.data.tier.di.tierDataModule
import com.kus.shared.domain.tier.di.tierDomainModule
import org.koin.core.KoinApplication
import com.kus.feature.splash.di.splashFeatureModule
import com.kus.shared.data.search.di.searchDataModule
import com.kus.shared.domain.search.di.searchDomainModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    config: KoinAppDeclaration? = null,
    additionalModules: List<Module> = emptyList(),
) : KoinApplication {
    return startKoin {
        config?.invoke(this)

        modules(
            //core
            configModule,

            // domain
            firstLaunchDomainModule,
            authDomainModule,
            tierDomainModule,
            searchDomainModule,

            // data (repository 등 공통)
            networkModule,
            firstLaunchDataModule,
            authDataModule,
            tierDataModule,
            searchDataModule,

            // feature
            splashFeatureModule,
            onboardingFeatureModule,
            featureLoginModule,
            tierFeatureModule,
            searchFeatureModule,
        )

        modules(additionalModules)
    }
}