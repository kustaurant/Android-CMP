package com.kus.feature.tier.ui.map

import android.graphics.Color
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.kus.core.designsystem.R
import com.kus.designsystem.theme.KusTheme
import com.kus.shared.domain.model.tier.TierMapData
import com.kus.shared.domain.model.tier.TierRestaurant
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PolygonOverlay
import com.naver.maps.map.overlay.PolylineOverlay

@Composable
fun TierMapAndroidScreen(
    modifier: Modifier,
    state: TierMapUiState,
    mapInstance: Any,
    onMapTapped: () -> Unit,
    onRestaurantSelected: (Long) -> Unit,
    onBottomSheetClick: (Long) -> Unit,
    onCameraIdle: (MapCameraState) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val density = LocalDensity.current

    val mapView = (mapInstance as MapHolder).mapView
    var naverMap by remember { mutableStateOf<NaverMap?>(null) }

    var isMapLoaded by remember { mutableStateOf(mapInstance.isLoaded) }
    var isMapReadyToShow by remember { mutableStateOf(false) }

    val outlineColorInt = KusTheme.colors.c_43AB38.toArgb()

    var currentZoom by remember {
        mutableIntStateOf(mapInstance.lastCameraPosition?.zoom?.toInt() ?: 11)
    }

    val latestState by rememberUpdatedState(state)
    val latestOnRestaurantSelected by rememberUpdatedState(onRestaurantSelected)
    val latestOnMapTapped by rememberUpdatedState(onMapTapped)
    val mapAlpha by animateFloatAsState(if (isMapReadyToShow) 1f else 0f)


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

            map.setOnMapClickListener { _, _ -> latestOnMapTapped() }

            if (mapInstance.isLoaded) {
                isMapLoaded = true
            }

            map.addOnLoadListener {
                isMapLoaded = true
                mapInstance.isLoaded = true
            }

            map.addOnCameraIdleListener {
                val pos = map.cameraPosition
                currentZoom = pos.zoom.toInt()

                mapInstance.lastCameraPosition = pos

                onCameraIdle(
                    MapCameraState(
                        latitude = pos.target.latitude,
                        longitude = pos.target.longitude,
                        zoom = pos.zoom,
                        tilt = pos.tilt,
                        bearing = pos.bearing
                    )
                )

                val data = (latestState.map as? UiState.Success)?.data ?: return@addOnCameraIdleListener
                updateMap(
                    map = map,
                    mapData = data,
                    currentZoom = currentZoom,
                    polygonOverlays = mapInstance.polygonOverlays,
                    polylineOverlays = mapInstance.polylineOverlays,
                    restaurantMarkers = mapInstance.restaurantMarkers,
                    onRestaurantSelected = latestOnRestaurantSelected,
                    outlineColor = outlineColorInt,
                )
            }
        }
    }

    LaunchedEffect(naverMap, isMapLoaded, state.map) {
        val map = naverMap ?: return@LaunchedEffect
        if (!isMapLoaded) return@LaunchedEffect
        val data = (state.map as? UiState.Success)?.data ?: return@LaunchedEffect

        map.minZoom = data.minZoom.toDouble()

        val isBoundsChanged = mapInstance.lastBounds != data.visibleBounds

        if (data.visibleBounds.size < 4) {
            isMapReadyToShow = true
            return@LaunchedEffect
        }

        updateMap(
            map = map,
            mapData = data,
            currentZoom = map.cameraPosition.zoom.toInt(),
            polygonOverlays = mapInstance.polygonOverlays,
            polylineOverlays = mapInstance.polylineOverlays,
            restaurantMarkers = mapInstance.restaurantMarkers,
            onRestaurantSelected = latestOnRestaurantSelected,
            outlineColor = outlineColorInt,
        )

        if (isBoundsChanged) {
            mapInstance.lastBounds = data.visibleBounds
            isMapReadyToShow = false

            mapView.post {
                moveCameraToBoundsAndClampZoomOnce(
                    map = map,
                    visibleBounds = data.visibleBounds,
                    paddingPx = with(density) { 16.dp.roundToPx() },
                    minZoom = data.minZoom.toDouble()
                )

                var done = false
                val oneShotIdle = object : NaverMap.OnCameraIdleListener {
                    override fun onCameraIdle() {
                        if (done) return
                        done = true
                        map.removeOnCameraIdleListener(this)
                        isMapReadyToShow = true
                    }
                }
                map.addOnCameraIdleListener(oneShotIdle)

                mapView.postDelayed({
                    if (!done) {
                        map.removeOnCameraIdleListener(oneShotIdle)
                        isMapReadyToShow = true
                    }
                }, 700)
            }
        } else if (!isMapReadyToShow) {
            isMapReadyToShow = true
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
            modifier = Modifier
                .fillMaxSize()
                .alpha(mapAlpha),
            factory = { mapView },
        )

        if (!isMapReadyToShow) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        val visible = state.isShowBottomSheet && selectedRestaurant != null

        AnimatedVisibility(
            visible = visible,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            val r = selectedRestaurant ?: return@AnimatedVisibility
            TierRestaurantBottomSheetCard(
                restaurant = r,
                onClick = { onBottomSheetClick(r.restaurantId) }
            )
        }
    }
}

private fun moveCameraToBoundsAndClampZoomOnce(
    map: NaverMap,
    visibleBounds: List<Double>,
    paddingPx: Int,
    minZoom: Double,
) {
    if (visibleBounds.size < 4) return

    val bounds = LatLngBounds(
        LatLng(visibleBounds[2], visibleBounds[0]),
        LatLng(visibleBounds[3], visibleBounds[1])
    )

    map.moveCamera(CameraUpdate.fitBounds(bounds, paddingPx))

    val z = map.cameraPosition.zoom
    if (z < minZoom) {
        map.moveCamera(CameraUpdate.scrollAndZoomTo(bounds.center, minZoom))
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
        OverlayImage.fromResource(R.drawable.ic_saved)
    } else if(restaurant.partnershipInfo.isNotEmpty()) {
        OverlayImage.fromResource(com.kus.feature.tier.R.drawable.ic_marker_partnership)
    } else
     {
        if(restaurant.isTempTier) {
            when (restaurant.mainTier) {
                1 -> OverlayImage.fromResource(R.drawable.ic_temp_tier_1)
                2 -> OverlayImage.fromResource(R.drawable.ic_temp_tier_2)
                3 -> OverlayImage.fromResource(R.drawable.ic_temp_tier_3)
                4 -> OverlayImage.fromResource(R.drawable.ic_temp_tier_4)
                else -> OverlayImage.fromResource(com.kus.feature.tier.R.drawable.ic_marker_none)
            }
        } else {
            when (restaurant.mainTier) {
                1 -> OverlayImage.fromResource(R.drawable.ic_tier_1)
                2 -> OverlayImage.fromResource(R.drawable.ic_tier_2)
                3 -> OverlayImage.fromResource(R.drawable.ic_tier_3)
                4 -> OverlayImage.fromResource(R.drawable.ic_tier_4)
                else -> OverlayImage.fromResource(com.kus.feature.tier.R.drawable.ic_marker_none)
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
    val POLYGON_SELECTED = Color.argb(59, 67, 171, 56)
    val POLYGON_UNSELECTED = Color.argb(31, 67, 171, 56)
}
