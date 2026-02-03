@file:OptIn(ExperimentalForeignApi::class)

package com.kus.feature.tier.ui.map


import kotlinx.cinterop.ExperimentalForeignApi
import cocoapods.NMapsMap.NMFPolygonOverlay
import cocoapods.NMapsMap.NMFPolylineOverlay
import cocoapods.NMapsMap.NMFMarker
import cocoapods.NMapsMap.NMFNaverMapView

class MapHolder(val mapView: NMFNaverMapView) {
    var lastCameraState: MapCameraState? = null
    var didApplyInitialBounds: Boolean = false

    val polygonOverlays = mutableListOf<NMFPolygonOverlay>()
    val polylineOverlays = mutableListOf<NMFPolylineOverlay>()
    val restaurantMarkers = mutableListOf<NMFMarker>()
}


