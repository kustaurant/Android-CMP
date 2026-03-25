package com.kus.feature.draw.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kus.core.serialization.KusJson
import com.kus.core.serialization.RouteCodec
import com.kus.feature.draw.model.DrawResultPayload
import com.kus.feature.draw.ui.result.DrawResultScreen
import com.kus.feature.draw.ui.select.DrawSelectScreen
import kotlinx.serialization.Serializable

@Serializable
data object Draw

@Serializable
data class DrawResult(
    val encoded: String,
)

fun NavGraphBuilder.drawNavGraph(
    navigateToDrawResult: (DrawResult) -> Unit,
    onBackClick: () -> Unit,
) {
    composable<Draw> {
        DrawSelectScreen(
            onBackClick = onBackClick,
            onApplyClick = { locations, cuisines ->
                val payload = DrawResultPayload(
                    locations = locations.toList(),
                    cuisines = cuisines.toList(),
                )

                val payloadJson = KusJson.json.encodeToString(
                    DrawResultPayload.serializer(),
                    payload
                )

                val encoded = RouteCodec.encode(payloadJson)

                navigateToDrawResult(DrawResult(encoded))
            }
        )
    }

    composable<DrawResult> { backStackEntry ->
        val args = backStackEntry.toRoute<DrawResult>()

        val payloadJson = RouteCodec.decode(args.encoded)

        val payload = KusJson.json.decodeFromString<DrawResultPayload>(payloadJson)

        DrawResultScreen(
            initialLocations = payload.locations.toSet(),
            initialCuisines = payload.cuisines.toSet(),
            onBackClick = onBackClick,
        )
    }
}