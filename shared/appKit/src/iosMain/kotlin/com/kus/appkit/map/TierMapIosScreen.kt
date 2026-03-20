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
 
    val polygonOverlays = holder.polygonOverlays
    val polylineOverlays = holder.polylineOverlays
    val restaurantMarkers = holder.restaurantMarkers

    val outlineUiColor = KusTheme.colors.c_43AB38.toUIColor()
    val currentZoomState = remember { mutableIntStateOf((holder.lastCameraState?.zoom ?: 11.0).toInt()) }

    val latestOnMapTapped by rememberUpdatedState(onMapTapped)
    val latestOnRestaurantSelected by rememberUpdatedState(onRestaurantSelected)
    val latestState by rememberUpdatedState(state)

    val touchDelegate = remember {
        object : NSObject(), NMFMapViewTouchDelegateProtocol {
            override fun mapView(
                mapView: NMFMapView,
                didTapMap: NMGLatLng,
                point: CValue<CGPoint>,
            ){
                latestOnMapTapped()
            }
        }
    }

    val cameraDelegate = remember {
        object : NSObject(), NMFMapViewCameraDelegateProtocol {
            override fun mapViewCameraIdle(mapView: NMFMapView) {
                val camera = mapView.cameraPosition

                if (!holder.didApplyInitialBounds) {
                    currentZoomState.intValue = camera.zoom.toInt()
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
                currentZoomState.intValue = camera.zoom.toInt()
                onCameraIdle(cameraState)

                val data = (latestState.map as? UiState.Success)?.data ?: return
                updateMapIos(
                    mapView = mapView,
                    mapData = data,
                    currentZoom = camera.zoom.toInt(),
                    polygonOverlays = polygonOverlays,
                    polylineOverlays = polylineOverlays,
                    restaurantMarkers = restaurantMarkers,
                    onRestaurantSelected = latestOnRestaurantSelected,
                    outlineColor = outlineUiColor,
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
        val data = (state.map as? UiState.Success)?.data ?: return@LaunchedEffect

        if (!holder.didApplyInitialBounds) {
            mapView.minZoomLevel = data.minZoom.toDouble()
            holder.didApplyInitialBounds = true
            moveCameraToVisibleBoundsIos(mapView, data.visibleBounds) {
                holder.isCameraReady = true
                updateMapIos(
                    mapView = mapView,
                    mapData = data,
                    currentZoom = currentZoomState.intValue,
                    polygonOverlays = polygonOverlays,
                    polylineOverlays = polylineOverlays,
                    restaurantMarkers = restaurantMarkers,
                    onRestaurantSelected = onRestaurantSelected,
                    outlineColor = outlineUiColor,
                )
            }
            holder.lastVisibleBounds = data.visibleBounds
        } else {
            if (holder.lastVisibleBounds != data.visibleBounds) {
                holder.lastVisibleBounds = data.visibleBounds
                moveCameraToVisibleBoundsIos(mapView, data.visibleBounds) {
                    updateMapIos(
                        mapView = mapView,
                        mapData = data,
                        currentZoom = currentZoomState.intValue,
                        polygonOverlays = polygonOverlays,
                        polylineOverlays = polylineOverlays,
                        restaurantMarkers = restaurantMarkers,
                        onRestaurantSelected = onRestaurantSelected,
                        outlineColor = outlineUiColor,
                    )
                }
            } else {
                updateMapIos(
                    mapView = mapView,
                    mapData = data,
                    currentZoom = currentZoomState.intValue,
                    polygonOverlays = polygonOverlays,
                    polylineOverlays = polylineOverlays,
                    restaurantMarkers = restaurantMarkers,
                    onRestaurantSelected = onRestaurantSelected,
                    outlineColor = outlineUiColor,
                )
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
        UIKitView(
            modifier = Modifier.fillMaxSize(),
            factory = { naverMapView },
            update = { _ -> }
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

        if (!holder.isCameraReady) {
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
    polygonOverlays: MutableList<NMFPolygonOverlay>,
    polylineOverlays: MutableList<NMFPolylineOverlay>,
    restaurantMarkers: MutableList<NMFMarker>,
    onRestaurantSelected: (Long) -> Unit,
    outlineColor: UIColor,
) {
    clearOverlaysAndMarkersIos(polygonOverlays, polylineOverlays, restaurantMarkers)

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
            polylineOverlays.add(polyline)
        }

        if (coords.size >= 3) {
            val ring = normalizeRing(coords)

            val polygon = NMFPolygonOverlay.polygonOverlayWith(ring) ?: return@forEach
            polygon.fillColor = PolygonColorsIos.POLYGON_SELECTED
            polygon.outlineWidth = 0UL
            polygon.zIndex = 0
            polygon.mapView = mapView
            polygonOverlays.add(polygon)
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
            polylineOverlays.add(polyline)
        }

        if (coords.size >= 3) {
            val ring = normalizeRing(coords)
            val polygon = NMFPolygonOverlay.polygonOverlayWith(ring) ?: return@forEach
            polygon.fillColor = PolygonColorsIos.POLYGON_UNSELECTED
            polygon.outlineWidth = 0UL
            polygon.zIndex = 0
            polygon.mapView = mapView
            polygonOverlays.add(polygon)
        }
    }

    mapData.favoriteTierRestaurants.forEach { r ->
        createRestaurantMarkerIos(mapView, r, onRestaurantSelected, restaurantMarkers)
    }
    mapData.tieredTierRestaurants.forEach { r ->
        createRestaurantMarkerIos(mapView, r, onRestaurantSelected, restaurantMarkers)
    }
    mapData.nonTieredRestaurants
        .filter { it.zoom <= currentZoom }
        .forEach { group ->
            group.tierRestaurants.forEach { r ->
                createRestaurantMarkerIos(mapView, r, onRestaurantSelected, restaurantMarkers)
            }
        }
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

private fun clearOverlaysAndMarkersIos(
    polygonOverlays: MutableList<NMFPolygonOverlay>,
    polylineOverlays: MutableList<NMFPolylineOverlay>,
    restaurantMarkers: MutableList<NMFMarker>,
) {
    polygonOverlays.forEach { it.mapView = null }
    polygonOverlays.clear()

    polylineOverlays.forEach { it.mapView = null }
    polylineOverlays.clear()

    restaurantMarkers.forEach { it.mapView = null }
    restaurantMarkers.clear()
}

private fun createRestaurantMarkerIos(
    mapView: NMFMapView,
    restaurant: TierRestaurant,
    onRestaurantSelected: (Long) -> Unit,
    restaurantMarkers: MutableList<NMFMarker>,
) {
    val marker = NMFMarker().apply {
        position = NMGLatLng().apply {
            setLat(restaurant.latitude)
            setLng(restaurant.longitude)
        }
        iconImage = getMarkerIconIos(restaurant)

        val isNoneMarker = !restaurant.isFavorite &&
                restaurant.partnershipInfo.isNotEmpty() ||
                restaurant.mainTier !in 1..4
        if (isNoneMarker) {
            width = 15.0
            height = 20.0
        }

        this.mapView = mapView

        zIndex = when {
            restaurant.isFavorite -> 5
            else -> when (restaurant.mainTier) {
                1 -> 4
                2 -> 3
                3 -> 2
                4 -> 1
                else -> 0
            }
        }

        touchHandler = { _ ->
            onRestaurantSelected(restaurant.restaurantId)
            true
        }
    }
    restaurantMarkers.add(marker)
}

private fun getMarkerIconIos(restaurant: TierRestaurant): NMFOverlayImage {
    val imageName = if(restaurant.isTempTier) {
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
    onComplete: () -> Unit
) {
    if (visibleBounds.size < 4) return

    val west  = visibleBounds[0]
    val east  = visibleBounds[1]
    val south = visibleBounds[2]
    val north = visibleBounds[3]

    val bounds = cocoapods.NMapsMap.NMGLatLngBounds.latLngBoundsWithSouthWestLat(
        southWestLat = south,
        southWestLng = west,
        northEastLat = north,
        northEastLng = east
    )

    val update = NMFCameraUpdate.cameraUpdateWithFitBounds(
        bounds = bounds,
    )

    mapView.moveCamera(update) {
        onComplete()
    }
}
