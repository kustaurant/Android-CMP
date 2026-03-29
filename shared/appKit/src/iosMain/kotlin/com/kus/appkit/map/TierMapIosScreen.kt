@file:OptIn(ExperimentalForeignApi::class)

package com.kus.appkit.map

import UiState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import cocoapods.NMapsMap.NMFCameraPosition
import cocoapods.NMapsMap.NMFCameraUpdate
import cocoapods.NMapsMap.NMFMapView
import cocoapods.NMapsMap.NMFMapViewCameraDelegateProtocol
import cocoapods.NMapsMap.NMFMapViewTouchDelegateProtocol
import cocoapods.NMapsMap.NMFMarker
import cocoapods.NMapsMap.NMFOverlayImage
import cocoapods.NMapsMap.NMFPolygonOverlay
import cocoapods.NMapsMap.NMFPolylineOverlay
import cocoapods.NMapsMap.NMGLatLng
import com.kus.designsystem.component.KusLoadingAnimation
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.toUIColor
import com.kus.feature.tier.ui.map.MapCameraState
import com.kus.feature.tier.ui.map.TierMapUiState
import com.kus.feature.tier.ui.map.TierRestaurantBottomSheetCard
import com.kus.shared.domain.model.tier.TierMapData
import com.kus.shared.domain.model.tier.TierRestaurant
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGPoint
import platform.UIKit.UIColor
import platform.darwin.NSObject

@Composable
fun TierMapIosScreen(
    modifier: Modifier,
    state: TierMapUiState,
    mapInstance: Any,
    onMapTapped: () -> Unit,
    onRestaurantSelected: (Long) -> Unit,
    onBottomSheetClick: (restaurantId: Long) -> Unit,
    onCameraIdle: (MapCameraState) -> Unit,
) {
    val holder = mapInstance as MapHolder
    val naverMapView = holder.mapView
    val mapView = naverMapView.mapView()

    val isCameraReady = remember { mutableStateOf(false) }

    val outlineUiColor = KusTheme.colors.c_43AB38.toUIColor()
    val currentZoomState = remember {
        mutableIntStateOf((holder.lastCameraState?.zoom ?: 11.0).toInt())
    }
    val tierMarkerSize = 25.0

    val latestOnMapTapped by rememberUpdatedState(onMapTapped)
    val latestOnRestaurantSelected by rememberUpdatedState(onRestaurantSelected)
    val latestState by rememberUpdatedState(state)

    LaunchedEffect(naverMapView) {
        naverMapView.showZoomControls = false
    }

    val touchDelegate = remember {
        object : NSObject(), NMFMapViewTouchDelegateProtocol {
            override fun mapView(
                mapView: NMFMapView,
                didTapMap: NMGLatLng,
                point: CValue<CGPoint>,
            ) {
                latestOnMapTapped()
            }
        }
    }

    val cameraDelegate = remember {
        object : NSObject(), NMFMapViewCameraDelegateProtocol {
            override fun mapViewCameraIdle(mapView: NMFMapView) {
                val camera = mapView.cameraPosition
                currentZoomState.intValue = camera.zoom.toInt()

                if (holder.suppressCameraIdle || !holder.didApplyInitialBounds) {
                    return
                }

                val cameraState = MapCameraState(
                    latitude = camera.target.lat(),
                    longitude = camera.target.lng(),
                    zoom = camera.zoom,
                    tilt = camera.tilt,
                    bearing = camera.heading
                )

                holder.lastCameraState = cameraState
                onCameraIdle(cameraState)

                val data = (latestState.map as? UiState.Success)?.data ?: return
                updateMapIos(
                    mapView = mapView,
                    mapData = data,
                    currentZoom = camera.zoom.toInt(),
                    mapHolder = holder,
                    selectedRestaurantId = latestState.selectedRestaurantId,
                    onRestaurantSelected = latestOnRestaurantSelected,
                    outlineColor = outlineUiColor,
                    tierMarkerSize = tierMarkerSize,
                )
            }
        }
    }

    DisposableEffect(mapView) {
        mapView.touchDelegate = touchDelegate
        mapView.addCameraDelegate(cameraDelegate)
        onDispose {
            mapView.touchDelegate = null
            mapView.removeCameraDelegate(cameraDelegate)
        }
    }

    LaunchedEffect(state.map) {
        if (state.map is UiState.Loading) {
            holder.didApplyInitialBounds = false
            holder.lastVisibleBounds = emptyList()
            holder.selectedMarker = null
            holder.markerRestaurantMap.clear()
        }
    }

    LaunchedEffect(state.map, currentZoomState.intValue) {
        val data = (state.map as? UiState.Success)?.data ?: return@LaunchedEffect

        mapView.minZoomLevel = data.minZoom.toDouble()

        updateMapIos(
            mapView = mapView,
            mapData = data,
            currentZoom = currentZoomState.intValue,
            mapHolder = holder,
            selectedRestaurantId = state.selectedRestaurantId,
            onRestaurantSelected = latestOnRestaurantSelected,
            outlineColor = outlineUiColor,
            tierMarkerSize = tierMarkerSize,
        )

        isCameraReady.value = true
    }

    LaunchedEffect(state.selectedRestaurantId, state.map) {
        if (state.map !is UiState.Success) return@LaunchedEffect
        if (holder.restaurantMarkers.isEmpty()) return@LaunchedEffect

        updateSelectedMarkerOnlyIos(
            mapHolder = holder,
            selectedRestaurantId = state.selectedRestaurantId,
            tierMarkerSize = tierMarkerSize,
        )
    }

    LaunchedEffect(state.map) {
        val data = (state.map as? UiState.Success)?.data ?: return@LaunchedEffect

        mapView.minZoomLevel = data.minZoom.toDouble()

        val incomingBounds = data.visibleBounds
        val shouldApplyBounds =
            !holder.didApplyInitialBounds || holder.lastVisibleBounds != incomingBounds

        if (shouldApplyBounds && incomingBounds.size >= 4) {
            holder.suppressCameraIdle = true
            isCameraReady.value = false

            moveCameraToVisibleBoundsIos(mapView, incomingBounds) {
                currentZoomState.intValue = mapView.cameraPosition.zoom.toInt()
                holder.lastVisibleBounds = incomingBounds
                holder.didApplyInitialBounds = true
                holder.suppressCameraIdle = false
                isCameraReady.value = true

                updateMapIos(
                    mapView = mapView,
                    mapData = data,
                    currentZoom = currentZoomState.intValue,
                    mapHolder = holder,
                    selectedRestaurantId = state.selectedRestaurantId,
                    onRestaurantSelected = latestOnRestaurantSelected,
                    outlineColor = outlineUiColor,
                    tierMarkerSize = tierMarkerSize,
                )
            }
        }
    }

    LaunchedEffect(state.cameraState) {
        val cameraState = state.cameraState ?: holder.lastCameraState ?: return@LaunchedEffect
        if (state.map !is UiState.Success) return@LaunchedEffect
        if (!holder.didApplyInitialBounds) return@LaunchedEffect

        holder.suppressCameraIdle = true
        moveCameraToCameraStateIos(mapView, cameraState) {
            currentZoomState.intValue = cameraState.zoom.toInt()
            holder.lastCameraState = cameraState
            holder.suppressCameraIdle = false
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
        UIKitView(
            modifier = Modifier.fillMaxSize(),
            factory = { naverMapView },
            update = { _ -> }
        )

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

        if (!isCameraReady.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(KusTheme.colors.c_FFFFFF),
                contentAlignment = Alignment.Center
            ) {
                KusLoadingAnimation()
            }
        }
    }
}

private fun updateMapIos(
    mapView: NMFMapView,
    mapData: TierMapData,
    currentZoom: Int,
    mapHolder: MapHolder,
    selectedRestaurantId: Long?,
    onRestaurantSelected: (Long) -> Unit,
    outlineColor: UIColor,
    tierMarkerSize: Double,
) {
    clearOverlaysAndMarkersIos(
        mapHolder = mapHolder
    )

    mapData.solidPolygonCoordsList.forEach { line ->
        if (line.isEmpty()) return@forEach

        val coords = line.map {
            NMGLatLng().apply {
                setLat(it.latitude)
                setLng(it.longitude)
            }
        }

        if (coords.size >= 2) {
            val polyline = NMFPolylineOverlay.polylineOverlayWith(closeLine(coords)) ?: return@forEach
            polyline.width = 3.0
            polyline.color = outlineColor
            polyline.zIndex = 1
            polyline.mapView = mapView
            mapHolder.polylineOverlays.add(polyline)
        }

        if (coords.size >= 3) {
            val ring = normalizeRing(coords)
            val polygon = NMFPolygonOverlay.polygonOverlayWith(ring) ?: return@forEach
            polygon.fillColor = PolygonColorsIos.POLYGON_SELECTED
            polygon.outlineWidth = 0UL
            polygon.zIndex = 0
            polygon.mapView = mapView
            mapHolder.polygonOverlays.add(polygon)
        }
    }

    mapData.dashedPolygonCoordsList.forEach { line ->
        if (line.isEmpty()) return@forEach

        val coords = line.map {
            NMGLatLng().apply {
                setLat(it.latitude)
                setLng(it.longitude)
            }
        }

        if (coords.size >= 2) {
            val polyline = NMFPolylineOverlay.polylineOverlayWith(closeLine(coords)) ?: return@forEach
            polyline.width = 3.0
            polyline.color = outlineColor
            polyline.pattern = listOf(8, 8)
            polyline.zIndex = 1
            polyline.mapView = mapView
            mapHolder.polylineOverlays.add(polyline)
        }

        if (coords.size >= 3) {
            val ring = normalizeRing(coords)
            val polygon = NMFPolygonOverlay.polygonOverlayWith(ring) ?: return@forEach
            polygon.fillColor = PolygonColorsIos.POLYGON_UNSELECTED
            polygon.outlineWidth = 0UL
            polygon.zIndex = 0
            polygon.mapView = mapView
            mapHolder.polygonOverlays.add(polygon)
        }
    }

    mapData.favoriteTierRestaurants.forEach { r ->
        createRestaurantMarkerIos(
            mapView = mapView,
            restaurant = r,
            selectedRestaurantId = selectedRestaurantId,
            onRestaurantSelected = onRestaurantSelected,
            restaurantMarkers = mapHolder.restaurantMarkers,
            markerRestaurantMap = mapHolder.markerRestaurantMap,
            restaurantIdMarkerMap = mapHolder.restaurantIdMarkerMap,
            mapHolder = mapHolder,
            tierMarkerSize = tierMarkerSize,
        )
    }

    mapData.tieredTierRestaurants.forEach { r ->
        createRestaurantMarkerIos(
            mapView = mapView,
            restaurant = r,
            selectedRestaurantId = selectedRestaurantId,
            onRestaurantSelected = onRestaurantSelected,
            restaurantMarkers = mapHolder.restaurantMarkers,
            markerRestaurantMap = mapHolder.markerRestaurantMap,
            restaurantIdMarkerMap = mapHolder.restaurantIdMarkerMap,
            mapHolder = mapHolder,
            tierMarkerSize = tierMarkerSize,
        )
    }

    mapData.nonTieredRestaurants
        .filter { it.zoom <= currentZoom }
        .forEach { group ->
            group.tierRestaurants.forEach { r ->
                createRestaurantMarkerIos(
                    mapView = mapView,
                    restaurant = r,
                    selectedRestaurantId = selectedRestaurantId,
                    onRestaurantSelected = onRestaurantSelected,
                    restaurantMarkers = mapHolder.restaurantMarkers,
                    markerRestaurantMap = mapHolder.markerRestaurantMap,
                    restaurantIdMarkerMap = mapHolder.restaurantIdMarkerMap,
                    mapHolder = mapHolder,
                    tierMarkerSize = tierMarkerSize,
                )
            }
        }

    mapHolder.selectedMarker = mapHolder.restaurantMarkers
        .firstOrNull { mapHolder.markerRestaurantMap[it]?.restaurantId == selectedRestaurantId }
}

private fun closeLine(coords: List<NMGLatLng>): List<NMGLatLng> {
    if (coords.size < 2) return coords
    val first = coords.first()
    val last = coords.last()
    return if (first.lat() == last.lat() && first.lng() == last.lng()) coords else coords + first
}

private fun closeRing(coords: List<NMGLatLng>): List<NMGLatLng> {
    if (coords.size < 3) return coords
    val first = coords.first()
    val last = coords.last()
    return if (first.lat() == last.lat() && first.lng() == last.lng()) coords else coords + first
}

private fun isClockwise(coords: List<NMGLatLng>): Boolean {
    var sum = 0.0
    for (i in 0 until coords.size - 1) {
        val p1 = coords[i]
        val p2 = coords[i + 1]
        sum += (p2.lng() - p1.lng()) * (p2.lat() + p1.lat())
    }
    return sum > 0
}

private fun normalizeRing(coords: List<NMGLatLng>): List<NMGLatLng> {
    val closed = closeRing(coords)
    return if (isClockwise(closed)) closed.reversed() else closed
}

private fun clearOverlaysAndMarkersIos(mapHolder: MapHolder) {
    mapHolder.polygonOverlays.forEach { it.mapView = null }
    mapHolder.polylineOverlays.forEach { it.mapView = null }
    mapHolder.restaurantMarkers.forEach { it.mapView = null }

    mapHolder.polygonOverlays.clear()
    mapHolder.polylineOverlays.clear()
    mapHolder.restaurantMarkers.clear()
    mapHolder.markerRestaurantMap.clear()
    mapHolder.restaurantIdMarkerMap.clear()
    mapHolder.selectedMarker = null
}


private fun applyMarkerAppearanceIos(
    marker: NMFMarker,
    restaurant: TierRestaurant,
    isSelected: Boolean,
    tierMarkerSize: Double,
) {
    val isSaved = restaurant.isFavorite
    val isTier = restaurant.mainTier in 1..4 && !isSaved
    val scale = if (isSelected) SELECTED_TIER_MARKER_SCALE else 1.0

    marker.iconImage = if (isSelected) {
        getSelectedMarkerIconIos(restaurant)
    } else {
        getMarkerIconIos(restaurant)
    }

    when {
        isTier -> {
            val size = tierMarkerSize * scale
            marker.width = size
            marker.height = size
        }

        isSaved -> {
            marker.width = SAVED_MARKER_WIDTH * scale
            marker.height = SAVED_MARKER_HEIGHT * scale
        }

        else -> {
            marker.width = DEFAULT_MARKER_WIDTH * scale
            marker.height = DEFAULT_MARKER_HEIGHT * scale
        }
    }

    marker.zIndex = when {
        isSelected -> 10
        isSaved -> 5
        else -> when (restaurant.mainTier) {
            1 -> 4
            2 -> 3
            3 -> 2
            4 -> 1
            else -> 0
        }
    }
}

private fun updateSelectedMarkerIos(
    mapHolder: MapHolder,
    newMarker: NMFMarker,
    tierMarkerSize: Double,
) {
    if (mapHolder.selectedMarker === newMarker) return

    mapHolder.selectedMarker?.let { oldMarker ->
        mapHolder.markerRestaurantMap[oldMarker]?.let { oldRestaurant ->
            applyMarkerAppearanceIos(
                marker = oldMarker,
                restaurant = oldRestaurant,
                isSelected = false,
                tierMarkerSize = tierMarkerSize,
            )
        }
    }

    mapHolder.markerRestaurantMap[newMarker]?.let { newRestaurant ->
        applyMarkerAppearanceIos(
            marker = newMarker,
            restaurant = newRestaurant,
            isSelected = true,
            tierMarkerSize = tierMarkerSize,
        )
    }

    mapHolder.selectedMarker = newMarker
}


private fun createRestaurantMarkerIos(
    mapView: NMFMapView,
    restaurant: TierRestaurant,
    selectedRestaurantId: Long?,
    onRestaurantSelected: (Long) -> Unit,
    restaurantMarkers: MutableList<NMFMarker>,
    markerRestaurantMap: MutableMap<NMFMarker, TierRestaurant>,
    restaurantIdMarkerMap: MutableMap<Long, NMFMarker>,
    mapHolder: MapHolder,
    tierMarkerSize: Double,
) {
    val isSelected = selectedRestaurantId == restaurant.restaurantId

    val marker = NMFMarker().apply {
        position = NMGLatLng().apply {
            setLat(restaurant.latitude)
            setLng(restaurant.longitude)
        }

        applyMarkerAppearanceIos(
            marker = this,
            restaurant = restaurant,
            isSelected = isSelected,
            tierMarkerSize = tierMarkerSize,
        )

        this.mapView = mapView

        touchHandler = { _ ->
            updateSelectedMarkerIos(
                mapHolder = mapHolder,
                newMarker = this,
                tierMarkerSize = tierMarkerSize,
            )
            onRestaurantSelected(restaurant.restaurantId)
            true
        }
    }

    restaurantMarkers.add(marker)
    markerRestaurantMap[marker] = restaurant
    restaurantIdMarkerMap[restaurant.restaurantId] = marker

    if (isSelected) {
        mapHolder.selectedMarker = marker
    }
}

private fun updateSelectedMarkerOnlyIos(
    mapHolder: MapHolder,
    selectedRestaurantId: Long?,
    tierMarkerSize: Double,
) {
    val prev = mapHolder.selectedMarker
    val prevRestaurant = prev?.let { mapHolder.markerRestaurantMap[it] }

    if (prev != null && prevRestaurant != null) {
        applyMarkerAppearanceIos(
            marker = prev,
            restaurant = prevRestaurant,
            isSelected = false,
            tierMarkerSize = tierMarkerSize,
        )
    }

    val next = selectedRestaurantId
        ?.let { mapHolder.restaurantIdMarkerMap[it] }

    val nextRestaurant = next?.let { mapHolder.markerRestaurantMap[it] }
    if (next != null && nextRestaurant != null) {
        applyMarkerAppearanceIos(
            marker = next,
            restaurant = nextRestaurant,
            isSelected = true,
            tierMarkerSize = tierMarkerSize,
        )
    }

    mapHolder.selectedMarker = next
}

private fun getSelectedMarkerIconIos(restaurant: TierRestaurant): NMFOverlayImage {
    val imageName = if (restaurant.isTempTier) {
        when {
            restaurant.isFavorite -> "ic_saved"
            restaurant.partnershipInfo.isNotEmpty() -> "ic_tier_partnership_selected"
            restaurant.mainTier == 1 -> "ic_temp_tier_1_selected"
            restaurant.mainTier == 2 -> "ic_temp_tier_2_selected"
            restaurant.mainTier == 3 -> "ic_temp_tier_3_selected"
            restaurant.mainTier == 4 -> "ic_temp_tier_4_selected"
            else -> "ic_marker_none_selected"
        }
    } else {
        when {
            restaurant.isFavorite -> "ic_saved"
            restaurant.partnershipInfo.isNotEmpty() -> "ic_tier_partnership_selected"
            restaurant.mainTier == 1 -> "ic_tier_1_selected"
            restaurant.mainTier == 2 -> "ic_tier_2_selected"
            restaurant.mainTier == 3 -> "ic_tier_3_selected"
            restaurant.mainTier == 4 -> "ic_tier_4_selected"
            else -> "ic_marker_none_selected"
        }
    }

    return NMFOverlayImage.overlayImageWithName(imageName)!!
}

private fun getMarkerIconIos(restaurant: TierRestaurant): NMFOverlayImage {
    val imageName = if (restaurant.isTempTier) {
        when {
            restaurant.isFavorite -> "ic_saved"
            restaurant.partnershipInfo.isNotEmpty() -> "ic_marker_partnership"
            restaurant.mainTier == 1 -> "ic_temp_tier_1"
            restaurant.mainTier == 2 -> "ic_temp_tier_2"
            restaurant.mainTier == 3 -> "ic_temp_tier_3"
            restaurant.mainTier == 4 -> "ic_temp_tier_4"
            else -> "ic_marker_none"
        }
    } else {
        when {
            restaurant.isFavorite -> "ic_saved"
            restaurant.partnershipInfo.isNotEmpty() -> "ic_marker_partnership"
            restaurant.mainTier == 1 -> "ic_tier_1"
            restaurant.mainTier == 2 -> "ic_tier_2"
            restaurant.mainTier == 3 -> "ic_tier_3"
            restaurant.mainTier == 4 -> "ic_tier_4"
            else -> "ic_marker_none"
        }
    }
    return NMFOverlayImage.overlayImageWithName(imageName)!!
}

private fun moveCameraToVisibleBoundsIos(
    mapView: NMFMapView,
    visibleBounds: List<Double>,
    onComplete: () -> Unit,
) {
    if (visibleBounds.size < 4) return

    val west = visibleBounds[0]
    val east = visibleBounds[1]
    val south = visibleBounds[2]
    val north = visibleBounds[3]

    val bounds = cocoapods.NMapsMap.NMGLatLngBounds.latLngBoundsWithSouthWestLat(
        southWestLat = south,
        southWestLng = west,
        northEastLat = north,
        northEastLng = east
    )

    val update = NMFCameraUpdate.cameraUpdateWithFitBounds(bounds = bounds)

    mapView.moveCamera(update) {
        onComplete()
    }
}

private fun moveCameraToCameraStateIos(
    mapView: NMFMapView,
    cameraState: MapCameraState,
    onComplete: () -> Unit,
) {
    val target = NMGLatLng().apply {
        setLat(cameraState.latitude)
        setLng(cameraState.longitude)
    }

    val position = NMFCameraPosition.cameraPosition(
        target,
        cameraState.zoom,
    )

    val update = NMFCameraUpdate.cameraUpdateWithPosition(position)

    mapView.moveCamera(update) { isCanceled ->
        if (!isCanceled) {
            onComplete()
        }
    }
}
