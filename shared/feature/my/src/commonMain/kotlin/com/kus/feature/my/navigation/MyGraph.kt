package com.kus.feature.my.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object My

fun NavGraphBuilder.myNavGraph(
    onShowMessage: (String) -> Unit,
) {
    composable<My> {
        MyRoute(
            navigateToProfileEdit = {  },
            navigateToNotice = {  },
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
}
