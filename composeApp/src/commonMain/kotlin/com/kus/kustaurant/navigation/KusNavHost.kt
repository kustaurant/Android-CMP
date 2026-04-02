package com.kus.kustaurant.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kus.core.serialization.KusJson
import com.kus.feature.community.config.CommunityKeys.COMMUNITY_LIST_REFRESH
import com.kus.feature.community.config.CommunityKeys.COMMUNITY_POST_DELETE_ID
import com.kus.feature.community.config.CommunityKeys.COMMUNITY_POST_EDIT_RESULT
import com.kus.feature.community.config.CommunityKeys.COMMUNITY_POST_UPDATE_PAYLOAD
import com.kus.feature.community.model.CommunityPostModifyPayload
import com.kus.feature.community.navigation.CommunityDetail
import com.kus.feature.community.navigation.CommunityWriteModify
import com.kus.feature.community.navigation.communityFullscreenNavGraph
import com.kus.feature.community.navigation.navigateToCommunityDetail
import com.kus.feature.detail.config.DetailKeys.DETAIL_EVALUATE_REFRESH
import com.kus.feature.detail.navigation.detailNavGraph
import com.kus.feature.detail.navigation.navigateToDetail
import com.kus.feature.evaluate.navigation.Evaluate
import com.kus.feature.evaluate.navigation.evaluateNavGraph
import com.kus.feature.login.navigation.Login
import com.kus.feature.login.navigation.loginNavGraph
import com.kus.feature.my.navigation.myFullscreenNavGraph
import com.kus.feature.onboarding.navigatioin.Onboarding
import com.kus.feature.onboarding.navigatioin.onboardingNavGraph
import com.kus.feature.search.navigation.searchNavGraph
import com.kus.feature.splash.navigation.Splash
import com.kus.feature.splash.navigation.splashNavGraph
import com.kus.feature.tier.config.TierKeys.TIER_INITIAL_JSON
import com.kus.feature.tier.config.TierKeys.TIER_RESULT_JSON
import com.kus.feature.tier.navigation.tierFullscreenNavGraph
import com.kus.feature.tier.ui.TierFilterState

@Composable
fun KusNavHost(
    navController: NavHostController,
    durationMillis: Int,
    onShowMessage: (String) -> Unit,
    onSplashLoadingChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Splash,
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
            navigateToHome = {
                navController.navigate(Main) {
                    popUpTo(Splash) { inclusive = true }
                    launchSingleTop = true
                }
            },
            navigateToLogin = {
                navController.navigate(Login) {
                    popUpTo(Splash) { inclusive = true }
                }
            },
            onLoadingChanged = onSplashLoadingChanged,
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
                navController.navigate(Main) {
                    popUpTo(Login) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onShowMessage = onShowMessage
        )

        composable<Main> { backStackEntry ->
            MainScreen(
                rootNavController = navController,
                mainBackStackEntry = backStackEntry,
                durationMillis = durationMillis,
                onShowMessage = onShowMessage,
                modifier = Modifier.fillMaxSize()
            )
        }

        tierFullscreenNavGraph(
            initialProvider = {
                val json = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<String>(TIER_INITIAL_JSON)

                if (json == null) TierFilterState()
                else KusJson.json.decodeFromString<TierFilterState>(json)
            },
            popBackStackWithResult = { result ->
                val json = KusJson.json.encodeToString(result)
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(TIER_RESULT_JSON, json)

                navController.popBackStack()
            },
            onBackButtonClick = { navController.popBackStack() }
        )

        communityFullscreenNavGraph(
            onShowMessage = onShowMessage,
            onPostCreated = { postId ->
                navController.getBackStackEntry<Main>()
                    .savedStateHandle[COMMUNITY_LIST_REFRESH] = true

                navController.navigate(CommunityDetail(postId)) {
                    popUpTo<Main> { inclusive = false }
                    launchSingleTop = true
                }
            },
            onPostModified = { payload ->
                val json =
                    KusJson.json.encodeToString(CommunityPostModifyPayload.serializer(), payload)
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(COMMUNITY_POST_EDIT_RESULT, json)
                navController.popBackStack()
            },
            onBackButtonClick = { navController.popBackStack() },
            onPostModifyClick = { encoded ->
                navController.navigate(CommunityWriteModify(encoded))
            },
            onPostModifiedInDetail = { payload ->
                val json =
                    KusJson.json.encodeToString(CommunityPostModifyPayload.serializer(), payload)
                navController.getBackStackEntry<Main>()
                    .savedStateHandle[COMMUNITY_POST_UPDATE_PAYLOAD] = json
            },
            onDetailBackClick = { payload ->
                if (payload != null) {
                    val json = KusJson.json.encodeToString(
                        CommunityPostModifyPayload.serializer(),
                        payload
                    )
                    navController.getBackStackEntry<Main>()
                        .savedStateHandle[COMMUNITY_POST_UPDATE_PAYLOAD] = json
                }
                navController.popBackStack()
            },
            onPostDeletedInDetail = { postId ->
                navController.getBackStackEntry<Main>()
                    .savedStateHandle[COMMUNITY_POST_DELETE_ID] = postId

                navController.popBackStack()
            },
        )

        myFullscreenNavGraph(
            navigateToUp = navController::popBackStack,
            onRestItemClick = navController::navigateToDetail,
            onArticleClick = navController::navigateToCommunityDetail,
            onShowMessage = onShowMessage,
        )

        detailNavGraph(
            navigateToUp = navController::popBackStack,
            navigateToEvaluate = { restaurant ->
                navController.navigate(
                    Evaluate(
                        restaurantId = restaurant.restaurantId,
                        restaurantName = restaurant.restaurantName,
                        mainTier = restaurant.mainTier,
                        restaurantCuisine = restaurant.restaurantCuisine,
                        restaurantCuisineImgUrl = restaurant.restaurantCuisineImgUrl,
                        restaurantPosition = restaurant.restaurantPosition,
                        restaurantAddress = restaurant.restaurantAddress,
                        situationList = restaurant.situationList,
                        partnershipInfo = restaurant.partnershipInfo,
                    )
                )
            },
            onShowMessage = onShowMessage,
        )

        evaluateNavGraph(
            onShowMessage = onShowMessage,
            onBackClick = { navController.popBackStack() },
            onSubmitSuccess = {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(DETAIL_EVALUATE_REFRESH, true)

                navController.popBackStack()
            }
        )

        searchNavGraph(
            navigateToUp = navController::popBackStack,
            navigateToRestDetail = navController::navigateToDetail,
        )
    }
}
