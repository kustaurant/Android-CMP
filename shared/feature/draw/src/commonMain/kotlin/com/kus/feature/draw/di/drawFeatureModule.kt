package com.kus.feature.draw.di

import com.kus.feature.draw.ui.result.DrawResultViewModel
import com.kus.feature.draw.ui.select.DrawSelectViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val drawFeatureModule = module {
    viewModelOf(::DrawSelectViewModel)
    viewModelOf(::DrawResultViewModel)
}