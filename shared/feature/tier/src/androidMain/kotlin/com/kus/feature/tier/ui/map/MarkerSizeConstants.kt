package com.kus.feature.tier.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

private const val SELECTED_MARKER_SCALE = 1.2f

data class MarkerSizes(
    val defaultWidth: Int,
    val defaultHeight: Int,
    val savedWidth: Int,
    val savedHeight: Int,
    val tierWidth: Int,
    val tierHeight: Int,
    val selectedDefaultWidth: Int,
    val selectedDefaultHeight: Int,
    val selectedSavedWidth: Int,
    val selectedSavedHeight: Int,
    val selectedTierWidth: Int,
    val selectedTierHeight: Int,
)

@Composable
fun rememberMarkerSizes(): MarkerSizes {
    val density = LocalDensity.current

    return remember(density) {
        with(density) {
            val tierWidth = 25.dp.roundToPx()
            val tierHeight = 25.dp.roundToPx()

            val defaultWidth = (tierWidth * (15f / 25f)).toInt()
            val defaultHeight = (tierHeight * (20f / 25f)).toInt()

            val savedWidth = (tierWidth * (18.5f / 25f)).toInt()
            val savedHeight = (tierHeight * (22.5f / 25f)).toInt()

            MarkerSizes(
                defaultWidth = defaultWidth,
                defaultHeight = defaultHeight,
                savedWidth = savedWidth,
                savedHeight = savedHeight,
                tierWidth = tierWidth,
                tierHeight = tierHeight,
                selectedDefaultWidth = (defaultWidth * SELECTED_MARKER_SCALE).toInt(),
                selectedDefaultHeight = (defaultHeight * SELECTED_MARKER_SCALE).toInt(),
                selectedSavedWidth = (savedWidth * SELECTED_MARKER_SCALE).toInt(),
                selectedSavedHeight = (savedHeight * SELECTED_MARKER_SCALE).toInt(),
                selectedTierWidth = (tierWidth * SELECTED_MARKER_SCALE).toInt(),
                selectedTierHeight = (tierHeight * SELECTED_MARKER_SCALE).toInt(),
            )
        }
    }
}