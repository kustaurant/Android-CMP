package com.kus.kustaurant.di

import com.kus.data.firstLaunch.di.firstLaunchDataModule
import com.kus.domain.firstLaunch.di.firstLaunchDomainModule
import com.kus.feature.onBoarding.di.onboardingModule
import com.kus.feature.splash.di.splashModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)

        modules(
            // domain
            firstLaunchDomainModule,

            // data
            firstLaunchDataModule,

            // feature
            splashModule,
            onboardingModule,
            //loginModule,
            //platformModule,
        )
    }
}