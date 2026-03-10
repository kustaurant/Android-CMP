package com.kus.kustaurant.navigation

import kotlin.reflect.KClass
import com.kus.feature.community.navigation.CommunityDetail
import com.kus.feature.community.navigation.CommunityWrite
import com.kus.feature.community.navigation.CommunityWriteModify
import com.kus.feature.login.navigation.Login
import com.kus.feature.onboarding.navigatioin.Onboarding
import com.kus.feature.splash.navigation.Splash

val HIDE_BOTTOM_BAR_ROUTES: Set<KClass<*>> = setOf(
    Login::class,
    Splash::class,
    Onboarding::class,
    CommunityDetail::class,
    CommunityWrite::class,
    CommunityWriteModify::class,
)

