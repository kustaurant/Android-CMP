package com.kus.kustaurant.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kus.core.serialization.KusJson
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.community.config.CommunityKeys.COMMUNITY_LIST_REFRESH
import com.kus.feature.community.config.CommunityKeys.COMMUNITY_POST_DELETE_ID
import com.kus.feature.community.config.CommunityKeys.COMMUNITY_POST_UPDATE_PAYLOAD
import com.kus.feature.community.navigation.Community
import com.kus.feature.community.navigation.CommunityDetail
import com.kus.feature.community.navigation.CommunityWrite
import com.kus.feature.community.navigation.communityMainNavGraph
import com.kus.feature.detail.navigation.Detail
import com.kus.feature.draw.navigation.Draw
import com.kus.feature.draw.navigation.drawNavGraph
import com.kus.feature.home.navigation.Home
import com.kus.feature.home.navigation.homeNavGraph
import com.kus.feature.login.navigation.Login
import com.kus.feature.my.navigation.My
import com.kus.feature.my.navigation.myMainNavGraph
import com.kus.feature.my.navigation.navigateToCheckedRest
import com.kus.feature.my.navigation.navigateToEditProfile
import com.kus.feature.my.navigation.navigateToFavoriteRest
import com.kus.feature.my.navigation.navigateToFeedback
import com.kus.feature.my.navigation.navigateToMyArticle
import com.kus.feature.my.navigation.navigateToMyComment
import com.kus.feature.my.navigation.navigateToMyWebView
import com.kus.feature.my.navigation.navigateToScrap
import com.kus.feature.search.navigation.Search
import com.kus.feature.tier.config.TierKeys.TIER_INITIAL_JSON
import com.kus.feature.tier.config.TierKeys.TIER_RESULT_JSON
import com.kus.feature.tier.navigation.Tier
import com.kus.feature.tier.navigation.TierCategorySelect
import com.kus.feature.tier.navigation.tierMainNavGraph
import com.kus.feature.tier.ui.TierFilterState
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.feature.tier.TierKeys as TierResultKeys

@Composable
fun MainScreen(
    rootNavController: NavHostController,
    mainBackStackEntry: NavBackStackEntry,
    durationMillis: Int,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val mainNavController = rememberNavController()
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val selectedKey = BottomTab.fromRoute(navBackStackEntry?.destination?.route).key

    val tierResultJson by mainBackStackEntry.savedStateHandle
        .getStateFlow<String?>(TIER_RESULT_JSON, null)
        .collectAsStateWithLifecycle()

    val communityShouldRefresh by mainBackStackEntry.savedStateHandle
        .getStateFlow(COMMUNITY_LIST_REFRESH, false)
        .collectAsStateWithLifecycle()

    val communityUpdatePayloadJson by mainBackStackEntry.savedStateHandle
        .getStateFlow<String?>(COMMUNITY_POST_UPDATE_PAYLOAD, null)
        .collectAsStateWithLifecycle()

    val communityDeletedPostId by mainBackStackEntry.savedStateHandle
        .getStateFlow<Long?>(COMMUNITY_POST_DELETE_ID, null)
        .collectAsStateWithLifecycle()

    LaunchedEffect(tierResultJson) {
        val json = tierResultJson ?: return@LaunchedEffect

        runCatching {
            mainNavController.getBackStackEntry<Tier>()
                .savedStateHandle[TierResultKeys.RESULT_FILTER] = json
        }

        mainBackStackEntry.savedStateHandle.remove<String>(TIER_RESULT_JSON)
    }

    LaunchedEffect(communityShouldRefresh) {
        if (!communityShouldRefresh) return@LaunchedEffect

        runCatching {
            mainNavController.getBackStackEntry<Community>()
                .savedStateHandle[COMMUNITY_LIST_REFRESH] = true
        }

        mainBackStackEntry.savedStateHandle[COMMUNITY_LIST_REFRESH] = false
    }

    LaunchedEffect(communityUpdatePayloadJson) {
        val json = communityUpdatePayloadJson ?: return@LaunchedEffect

        runCatching {
            mainNavController.getBackStackEntry<Community>()
                .savedStateHandle[COMMUNITY_POST_UPDATE_PAYLOAD] = json
        }

        mainBackStackEntry.savedStateHandle.remove<String>(COMMUNITY_POST_UPDATE_PAYLOAD)
    }

    LaunchedEffect(communityDeletedPostId) {
        val postId = communityDeletedPostId ?: return@LaunchedEffect

        runCatching {
            mainNavController.getBackStackEntry<Community>()
                .savedStateHandle[COMMUNITY_POST_DELETE_ID] = postId
        }

        mainBackStackEntry.savedStateHandle.remove<Long>(COMMUNITY_POST_DELETE_ID)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = KusTheme.colors.c_FFFFFF,
        bottomBar = {
            KusBottomBar(
                selectedKey = selectedKey,
                onNavigateToTab = { key -> mainNavController.navigateToTab(key) },
            )
        },
    ) { padding ->
        NavHost(
            navController = mainNavController,
            startDestination = Home,
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
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            homeNavGraph(
                navigateToSearch = { rootNavController.navigate(Search) },
                navigateToAlert = { },
                navigateToTier = { cuisine: Cuisine ->
                    val resolved = Cuisine.entries.find { it == cuisine } ?: Cuisine.ALL
                    val filter = TierFilterState(cuisines = setOf(resolved)).normalized()
                    val json = KusJson.json.encodeToString(filter)

                    mainNavController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(TIER_INITIAL_JSON, json)

                    mainNavController.navigateToTab(BottomTab.TIER.key)
                },
                navigateToDetail = { restaurantId ->
                    rootNavController.navigate(Detail(restaurantId))
                },
            )

            drawNavGraph(
                onShowMessage = onShowMessage,
                navigateToDrawResult = { route -> mainNavController.navigate(route) },
                onBackClick = { mainNavController.popBackStack() },
            )

            tierMainNavGraph(
                onShowMessage = onShowMessage,
                initialProvider = {
                    val json = mainNavController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<String>(TIER_INITIAL_JSON)

                    if (json == null) TierFilterState()
                    else KusJson.json.decodeFromString<TierFilterState>(json)
                },
                navigateToTierCategorySelect = { initial ->
                    val json = KusJson.json.encodeToString(initial)
                    mainBackStackEntry.savedStateHandle[TIER_INITIAL_JSON] = json

                    rootNavController.navigate(TierCategorySelect)
                },
                navigateToDetail = { restaurantId ->
                    rootNavController.navigate(Detail(restaurantId))
                },
            )

            communityMainNavGraph(
                onShowMessage = onShowMessage,
                onBackButtonClick = { mainNavController.popBackStack() },
                onPostClick = { postId ->
                    rootNavController.navigate(CommunityDetail(postId))
                },
                onPostWriteClick = {
                    rootNavController.navigate(CommunityWrite)
                },
            )

            myMainNavGraph(
                onShowMessage = onShowMessage,
                navigateToEditProfile = { nickName, email, phoneNumber ->
                    rootNavController.navigateToEditProfile(nickName, email, phoneNumber)
                },
                navigateToNotice = {
                    rootNavController.navigateToMyWebView(
                        title = "공지사항",
                        url = "https://kustaurant.com/notice",
                    )
                },
                navigateToTerms = {
                    rootNavController.navigateToMyWebView(
                        title = "이용약관",
                        url = "https://kustaurant.com/terms_of_use",
                    )
                },
                navigateToPrivacyPolicy = {
                    rootNavController.navigateToMyWebView(
                        title = "개인정보처리방침",
                        url = "https://kustaurant.com/privacy-policy",
                    )
                },
                navigateToFeedback = rootNavController::navigateToFeedback,
                navigateToSavedRest = rootNavController::navigateToFavoriteRest,
                navigateToCheckedRest = rootNavController::navigateToCheckedRest,
                navigateToMyArticle = rootNavController::navigateToMyArticle,
                navigateToMyComment = rootNavController::navigateToMyComment,
                navigateToScrap = rootNavController::navigateToScrap,
                navigateToLogin = {
                    rootNavController.navigate(Login) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}

private fun NavHostController.navigateToTab(key: String) {
    when (key) {
        BottomTab.HOME.key -> navigate(Home) { tabOptions(this@navigateToTab) }
        BottomTab.DRAW.key -> navigate(Draw) { tabOptions(this@navigateToTab) }
        BottomTab.TIER.key -> navigate(Tier) { tabOptions(this@navigateToTab) }
        BottomTab.COMMUNITY.key -> navigate(Community) { tabOptions(this@navigateToTab) }
        BottomTab.MY.key -> navigate(My) { tabOptions(this@navigateToTab) }
    }
}

private fun NavOptionsBuilder.tabOptions(navController: NavHostController) {
    launchSingleTop = true
    restoreState = true
    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
}
