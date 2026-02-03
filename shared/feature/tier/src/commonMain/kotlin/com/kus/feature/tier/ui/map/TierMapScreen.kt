package com.kus.feature.tier.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun TierMapScreen(
    modifier: Modifier = Modifier,
    state: TierMapUiState,
    mapInstance: Any,
    onMapTapped: () -> Unit,
    onRestaurantSelected: (restaurantId: Long) -> Unit,
    onBottomSheetClick: (restaurantId: Long) -> Unit,
    onCameraIdle: (MapCameraState) -> Unit,
)