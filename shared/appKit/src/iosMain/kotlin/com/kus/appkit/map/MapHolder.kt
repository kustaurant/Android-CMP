@file:OptIn(ExperimentalForeignApi::class)
package com.kus.appkit.map

import com.kus.feature.tier.ui.map.MapCameraState
import cocoapods.NMapsMap.NMFNaverMapView
import cocoapods.NMapsMap.NMFPolygonOverlay
import cocoapods.NMapsMap.NMFMarker
import cocoapods.NMapsMap.NMFPolylineOverlay
import kotlinx.cinterop.ExperimentalForeignApi

class MapHolder(val mapView: NMFNaverMapView) {
    var lastCameraState: MapCameraState? = null
    var didApplyInitialBounds: Boolean = false

    var lastVisibleBounds: List<Double> = emptyList()

    val polygonOverlays = mutableListOf<NMFPolygonOverlay>()
    val polylineOverlays = mutableListOf<NMFPolylineOverlay>()
    val restaurantMarkers = mutableListOf<NMFMarker>()
}