package com.kus.feature.tier.di

import com.kus.feature.tier.ui.TierViewModel
import com.kus.feature.tier.ui.category.TierCategorySelectViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val tierFeatureModule = module {
    viewModelOf(::TierViewModel)
    viewModelOf(::TierCategorySelectViewModel)
}