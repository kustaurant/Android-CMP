package com.kus.kustaurant

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
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
    val bottomBarState = rememberSaveable { mutableStateOf(false) }
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

    Scaffold(
        bottomBar = {
            if (bottomBarState.value) {
                // TODO Custom Bottom Bar Composable
            }
        },
        snackbarHost = {
            KusSnackBarHost(
                hostState = snackBarHostState
            )
        },
    ) { padding ->
        KusNavHost(
            navController = navController,
            durationMillis = durationMillis,
            bottomBarState = bottomBarState,
            onShowMessage = onShowMessage,
            modifier = Modifier.padding(padding),
        )
    }
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