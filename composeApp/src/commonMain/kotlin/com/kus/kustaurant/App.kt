package com.kus.kustaurant

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kus.feature.community.navigation.Community
import com.kus.feature.draw.navigation.Draw
import com.kus.feature.home.navigation.Home
import com.kus.feature.my.navigation.My
import com.kus.feature.tier.navigation.Tier
import com.kus.kustaurant.navigation.BottomTab
import com.kus.kustaurant.navigation.KusBottomBar
import com.kus.kustaurant.navigation.KusNavHost
import com.kus.kustaurant.theme.KusTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

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

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = shouldShowBottomBar(currentRoute)
    val selectedKey = BottomTab.fromRoute(currentRoute).key

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
        modifier = Modifier.systemBarsPadding(),
    ) { padding ->
        KusNavHost(
            navController = navController,
            durationMillis = durationMillis,
            onShowMessage = onShowMessage,
            modifier = Modifier.padding(padding),
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

private fun shouldShowBottomBar(currentRoute: String?): Boolean = when {
    currentRoute?.contains("Splash") == true -> false
    currentRoute?.contains("Onboarding") == true -> false
    currentRoute?.contains("Login") == true -> false
    else -> true
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
