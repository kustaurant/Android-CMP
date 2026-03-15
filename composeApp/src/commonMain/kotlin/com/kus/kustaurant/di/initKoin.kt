package com.kus.kustaurant.di

import com.kus.core.config.di.configModule
import com.kus.data.firstLaunch.di.firstLaunchDataModule
import com.kus.data.auth.di.authDataModule
import com.kus.data.community.di.communityDataModule
import com.kus.data.draw.di.DrawDataModule
import com.kus.data.network.di.networkModule
import com.kus.domain.firstLaunch.di.firstLaunchDomainModule
import com.kus.domain.auth.di.authDomainModule
import com.kus.domain.community.di.communityDomainModule
import com.kus.domain.draw.di.drawDomainModule
import com.kus.feature.community.di.communityFeatureModule
import com.kus.feature.draw.di.drawFeatureModule
import com.kus.feature.home.di.homeFeatureModule
import com.kus.feature.login.di.featureLoginModule
import com.kus.feature.onboarding.di.onboardingFeatureModule
import com.kus.feature.tier.di.tierFeatureModule
import com.kus.shared.data.tier.di.tierDataModule
import com.kus.shared.domain.tier.di.tierDomainModule
import org.koin.core.KoinApplication
import com.kus.feature.splash.di.splashFeatureModule
import com.kus.kustaurant.home.di.homeDataModule
import com.kus.shared.domain.home.di.homeDomainModule
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
            homeDomainModule,
            communityDomainModule,
            drawDomainModule,

            // data (repository 등 공통)
            networkModule,
            firstLaunchDataModule,
            authDataModule,
            tierDataModule,
            homeDataModule,
            communityDataModule,
            DrawDataModule,

            // feature
            splashFeatureModule,
            onboardingFeatureModule,
            featureLoginModule,
            tierFeatureModule,
            homeFeatureModule,
            communityFeatureModule,
            drawFeatureModule
        )

        modules(additionalModules)
    }
}