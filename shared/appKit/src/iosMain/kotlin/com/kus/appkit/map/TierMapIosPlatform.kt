@file:OptIn(ExperimentalForeignApi::class)
package com.kus.appkit.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.kus.feature.tier.ui.map.*
import cocoapods.NMapsMap.NMFNaverMapView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake

class TierMapIosPlatform : TierMapPlatform {

    @Composable
    override fun rememberMapInstance(): Any {
        return remember {
            val mapView = NMFNaverMapView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0))
            MapHolder(mapView)
        }
    }

    @Composable
    override fun TierMapScreen(
        modifier: Modifier,
        state: TierMapUiState,
        mapInstance: Any,
        onMapTapped: () -> Unit,
        onRestaurantSelected: (Long) -> Unit,
        onBottomSheetClick: (Long) -> Unit,
        onCameraIdle: (MapCameraState) -> Unit,
    ) {
        TierMapIosScreen(
            modifier = modifier,
            state = state,
            mapInstance = mapInstance,
            onMapTapped = onMapTapped,
            onRestaurantSelected = onRestaurantSelected,
            onBottomSheetClick = onBottomSheetClick,
            onCameraIdle = onCameraIdle,
        )
    }
}
