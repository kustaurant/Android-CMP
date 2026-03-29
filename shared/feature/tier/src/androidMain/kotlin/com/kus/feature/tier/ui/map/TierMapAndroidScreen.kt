package com.kus.feature.tier.ui.map

import UiState
import android.graphics.Color
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
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
import com.kus.designsystem.component.KusLoadingAnimation
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.tier.R
import com.kus.shared.domain.model.tier.TierMapData
import com.kus.shared.domain.model.tier.TierRestaurant
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PolygonOverlay
import com.naver.maps.map.overlay.PolylineOverlay
import com.kus.core.designsystem.R as CoreRes

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

    val mapHolder = mapInstance as MapHolder
    val mapView = mapHolder.mapView

    var naverMap by remember { mutableStateOf<NaverMap?>(null) }
    var isMapLoaded by remember { mutableStateOf(mapHolder.isLoaded) }
    var isMapReadyToShow by remember { mutableStateOf(false) }
    var currentZoom by remember { mutableIntStateOf(state.cameraState?.zoom?.toInt() ?: 11) }
    var suppressCameraIdle by remember { mutableStateOf(false) }

    val outlineColorInt = KusTheme.colors.c_43AB38.toArgb()
    val latestState by rememberUpdatedState(state)
    val latestOnRestaurantSelected by rememberUpdatedState(onRestaurantSelected)
    val latestOnMapTapped by rememberUpdatedState(onMapTapped)
    val latestSelectedRestaurantId by rememberUpdatedState(state.selectedRestaurantId)
    val mapAlpha by animateFloatAsState(if (isMapReadyToShow) 1f else 0f)
    val tierMarkerSize = with(LocalDensity.current) { 25.dp.toPx().toInt() }

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
                latestOnMapTapped()
            }

            if (mapHolder.isLoaded) {
                isMapLoaded = true
            }

            map.addOnLoadListener {
                isMapLoaded = true
                mapHolder.isLoaded = true
            }

            map.addOnCameraIdleListener {
                val pos = map.cameraPosition
                currentZoom = pos.zoom.toInt()

                if (!suppressCameraIdle && mapHolder.hasAppliedInitialCamera) {
                    onCameraIdle(
                        MapCameraState(
                            latitude = pos.target.latitude,
                            longitude = pos.target.longitude,
                            zoom = pos.zoom,
                            tilt = pos.tilt,
                            bearing = pos.bearing
                        )
                    )
                }
            }
        }
    }

    LaunchedEffect(state.map) {
        if (state.map is UiState.Loading) {
            mapHolder.hasAppliedInitialCamera = false
        }
    }

    LaunchedEffect(naverMap, isMapLoaded, state.map, currentZoom) {
        val map = naverMap ?: return@LaunchedEffect
        if (!isMapLoaded) return@LaunchedEffect
        val data = (state.map as? UiState.Success)?.data ?: return@LaunchedEffect

        map.minZoom = data.minZoom.toDouble()

        updateMap(
            map = map,
            mapData = data,
            currentZoom = currentZoom,
            mapHolder = mapHolder,
            selectedRestaurantId = latestSelectedRestaurantId,
            onRestaurantSelected = latestOnRestaurantSelected,
            outlineColor = outlineColorInt,
            tierMarkerSize = tierMarkerSize,
        )

        isMapReadyToShow = true
    }

    LaunchedEffect(state.selectedRestaurantId, state.map) {
        if (state.map !is UiState.Success) return@LaunchedEffect
        if (mapHolder.restaurantMarkers.isEmpty()) return@LaunchedEffect

        updateSelectedMarkerOnly(
            mapHolder = mapHolder,
            selectedRestaurantId = state.selectedRestaurantId,
            tierMarkerSize = tierMarkerSize,
        )
    }

    LaunchedEffect(naverMap, isMapLoaded, state.map) {
        val map = naverMap ?: return@LaunchedEffect
        if (!isMapLoaded) return@LaunchedEffect
        val data = (state.map as? UiState.Success)?.data ?: return@LaunchedEffect

        map.minZoom = data.minZoom.toDouble()

        if (data.visibleBounds.size < 4) {
            isMapReadyToShow = true
            return@LaunchedEffect
        }

        isMapReadyToShow = false

        mapView.post {
            suppressCameraIdle = true

            val cameraState = latestState.cameraState
            if (cameraState != null) {
                map.moveCamera(
                    CameraUpdate.toCameraPosition(
                        CameraPosition(
                            LatLng(cameraState.latitude, cameraState.longitude),
                            cameraState.zoom,
                            cameraState.tilt,
                            cameraState.bearing
                        )
                    )
                )
                currentZoom = cameraState.zoom.toInt()
            } else {
                moveCameraToBoundsAndClampZoomOnce(
                    map = map,
                    visibleBounds = data.visibleBounds,
                    paddingPx = with(density) { 16.dp.roundToPx() },
                    minZoom = data.minZoom.toDouble()
                )
                currentZoom = map.cameraPosition.zoom.toInt()
            }

            mapView.post {
                mapHolder.hasAppliedInitialCamera = true
                suppressCameraIdle = false
                isMapReadyToShow = true
            }
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
                KusLoadingAnimation()
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
    mapHolder: MapHolder,
    selectedRestaurantId: Long?,
    onRestaurantSelected: (Long) -> Unit,
    outlineColor: Int,
    tierMarkerSize: Int,
) {
    clearOverlaysAndMarkers(
        polygonOverlays = mapHolder.polygonOverlays,
        polylineOverlays = mapHolder.polylineOverlays,
        restaurantMarkers = mapHolder.restaurantMarkers,
    )

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
            mapHolder.polylineOverlays.add(polyline)

            val polygon = PolygonOverlay().apply {
                this.coords = coords
                color = PolygonColors.POLYGON_SELECTED
                outlineWidth = 0
                this.map = map
            }
            mapHolder.polygonOverlays.add(polygon)
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
            mapHolder.polylineOverlays.add(polyline)

            val polygon = PolygonOverlay().apply {
                this.coords = coords
                color = PolygonColors.POLYGON_UNSELECTED
                outlineWidth = 0
                this.map = map
            }
            mapHolder.polygonOverlays.add(polygon)
        }
    }

    fun addMarker(r: TierRestaurant) {
        createRestaurantMarker(
            map = map,
            restaurant = r,
            selectedRestaurantId = selectedRestaurantId,
            onRestaurantSelected = onRestaurantSelected,
            restaurantMarkers = mapHolder.restaurantMarkers,
            tierMarkerSize = tierMarkerSize,
        )
    }

    mapData.favoriteTierRestaurants.forEach(::addMarker)
    mapData.tieredTierRestaurants.forEach(::addMarker)

    mapData.nonTieredRestaurants
        .filter { it.zoom <= currentZoom }
        .forEach { group ->
            group.tierRestaurants.forEach(::addMarker)
        }

    mapHolder.selectedMarker = mapHolder.restaurantMarkers
        .firstOrNull { (it.tag as? TierRestaurant)?.restaurantId == selectedRestaurantId }
}

private fun updateSelectedMarkerOnly(
    mapHolder: MapHolder,
    selectedRestaurantId: Long?,
    tierMarkerSize: Int,
) {
    val prev = mapHolder.selectedMarker
    val prevRestaurant = prev?.tag as? TierRestaurant

    if (prev != null && prevRestaurant != null) {
        prev.icon = getMarkerIcon(prevRestaurant)
        prev.zIndex = if (prevRestaurant.isFavorite) {
            5
        } else {
            when (prevRestaurant.mainTier) {
                1 -> 4
                2 -> 3
                3 -> 2
                4 -> 1
                else -> 0
            }
        }

        if (!prevRestaurant.isFavorite && prevRestaurant.mainTier in 1..4) {
            prev.width = tierMarkerSize
            prev.height = tierMarkerSize
        }
    }

    val next = mapHolder.restaurantMarkers
        .firstOrNull { (it.tag as? TierRestaurant)?.restaurantId == selectedRestaurantId }

    val nextRestaurant = next?.tag as? TierRestaurant
    if (next != null && nextRestaurant != null) {
        next.icon = getSelectedMarkerIcon(nextRestaurant)
        next.zIndex = 10

        if (!nextRestaurant.isFavorite && nextRestaurant.mainTier in 1..4) {
            val selectedSize = (tierMarkerSize * 1.2f).toInt()
            next.width = selectedSize
            next.height = selectedSize
        }
    }

    mapHolder.selectedMarker = next
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
    selectedRestaurantId: Long?,
    onRestaurantSelected: (Long) -> Unit,
    restaurantMarkers: MutableList<Marker>,
    tierMarkerSize: Int,
) {
    val isSelected = selectedRestaurantId == restaurant.restaurantId

    val marker = Marker().apply {
        position = LatLng(restaurant.latitude, restaurant.longitude)

        icon = if (isSelected) {
            getSelectedMarkerIcon(restaurant)
        } else {
            getMarkerIcon(restaurant)
        }

        if (!restaurant.isFavorite && restaurant.mainTier in 1..4) {
            width = tierMarkerSize
            height = tierMarkerSize
        }

        this.map = map

        zIndex = if (isSelected) {
            10
        } else if (restaurant.isFavorite) {
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

        tag = restaurant

        setOnClickListener {
            onRestaurantSelected(restaurant.restaurantId)
            true
        }
    }

    restaurantMarkers.add(marker)
}

private fun getSelectedMarkerIcon(restaurant: TierRestaurant): OverlayImage {
    return if (restaurant.isFavorite) {
        OverlayImage.fromResource(CoreRes.drawable.ic_saved)
    } else if (restaurant.partnershipInfo.isNotEmpty()) {
        OverlayImage.fromResource(R.drawable.ic_tier_partnership_selected)
    } else {
        if (restaurant.isTempTier) {
            when (restaurant.mainTier) {
                1 -> OverlayImage.fromResource(R.drawable.ic_temp_tier_1_selected)
                2 -> OverlayImage.fromResource(R.drawable.ic_temp_tier_2_selected)
                3 -> OverlayImage.fromResource(R.drawable.ic_temp_tier_3_selected)
                4 -> OverlayImage.fromResource(R.drawable.ic_temp_tier_4_selected)
                else -> OverlayImage.fromResource(R.drawable.ic_tier_none_selected)
            }
        } else {
            when (restaurant.mainTier) {
                1 -> OverlayImage.fromResource(R.drawable.ic_tier_1_selected)
                2 -> OverlayImage.fromResource(R.drawable.ic_tier_2_selected)
                3 -> OverlayImage.fromResource(R.drawable.ic_tier_3_selected)
                4 -> OverlayImage.fromResource(R.drawable.ic_tier_4_selected)
                else -> OverlayImage.fromResource(R.drawable.ic_tier_none_selected)
            }
        }
    }
}

private fun getMarkerIcon(restaurant: TierRestaurant): OverlayImage {
    return if (restaurant.isFavorite) {
        OverlayImage.fromResource(CoreRes.drawable.ic_saved)
    } else if (restaurant.partnershipInfo.isNotEmpty()) {
        OverlayImage.fromResource(com.kus.feature.tier.R.drawable.ic_marker_partnership)
    } else {
        if (restaurant.isTempTier) {
            when (restaurant.mainTier) {
                1 -> OverlayImage.fromResource(CoreRes.drawable.ic_temp_tier_1)
                2 -> OverlayImage.fromResource(CoreRes.drawable.ic_temp_tier_2)
                3 -> OverlayImage.fromResource(CoreRes.drawable.ic_temp_tier_3)
                4 -> OverlayImage.fromResource(CoreRes.drawable.ic_temp_tier_4)
                else -> OverlayImage.fromResource(com.kus.feature.tier.R.drawable.ic_marker_none)
            }
        } else {
            when (restaurant.mainTier) {
                1 -> OverlayImage.fromResource(CoreRes.drawable.ic_tier_1)
                2 -> OverlayImage.fromResource(CoreRes.drawable.ic_tier_2)
                3 -> OverlayImage.fromResource(CoreRes.drawable.ic_tier_3)
                4 -> OverlayImage.fromResource(CoreRes.drawable.ic_tier_4)
                else -> OverlayImage.fromResource(com.kus.feature.tier.R.drawable.ic_marker_none)
            }
        }
    }
}

object PolygonColors {
    val POLYGON_SELECTED = Color.argb(59, 67, 171, 56)
    val POLYGON_UNSELECTED = Color.argb(31, 67, 171, 56)
}
