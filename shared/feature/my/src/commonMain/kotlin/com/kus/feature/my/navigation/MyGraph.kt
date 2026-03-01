package com.kus.feature.my.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kus.feature.my.ui.notice.NoticeScreen
import kotlinx.serialization.Serializable

fun NavController.navigateToNotice(
    navOptions: NavOptions? = null,
) = navigate(Notice, navOptions)

fun NavGraphBuilder.myNavGraph(
    onShowMessage: (String) -> Unit,
    navController: NavController,
) {
    composable<My> {
        MyRoute(
            navigateToProfileEdit = {  },
            navigateToNotice = navController::navigateToNotice,
            navigateToTerms = {  },
            navigateToPrivacyPolicy = {  },
            navigateToFeedback = {  },
            navigateToSavedRest = {  },
            navigateToCheckedRest = {  },
            navigateToMyArticle = {  },
            navigateToMyComment = {  },
            navigateToScrap = {  },
        )
    }

    composable<Notice> {
        NoticeScreen(
            onBackClick = { },
        )
    }
}

@Serializable
data object My

@Serializable
data object Notice