package com.kus.data.my.di

import com.kus.data.my.api.MyApi
import com.kus.data.my.api.MyCommunityApi
import com.kus.data.my.api.MyRestaurantApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val myDataModule = module {
    singleOf(::MyApi)
    singleOf(::MyCommunityApi)
    singleOf(::MyRestaurantApi)
}
