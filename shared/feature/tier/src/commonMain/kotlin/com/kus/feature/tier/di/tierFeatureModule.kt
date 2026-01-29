package com.kus.feature.tier.di

import com.kus.feature.tier.ui.TierViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val tierFeatureModule = module {
    factoryOf(::TierViewModel)
}