package com.kus.feature.my.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kus.feature.my.ui.feedback.FeedbackScreen
import com.kus.feature.my.ui.webview.MyPageWebViewScreen
import kotlinx.serialization.Serializable

fun NavController.navigateToMyWebView(
    title: String,
    url: String,
    navOptions: NavOptions? = null,
) = navigate(MyWebView(title, url), navOptions)

fun NavController.navigateToFeedback(
    navOptions: NavOptions? = null,
) = navigate(Feedback, navOptions)

fun NavGraphBuilder.myNavGraph(
    navigateToUp: () -> Unit,
    onShowMessage: (String) -> Unit,
    navController: NavController,
) {
    composable<My> {
        MyRoute(
            navigateToProfileEdit = { },
            navigateToNotice = {
                navController.navigateToMyWebView(
                    title = "공지사항",
                    url = "https://kustaurant.com/notice",
                )
            },
            navigateToTerms = {
                navController.navigateToMyWebView(
                    title = "공지사항",
                    url = "https://kustaurant.com/terms_of_use",
                )
            },
            navigateToPrivacyPolicy = {
                navController.navigateToMyWebView(
                    title = "공지사항",
                    url = "https://kustaurant.com/privacy-policy",
                )
            },
            navigateToFeedback = navController::navigateToFeedback,
            navigateToSavedRest = { },
            navigateToCheckedRest = { },
            navigateToMyArticle = { },
            navigateToMyComment = { },
            navigateToScrap = { },
        )
    }

    composable<MyWebView> { backStackEntry ->
        val args = backStackEntry.toRoute<MyWebView>()
        MyPageWebViewScreen(
            title = args.title,
            url = args.url,
            onBackClick = navigateToUp,
        )
    }

    composable<Feedback> {
        FeedbackScreen(
            onBackClick = navigateToUp,
        )
    }
}

@Serializable
data object My

@Serializable
data class MyWebView(
    val title: String,
    val url: String,
)

@Serializable
data object Feedback