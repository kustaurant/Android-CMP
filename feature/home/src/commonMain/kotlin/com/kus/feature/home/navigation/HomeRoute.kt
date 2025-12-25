import androidx.compose.runtime.Composable
import com.kus.feature.home.ui.HomeScreen

@Composable
fun HomeRoute(
    navigateToTier: () -> Unit,
    navigateToEvaluate: () -> Unit,
) {
    HomeScreen()
}