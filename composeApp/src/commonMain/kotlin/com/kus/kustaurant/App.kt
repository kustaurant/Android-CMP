package com.kus.kustaurant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kus.designsystem.theme.KusTheme
import com.kus.domain.auth.session.SessionEvent
import com.kus.domain.auth.session.SessionEventBus
import com.kus.feature.community.navigation.Community
import com.kus.feature.draw.navigation.Draw
import com.kus.feature.home.navigation.Home
import com.kus.feature.login.navigation.Login
import com.kus.feature.my.navigation.My
import com.kus.feature.tier.navigation.Tier
import com.kus.kustaurant.navigation.BottomTab
import com.kus.kustaurant.navigation.KusBottomBar
import com.kus.kustaurant.navigation.KusNavHost
import com.kus.kustaurant.navigation.util.shouldShowBottomBar
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    KusTheme {
        Surface {
            SetNavigation()
        }
    }
}

@Composable
fun SetNavigation() {
    val navController = rememberNavController()
    val durationMillis = 400

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val onShowMessage: (String) -> Unit = remember(snackBarHostState, scope) {
        { message ->
            scope.launch {
                snackBarHostState.showSnackbar(
                    message = message,
                    withDismissAction = false,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    val sessionBus: SessionEventBus = koinInject()
    LaunchedEffect(Unit) {
        sessionBus.events.collect { ev ->
            when (ev) {
                SessionEvent.Expired -> {
                    navController.navigate(Login) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination

    val showBottomBar = shouldShowBottomBar(destination)
    val currentRoute = destination?.route
    val selectedKey = BottomTab.fromRoute(currentRoute).key


    LaunchedEffect(destination) {
        println("hierarchy dest.route = ${destination?.route}")
        println("hierarchy.routes = ${destination?.hierarchy?.map { it.route }}")
    }

    Scaffold(
        bottomBar = {
            SetBottomBar(
                showBottomBar = showBottomBar,
                selectedKey = selectedKey,
                navController = navController,
            )
        },
        snackbarHost = {
            KusSnackBarHost(hostState = snackBarHostState)
        },
        modifier =
            Modifier
            .background(KusTheme.colors.c_FFFFFF)
            .systemBarsPadding(),
        contentWindowInsets = WindowInsets.systemBars,
    ) { padding ->
        KusNavHost(
            navController = navController,
            durationMillis = durationMillis,
            onShowMessage = onShowMessage,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        )
    }
}

@Composable
private fun SetBottomBar(
    showBottomBar: Boolean,
    selectedKey: String,
    navController: NavHostController,
) {
    if (!showBottomBar) return

    KusBottomBar(
        selectedKey = selectedKey,
        onNavigateToTab = { key ->
            navController.navigateToTab(key)
        },
    )
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

@Composable
fun KusSnackBarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
    ) { snackBarData ->
        // TODO Custom Snackbar Composable
    }
}

@Preview
@Composable
private fun AppPreview() {
    App()
}
