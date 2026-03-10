package com.kus.feature.evaluate.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kus.feature.evaluate.model.EvaluateRestaurant
import com.kus.feature.evaluate.ui.EvaluateRoute
import kotlinx.serialization.Serializable

@Serializable
data class Evaluate(
    val restaurantId: Long,
    val restaurantName: String,
    val mainTier: Int,
    val restaurantCuisine: String,
    val restaurantCuisineImgUrl: String,
    val restaurantPosition: String,
    val restaurantAddress: String,
    val situationList: List<String>,
    val partnershipInfo: String,
)

fun NavGraphBuilder.evaluateNavGraph(
    onBackClick: () -> Unit,
) {
    composable<Evaluate> { backStackEntry ->
        val evaluate = backStackEntry.toRoute<Evaluate>()
        EvaluateRoute(
            restaurantId = evaluate.restaurantId,
            restaurant = EvaluateRestaurant(
                restaurantId = evaluate.restaurantId,
                restaurantName = evaluate.restaurantName,
                mainTier = evaluate.mainTier,
                restaurantCuisine = evaluate.restaurantCuisine,
                restaurantCuisineImgUrl = evaluate.restaurantCuisineImgUrl,
                restaurantPosition = evaluate.restaurantPosition,
                restaurantAddress = evaluate.restaurantAddress,
                situationList = ArrayList(evaluate.situationList),
                partnershipInfo = evaluate.partnershipInfo,
            ),
            onBackClick = onBackClick,
        )
    }
}
