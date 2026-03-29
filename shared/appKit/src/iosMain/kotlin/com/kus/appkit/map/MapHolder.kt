@file:OptIn(ExperimentalForeignApi::class)
package com.kus.appkit.map

import cocoapods.NMapsMap.NMFMarker
import cocoapods.NMapsMap.NMFNaverMapView
import cocoapods.NMapsMap.NMFPolygonOverlay
import cocoapods.NMapsMap.NMFPolylineOverlay
import com.kus.feature.tier.ui.map.MapCameraState
import com.kus.shared.domain.model.tier.TierRestaurant
import kotlinx.cinterop.ExperimentalForeignApi

const val SELECTED_TIER_MARKER_SCALE = 1.2

const val DEFAULT_MARKER_WIDTH = 15.0
const val DEFAULT_MARKER_HEIGHT = 20.0

const val SAVED_MARKER_WIDTH = 12.0
const val SAVED_MARKER_HEIGHT = 15.0

class MapHolder(val mapView: NMFNaverMapView) {
    var lastCameraState: MapCameraState? = null
    var didApplyInitialBounds: Boolean = false
    var suppressCameraIdle: Boolean = false

    var lastVisibleBounds: List<Double> = emptyList()

    var selectedMarker: NMFMarker? = null

    val polygonOverlays = mutableListOf<NMFPolygonOverlay>()
    val polylineOverlays = mutableListOf<NMFPolylineOverlay>()
    val restaurantMarkers = mutableListOf<NMFMarker>()

    val markerRestaurantMap = mutableMapOf<NMFMarker, TierRestaurant>()
    val restaurantIdMarkerMap = mutableMapOf<Long, NMFMarker>()
}
