package com.kus.kustaurant

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kus.designsystem.component.LoginRequiredOverlay
import com.kus.designsystem.component.snackbar.KusSnackBarOverlay
import com.kus.designsystem.component.snackbar.LocalSnackBarBottomPadding
import com.kus.designsystem.theme.KusTheme
import com.kus.domain.auth.session.SessionEvent
import com.kus.domain.auth.session.SessionEventBus
import com.kus.feature.community.navigation.CommunityDetail
import com.kus.feature.community.navigation.CommunityWrite
import com.kus.feature.community.navigation.CommunityWriteModify
import com.kus.feature.login.navigation.Login
import com.kus.kustaurant.navigation.KusNavHost
import com.kus.kustaurant.navigation.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    KusTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = KusTheme.colors.c_FFFFFF,
        ) {
            SetNavigation()
        }
    }
}

@Composable
fun SetNavigation() {
    val navController = rememberNavController()
    val durationMillis = 400
    val snackDuration = 2800L

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val sessionBus: SessionEventBus = koinInject()
    var showRequireLoginPopup by remember { mutableStateOf<Boolean>(false) }

    val onShowMessage: (String) -> Unit = remember(snackBarHostState, scope) {
        { message ->
            scope.launch {
                snackBarHostState.currentSnackbarData?.dismiss()
                val dismissJob = launch {
                    delay(snackDuration)
                    val current = snackBarHostState.currentSnackbarData
                    if (current?.visuals?.message == message) {
                        current.dismiss()
                    }
                }

                snackBarHostState.showSnackbar(
                    message = message,
                    withDismissAction = false,
                    duration = SnackbarDuration.Indefinite
                )

                dismissJob.cancel()
            }
        }
    }

    LaunchedEffect(Unit) {
        sessionBus.events.collect { ev ->
            when (ev) {
                SessionEvent.Expired -> {
                    navController.navigate(Login) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }

                SessionEvent.LoginRequired -> {
                    showRequireLoginPopup = true
                }
            }
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination
    val isMain = destination?.hasRoute<Main>() == true
    val isWriter =
        destination?.hasRoute<CommunityWrite>() == true ||
            destination?.hasRoute<CommunityDetail>() == true ||
            destination?.hasRoute<CommunityWriteModify>() == true

    val snackBarBottomPadding = when {
        isMain -> 72.dp
        isWriter -> 52.dp
        else -> 16.dp
    }

    CompositionLocalProvider(
        LocalSnackBarBottomPadding provides snackBarBottomPadding
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            KusNavHost(
                navController = navController,
                durationMillis = durationMillis,
                onShowMessage = onShowMessage,
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding(),
            )

            KusSnackBarOverlay(
                hostState = snackBarHostState
            )

            if (showRequireLoginPopup) {
                LoginRequiredOverlay(
                    onLoginButtonClick = {
                        showRequireLoginPopup = false
                        navController.navigate(Login) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onDismissRequest = { showRequireLoginPopup = false },
                )
            }
        }
    }
}

@Preview
@Composable
private fun AppPreview() {
    App()
}
