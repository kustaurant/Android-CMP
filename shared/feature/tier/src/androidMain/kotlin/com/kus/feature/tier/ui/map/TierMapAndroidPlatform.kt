package com.kus.feature.tier.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.naver.maps.map.MapView

class TierMapAndroidPlatform : TierMapPlatform {
    @Composable
    override fun rememberMapInstance(): Any {
        val context = LocalContext.current
        return remember {
            val mv = MapView(context).apply { onCreate(android.os.Bundle()) }
            val holder = MapHolder(mv)
            mv.getMapAsync { map ->
                map.addOnLoadListener {
                    holder.isLoaded = true
                }
            }
            holder
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
        TierMapAndroidScreen(
            modifier = modifier,
            state = state,
            mapInstance = mapInstance,
            onMapTapped = onMapTapped,
            onRestaurantSelected = onRestaurantSelected,
            onBottomSheetClick = onBottomSheetClick,
            onCameraIdle = onCameraIdle
        )
    }
}
