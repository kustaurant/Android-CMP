package com.kus.feature.login.di

import com.kus.feature.login.ui.LoginViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureLoginModule = module {
    viewModelOf(::LoginViewModel)
}