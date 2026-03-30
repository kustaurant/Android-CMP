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
import cocoapods.NMapsMap.NMFPolygonOverlay
import cocoapods.NMapsMap.NMFPolylineOverlay
import cocoapods.NMapsMap.NMGLatLng
import cocoapods.NMapsMap.NMGLatLngBounds
import com.kus.appkit.util.buildOverlayDataKey
import com.kus.appkit.util.closeLine
import com.kus.appkit.util.normalizeRing
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
    val latestOnCameraIdle by rememberUpdatedState(onCameraIdle)
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
                val currentZoom = camera.zoom.toInt()
                currentZoomState.intValue = currentZoom

                if (holder.suppressCameraIdle || !holder.didApplyInitialBounds) {
                    return
                }

                val cameraState = MapCameraState(
                    latitude = camera.target.lat(),
                    longitude = camera.target.lng(),
                    zoom = camera.zoom,
                    tilt = camera.tilt,
                    bearing = camera.heading,
                )

                holder.lastCameraState = cameraState
                latestOnCameraIdle(cameraState)

                val data = (latestState.map as? UiState.Success)?.data ?: return

                refreshVisibleMarkersIos(
                    mapView = mapView,
                    mapData = data,
                    currentZoom = currentZoom,
                    mapHolder = holder,
                    selectedRestaurantId = latestState.selectedRestaurantId,
                    onRestaurantSelected = latestOnRestaurantSelected,
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
        when (val mapState = state.map) {
            is UiState.Loading -> {
                holder.didApplyInitialBounds = false
                holder.suppressCameraIdle = false
                holder.lastVisibleBounds = emptyList()
                holder.selectedMarker = null

                holder.lastOverlayDataKey = null
                holder.lastMarkerDataKey = null
                holder.lastRenderedMarkerZoom = null
                holder.lastRenderedViewportKey = null

                clearAllMapObjectsIos(holder)
                isCameraReady.value = false
            }

            is UiState.Success -> {
                val data = mapState.data
                mapView.minZoomLevel = data.minZoom.toDouble()

                ensureRegionOverlaysIos(
                    mapView = mapView,
                    mapData = data,
                    mapHolder = holder,
                    outlineColor = outlineUiColor,
                )

                val incomingBounds = data.visibleBounds
                val shouldApplyBounds =
                    !holder.didApplyInitialBounds || holder.lastVisibleBounds != incomingBounds

                if (shouldApplyBounds && incomingBounds.size >= 4) {
                    holder.suppressCameraIdle = true
                    isCameraReady.value = false

                    moveCameraToVisibleBoundsIos(mapView, incomingBounds) {
                        val currentZoom = mapView.cameraPosition.zoom.toInt()
                        currentZoomState.intValue = currentZoom

                        holder.lastVisibleBounds = incomingBounds
                        holder.didApplyInitialBounds = true
                        holder.suppressCameraIdle = false
                        isCameraReady.value = true

                        refreshVisibleMarkersIos(
                            mapView = mapView,
                            mapData = data,
                            currentZoom = currentZoom,
                            mapHolder = holder,
                            selectedRestaurantId = state.selectedRestaurantId,
                            onRestaurantSelected = latestOnRestaurantSelected,
                            tierMarkerSize = tierMarkerSize,
                        )
                    }
                } else {
                    val currentZoom = mapView.cameraPosition.zoom.toInt()
                    currentZoomState.intValue = currentZoom
                    holder.didApplyInitialBounds = true

                    refreshVisibleMarkersIos(
                        mapView = mapView,
                        mapData = data,
                        currentZoom = currentZoom,
                        mapHolder = holder,
                        selectedRestaurantId = state.selectedRestaurantId,
                        onRestaurantSelected = latestOnRestaurantSelected,
                        tierMarkerSize = tierMarkerSize,
                    )

                    isCameraReady.value = true
                }
            }

            else -> Unit
        }
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

    LaunchedEffect(state.cameraState) {
        val cameraState = state.cameraState ?: holder.lastCameraState ?: return@LaunchedEffect
        val data = (state.map as? UiState.Success)?.data ?: return@LaunchedEffect
        if (!holder.didApplyInitialBounds) return@LaunchedEffect

        holder.suppressCameraIdle = true

        moveCameraToCameraStateIos(mapView, cameraState) {
            val currentZoom = cameraState.zoom.toInt()
            currentZoomState.intValue = currentZoom
            holder.lastCameraState = cameraState
            holder.suppressCameraIdle = false

            refreshVisibleMarkersIos(
                mapView = mapView,
                mapData = data,
                currentZoom = currentZoom,
                mapHolder = holder,
                selectedRestaurantId = state.selectedRestaurantId,
                onRestaurantSelected = latestOnRestaurantSelected,
                tierMarkerSize = tierMarkerSize,
            )
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
            update = { _ -> },
        )

        val visible = state.isShowBottomSheet && selectedRestaurant != null

        AnimatedVisibility(
            visible = visible,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        ) {
            val r = selectedRestaurant ?: return@AnimatedVisibility
            TierRestaurantBottomSheetCard(
                restaurant = r,
                onClick = { onBottomSheetClick(r.restaurantId) },
            )
        }

        if (!isCameraReady.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(KusTheme.colors.c_FFFFFF),
                contentAlignment = Alignment.Center,
            ) {
                KusLoadingAnimation()
            }
        }
    }
}

private fun ensureRegionOverlaysIos(
    mapView: NMFMapView,
    mapData: TierMapData,
    mapHolder: MapHolder,
    outlineColor: UIColor,
) {
    val overlayDataKey = buildOverlayDataKey(mapData)
    if (mapHolder.lastOverlayDataKey == overlayDataKey) return

    clearRegionOverlaysIos(mapHolder)

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

    mapHolder.lastOverlayDataKey = overlayDataKey
}

fun clearRegionOverlaysIos(mapHolder: MapHolder) {
    mapHolder.polygonOverlays.forEach { it.mapView = null }
    mapHolder.polylineOverlays.forEach { it.mapView = null }
    mapHolder.polygonOverlays.clear()
    mapHolder.polylineOverlays.clear()
}

fun expandedContentBounds(
    bounds: NMGLatLngBounds,
    latBufferRatio: Double = 0.08,
    lngBufferRatio: Double = 0.08,
    minLatBuffer: Double = 0.0003,
    minLngBuffer: Double = 0.0003,
): NMGLatLngBounds {
    val latBuffer = maxOf(bounds.latSpan() * latBufferRatio, minLatBuffer)
    val lngBuffer = maxOf(bounds.lngSpan() * lngBufferRatio, minLngBuffer)

    return NMGLatLngBounds.latLngBoundsWithSouthWestLat(
        southWestLat = bounds.southWestLat() - latBuffer,
        southWestLng = bounds.southWestLng() - lngBuffer,
        northEastLat = bounds.northEastLat() + latBuffer,
        northEastLng = bounds.northEastLng() + lngBuffer,
    )
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

    val bounds = NMGLatLngBounds.latLngBoundsWithSouthWestLat(
        southWestLat = south,
        southWestLng = west,
        northEastLat = north,
        northEastLng = east,
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
