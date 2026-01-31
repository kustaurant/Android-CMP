package com.kus.feature.tier.ui

import android.os.Bundle
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.kus.designsystem.theme.KusTheme
import com.kus.shared.domain.model.tier.TierMapData
import com.kus.shared.domain.model.tier.TierRestaurant
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PolygonOverlay
import com.naver.maps.map.overlay.PolylineOverlay

@Composable
actual fun TierMapScreen(
    modifier: Modifier,
    state: TierMapUiState,
    onMapTapped: () -> Unit,
    onRestaurantSelected: (Long) -> Unit,
    onBottomSheetClick: (Long) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val mapView = remember { MapView(context).apply { onCreate(Bundle()) } }
    var naverMap by remember { mutableStateOf<NaverMap?>(null) }
    val outlineColorInt = KusTheme.colors.c_43AB38.toArgb()

    val polygonOverlays = remember { mutableStateListOf<PolygonOverlay>() }
    val polylineOverlays = remember { mutableStateListOf<PolylineOverlay>() }
    val restaurantMarkers = remember { mutableStateListOf<Marker>() }

    var currentZoom by remember { mutableIntStateOf(11) }
    var lastBounds by remember { mutableStateOf<List<Double>?>(null) }

    DisposableEffect(lifecycleOwner, mapView) {
        val obs = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    LaunchedEffect(mapView) {
        mapView.getMapAsync { map ->
            naverMap = map
            map.uiSettings.isZoomControlEnabled = false

            map.setOnMapClickListener { _, _ ->
                onMapTapped()
            }

            map.addOnCameraChangeListener { _, _ ->
                val newZoom = map.cameraPosition.zoom.toInt()
                if (newZoom != currentZoom) {
                    currentZoom = newZoom
                    val data = (state.map as? UiState.Success)?.data
                    if (data != null) {
                        updateMap(
                            map = map,
                            mapData = data,
                            currentZoom = currentZoom,
                            polygonOverlays = polygonOverlays,
                            polylineOverlays = polylineOverlays,
                            restaurantMarkers = restaurantMarkers,
                            onRestaurantSelected = onRestaurantSelected,
                            outlineColor = outlineColorInt,
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(naverMap, state.map) {
        val map = naverMap ?: return@LaunchedEffect
        val data = (state.map as? UiState.Success)?.data ?: return@LaunchedEffect

        updateMap(
            map = map,
            mapData = data,
            currentZoom = currentZoom,
            polygonOverlays = polygonOverlays,
            polylineOverlays = polylineOverlays,
            restaurantMarkers = restaurantMarkers,
            onRestaurantSelected = onRestaurantSelected,
            outlineColor = outlineColorInt,
        )

        if (lastBounds != data.visibleBounds) {
            lastBounds = data.visibleBounds
            moveCameraToVisibleBounds(map, data.visibleBounds)
        }
    }

    val selectedRestaurant: TierRestaurant? = remember(state.map, state.selectedRestaurantId) {
        val data = (state.map as? UiState.Success)?.data ?: return@remember null
        val id = state.selectedRestaurantId ?: return@remember null

        data.favoriteTierRestaurants.firstOrNull { it.restaurantId == id }
            ?: data.tieredTierRestaurants.firstOrNull { it.restaurantId == id }
            ?: data.nonTieredRestaurants.asSequence()
                .flatMap { it.tierRestaurants.asSequence() }
                .firstOrNull { it.restaurantId == id }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { mapView },
        )

        val visible = state.isShowBottomSheet && selectedRestaurant != null

        AnimatedVisibility(
            visible = visible,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight }
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight }
            ) + fadeOut()
        ) {
            val r = selectedRestaurant ?: return@AnimatedVisibility

            TierRestaurantBottomSheetCard(
                restaurant = r,
                onClick = { onBottomSheetClick(r.restaurantId) }
            )
        }
    }
}

private fun updateMap(
    map: NaverMap,
    mapData: TierMapData,
    currentZoom: Int,
    polygonOverlays: MutableList<PolygonOverlay>,
    polylineOverlays: MutableList<PolylineOverlay>,
    restaurantMarkers: MutableList<Marker>,
    onRestaurantSelected: (Long) -> Unit,
    outlineColor: Int,
) {
    clearOverlaysAndMarkers(polygonOverlays, polylineOverlays, restaurantMarkers)

    mapData.solidPolygonCoordsList.forEach { line ->
        if (line.isNotEmpty()) {
            val coords = line.map { LatLng(it.latitude, it.longitude) }

            val polyline = PolylineOverlay().apply {
                this.coords = coords + coords.first()
                width = 7
                joinType = PolylineOverlay.LineJoin.Round
                color = outlineColor
                this.map = map
            }
            polylineOverlays.add(polyline)

            val polygon = PolygonOverlay().apply {
                this.coords = coords
                color = PolygonColors.POLYGON_SELECTED
                outlineWidth = 0
                this.map = map
            }
            polygonOverlays.add(polygon)
        }
    }

    mapData.dashedPolygonCoordsList.forEach { line ->
        if (line.isNotEmpty()) {
            val coords = line.map { LatLng(it.latitude, it.longitude) }

            val polyline = PolylineOverlay().apply {
                this.coords = coords + coords.first()
                width = 7
                joinType = PolylineOverlay.LineJoin.Round
                color = outlineColor
                this.map = map
            }
            polyline.setPattern(10, 12)
            polylineOverlays.add(polyline)

            val polygon = PolygonOverlay().apply {
                this.coords = coords
                color = PolygonColors.POLYGON_UNSELECTED
                outlineWidth = 0
                this.map = map
            }
            polygonOverlays.add(polygon)
        }
    }

    mapData.favoriteTierRestaurants.forEach { r ->
        createRestaurantMarker(map, r, onRestaurantSelected, restaurantMarkers)
    }

    mapData.tieredTierRestaurants.forEach { r ->
        createRestaurantMarker(map, r, onRestaurantSelected, restaurantMarkers)
    }

    mapData.nonTieredRestaurants
        .filter { it.zoom <= currentZoom }
        .forEach { group ->
            group.tierRestaurants.forEach { r ->
                createRestaurantMarker(map, r, onRestaurantSelected, restaurantMarkers)
            }
        }
}

private fun clearOverlaysAndMarkers(
    polygonOverlays: MutableList<PolygonOverlay>,
    polylineOverlays: MutableList<PolylineOverlay>,
    restaurantMarkers: MutableList<Marker>,
) {
    polygonOverlays.forEach { it.map = null }
    polygonOverlays.clear()
    polylineOverlays.forEach { it.map = null }
    polylineOverlays.clear()
    restaurantMarkers.forEach { it.map = null }
    restaurantMarkers.clear()
}

private fun createRestaurantMarker(
    map: NaverMap,
    restaurant: TierRestaurant,
    onRestaurantSelected: (Long) -> Unit,
    restaurantMarkers: MutableList<Marker>,
) {
    val marker = Marker().apply {
        position = LatLng(restaurant.latitude, restaurant.longitude)
        icon = getMarkerIcon(restaurant)
        this.map = map
        zIndex = if (restaurant.isFavorite) {
            5
        } else {
            when (restaurant.mainTier) {
                1 -> 4
                2 -> 3
                3 -> 2
                4 -> 1
                else -> 0
            }
        }
        setOnClickListener {
            onRestaurantSelected(restaurant.restaurantId)
            true
        }
    }
    restaurantMarkers.add(marker)
}

private fun getMarkerIcon(restaurant: TierRestaurant): OverlayImage {
    return if (restaurant.isFavorite) {
        OverlayImage.fromResource(com.kus.core.designsystem.R.drawable.ic_saved)
    } else {
        if(restaurant.isTempTier) {
            when (restaurant.mainTier) {
                1 -> OverlayImage.fromResource(com.kus.core.designsystem.R.drawable.ic_temp_tier_1)
                2 -> OverlayImage.fromResource(com.kus.core.designsystem.R.drawable.ic_temp_tier_2)
                3 -> OverlayImage.fromResource(com.kus.core.designsystem.R.drawable.ic_temp_tier_3)
                4 -> OverlayImage.fromResource(com.kus.core.designsystem.R.drawable.ic_temp_tier_4)
                else -> OverlayImage.fromResource(com.kus.feature.tier.R.drawable.ic_map_marker)
            }
        } else {
            when (restaurant.mainTier) {
                1 -> OverlayImage.fromResource(com.kus.core.designsystem.R.drawable.ic_tier_1)
                2 -> OverlayImage.fromResource(com.kus.core.designsystem.R.drawable.ic_tier_2)
                3 -> OverlayImage.fromResource(com.kus.core.designsystem.R.drawable.ic_tier_3)
                4 -> OverlayImage.fromResource(com.kus.core.designsystem.R.drawable.ic_tier_4)
                else -> OverlayImage.fromResource(com.kus.feature.tier.R.drawable.ic_map_marker)
            }
        }
    }
}

private fun moveCameraToVisibleBounds(map: NaverMap, visibleBounds: List<Double>) {
    if (visibleBounds.size < 4) return
    val bounds = LatLngBounds(
        LatLng(visibleBounds[2], visibleBounds[0]),
        LatLng(visibleBounds[3], visibleBounds[1])
    )
    map.moveCamera(CameraUpdate.fitBounds(bounds, 50))
}

object PolygonColors {
    val POLYGON_SELECTED = android.graphics.Color.argb(59, 67, 171, 56)
    val POLYGON_UNSELECTED = android.graphics.Color.argb(31, 67, 171, 56)
}
