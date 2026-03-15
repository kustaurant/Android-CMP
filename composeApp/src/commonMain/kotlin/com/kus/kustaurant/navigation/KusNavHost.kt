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
import com.kus.feature.community.config.CommunityKeys.COMMUNITY_LIST_REFRESH
import com.kus.feature.community.config.CommunityKeys.COMMUNITY_POST_DELETE_ID
import com.kus.feature.community.config.CommunityKeys.COMMUNITY_POST_EDIT_RESULT
import com.kus.feature.community.config.CommunityKeys.COMMUNITY_POST_UPDATE_PAYLOAD
import com.kus.feature.community.model.CommunityPostModifyPayload
import com.kus.feature.community.navigation.Community
import com.kus.feature.community.navigation.CommunityDetail
import com.kus.feature.community.navigation.CommunityWrite
import com.kus.feature.community.navigation.CommunityWriteModify
import com.kus.feature.community.navigation.communityNavGraph
import com.kus.feature.community.navigation.navigateToCommunityDetail
import com.kus.feature.detail.config.DetailKeys.DETAIL_EVALUATE_REFRESH
import com.kus.feature.detail.navigation.Detail
import com.kus.feature.detail.navigation.detailNavGraph
import com.kus.feature.detail.navigation.navigateToDetail
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
import com.kus.feature.tier.config.TierKeys.TIER_INITIAL_JSON
import com.kus.feature.tier.config.TierKeys.TIER_RESULT_JSON
import com.kus.feature.tier.navigation.Tier
import com.kus.feature.tier.navigation.TierCategorySelect
import com.kus.feature.tier.navigation.tierNavGraph
import com.kus.feature.tier.ui.TierFilterState
import com.kus.shared.domain.model.tier.filter.Cuisine

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
            navigateToTier = { cuisine: Cuisine ->
                val cuisine = Cuisine.entries.find { it == cuisine } ?: Cuisine.ALL
                val filter = TierFilterState(cuisines = setOf(cuisine)).normalized()
                val json = KusJson.json.encodeToString(filter)

                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(TIER_INITIAL_JSON, json)

                navController.navigate(Tier)
            },
            navigateToDetail = navController::navigateToDetail,
        )

        drawNavGraph(
            onSearchClick = {},
            onAlarmClick = {},
            onBackClick = { navController.popBackStack() },
            navigateToDrawResult = { route -> navController.navigate(route) },
            onShowMessage = onShowMessage
        )

        tierNavGraph(
            onShowMessage = onShowMessage,
            initialProvider = {
                val json = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<String>(TIER_INITIAL_JSON)

                if (json == null) TierFilterState()
                else KusJson.json.decodeFromString<TierFilterState>(json)
            },
            navigateToTierCategorySelect = { initial ->
                val json = KusJson.json.encodeToString(initial)
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(TIER_INITIAL_JSON, json)

                navController.navigate(TierCategorySelect)
            },
            navigateToDetail = { restaurantId -> navController.navigate(Detail(restaurantId)) },
            popBackStackWithResult = { result ->
                val json = KusJson.json.encodeToString(result)
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(TIER_RESULT_JSON, json)

                navController.popBackStack()
            },
            onBackButtonClick = { navController.popBackStack() }
        )

        communityNavGraph(
            onShowMessage = onShowMessage,
            onPostClick = { postId ->
                navController.navigate(CommunityDetail(postId)) {
                    popUpTo<Community> { inclusive = false }
                    launchSingleTop = true
                }
            },
            onPostCreated = { postId ->
                navController.getBackStackEntry<Community>()
                    .savedStateHandle[COMMUNITY_LIST_REFRESH] = true

                navController.navigate(CommunityDetail(postId)) {
                    popUpTo<Community> { inclusive = false }
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
            onPostWriteClick = {
                navController.navigate(CommunityWrite)
            },
            onPostModifyClick = { encoded ->
                navController.navigate(CommunityWriteModify(encoded))
            },
            onSearchClick = {},
            onPostModifiedInDetail = { payload ->
                val json =
                    KusJson.json.encodeToString(CommunityPostModifyPayload.serializer(), payload)
                navController.getBackStackEntry<Community>()
                    .savedStateHandle[COMMUNITY_POST_UPDATE_PAYLOAD] = json
            },
            onDetailBackClick = { payload ->
                if (payload != null) {
                    val json = KusJson.json.encodeToString(
                        CommunityPostModifyPayload.serializer(),
                        payload
                    )
                    navController.getBackStackEntry<Community>()
                        .savedStateHandle[COMMUNITY_POST_UPDATE_PAYLOAD] = json
                }
                navController.popBackStack()
            },
            onPostDeletedInDetail = { postId ->
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(COMMUNITY_POST_DELETE_ID, postId)

                navController.popBackStack()
            },
        )
        myNavGraph(
            onShowMessage = onShowMessage,
            navController = navController,
            navigateToUp = navController::popBackStack,
            onRestItemClick = navController::navigateToDetail,
            onArticleClick = navController::navigateToCommunityDetail,
            navigateToLogin = {
                navController.navigate(Login) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
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
        )

        evaluateNavGraph(
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
