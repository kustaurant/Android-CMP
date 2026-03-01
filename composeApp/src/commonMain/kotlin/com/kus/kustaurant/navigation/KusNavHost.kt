package com.kus.kustaurant.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.kus.core.serialization.KusJson
import com.kus.feature.community.navigation.communityNavGraph
import com.kus.feature.detail.navigation.Detail
import com.kus.feature.detail.navigation.detailNavGraph
import com.kus.feature.draw.navigation.drawNavGraph
import com.kus.feature.evaluate.navigation.Evaluate
import com.kus.feature.evaluate.navigation.evaluateNavGraph
import com.kus.feature.home.navigation.Home
import com.kus.feature.home.navigation.homeNavGraph
import com.kus.feature.login.navigation.Login
import com.kus.feature.login.navigation.loginNavGraph
import com.kus.feature.my.navigation.myNavGraph
import com.kus.feature.onboarding.navigatioin.Onboarding
import com.kus.feature.onboarding.navigatioin.onboardingNavGraph
import com.kus.feature.search.navigation.navigateToSearch
import com.kus.feature.search.navigation.searchNavGraph
import com.kus.feature.splash.navigation.Splash
import com.kus.feature.splash.navigation.splashNavGraph
import com.kus.feature.tier.navigation.TierCategorySelect
import com.kus.feature.tier.navigation.tierNavGraph
import com.kus.feature.tier.ui.TierFilterState

@Composable
fun KusNavHost(
    navController: NavHostController,
    durationMillis: Int,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Login,

        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis)) +
                    scaleIn(
                        initialScale = 0.98f,
                        animationSpec = tween(durationMillis)
                    )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis)) +
                    scaleOut(
                        targetScale = 0.98f,
                        animationSpec = tween(durationMillis)
                    )
        },

        popEnterTransition = {
            fadeIn(animationSpec = tween(durationMillis)) +
                    scaleIn(
                        initialScale = 0.98f,
                        animationSpec = tween(durationMillis)
                    )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(durationMillis)) +
                    scaleOut(
                        targetScale = 0.98f,
                        animationSpec = tween(durationMillis)
                    )
        },

        modifier = modifier
    ) {
        splashNavGraph(
            navigateToOnboarding = {
                navController.navigate(Onboarding) {
                    popUpTo(Splash) { inclusive = true }
                }
            },
            navigateToLogin = {
                navController.navigate(Login) {
                    popUpTo(Splash) { inclusive = true }
                }
            }
        )

        onboardingNavGraph(
            navigateToLogin = {
                navController.navigate(Login) {
                    popUpTo(Onboarding) { inclusive = true }
                }
            }
        )

        loginNavGraph(
            navigateToHome = {
                navController.navigate(Home) {
                    popUpTo(Login) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onShowMessage = onShowMessage
        )

        homeNavGraph(
            navigateToSearch = navController::navigateToSearch,
            navigateToAlert = { },
            navigateToTier = { /* TODO */ },
            navigateToDetail = { },
        )

        drawNavGraph(onShowMessage = onShowMessage)
        tierNavGraph(
            initialProvider = {
                val json = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<String>("tier_initial_json")

                if (json == null) TierFilterState()
                else KusJson.json.decodeFromString<TierFilterState>(json)
            },
            navigateToTierCategorySelect = { initial ->
                val json = KusJson.json.encodeToString(initial)
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("tier_initial_json", json)

                navController.navigate(TierCategorySelect)
            },
            navigateToDetail = { navController.navigate(Detail) },
            popBackStackWithResult = { result ->
                val json = KusJson.json.encodeToString(result)
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("tier_result_json", json)

                navController.popBackStack()
            },
            onBackButtonClick = { navController.popBackStack() }
        )

        communityNavGraph(onShowMessage = onShowMessage)
        myNavGraph(onShowMessage = onShowMessage)

        detailNavGraph(
            navigateToUp = navController::popBackStack,
            navigateToEvaluate = { navController.navigate(Evaluate) },
        )

        evaluateNavGraph(
            onBackClick = { navController.popBackStack() }
        )

        searchNavGraph(
            navigateToUp = navController::popBackStack,
            navigateToRestDetail = { /* Todo: 상세화면 연결 */ },
        )
    }
}