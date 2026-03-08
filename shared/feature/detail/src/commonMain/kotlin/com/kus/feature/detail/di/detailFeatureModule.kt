package com.kus.feature.detail.di

import com.kus.feature.detail.ui.DetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val detailFeatureModule = module {
    viewModelOf(::DetailViewModel)
}
