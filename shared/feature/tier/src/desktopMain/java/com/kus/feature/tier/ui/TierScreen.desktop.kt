package com.kus.feature.tier.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kus.feature.tier.ui.map.MapCameraState
import com.kus.feature.tier.ui.map.TierMapUiState

@Composable
actual fun TierMapScreen(
    modifier: Modifier,
    state: TierMapUiState,
    onMapTapped: () -> Unit,
    onRestaurantSelected: (restaurantId: Long) -> Unit,
    onBottomSheetClick: (restaurantId: Long) -> Unit,
    onCameraIdle: (MapCameraState) -> Unit,
) {
}