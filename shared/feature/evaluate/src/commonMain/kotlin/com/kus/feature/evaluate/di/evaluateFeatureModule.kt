package com.kus.feature.evaluate.di

import com.kus.feature.evaluate.ui.EvaluateViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val evaluateFeatureModule = module {
    viewModelOf(::EvaluateViewModel)
}
