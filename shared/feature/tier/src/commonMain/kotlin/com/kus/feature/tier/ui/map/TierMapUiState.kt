package com.kus.feature.tier.ui.map

import com.kus.feature.tier.ui.UiState
import com.kus.shared.domain.model.tier.TierMapData

data class TierMapUiState(
    val map: UiState<TierMapData> = UiState.Loading,
    val isShowBottomSheet: Boolean = false,
    val selectedRestaurantId: Long? = null,
    val cameraState: MapCameraState? = null,
)