import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Home

fun NavGraphBuilder.homeNavGraph(
    navigateToTier: () -> Unit,
    navigateToEvaluate: () -> Unit,
) {
    composable<Home> {
        HomeRoute(
            navigateToTier = navigateToTier,
            navigateToEvaluate = navigateToEvaluate,
        )
    }
}