package com.kus.feature.tier.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


interface TierMapPlatform {
    @Composable
    fun rememberMapInstance(): Any

    @Composable
    fun TierMapScreen(
        modifier: Modifier,
        state: TierMapUiState,
        mapInstance: Any,
        onMapTapped: () -> Unit,
        onRestaurantSelected: (Long) -> Unit,
        onBottomSheetClick: (Long) -> Unit,
        onCameraIdle: (MapCameraState) -> Unit,
    )
}
