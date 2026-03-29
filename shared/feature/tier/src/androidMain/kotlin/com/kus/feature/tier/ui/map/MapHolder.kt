package com.kus.feature.tier.ui.map

import com.naver.maps.map.MapView
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PolygonOverlay
import com.naver.maps.map.overlay.PolylineOverlay

class MapHolder(val mapView: MapView) {
    var isLoaded: Boolean = false

    val polygonOverlays = mutableListOf<PolygonOverlay>()
    val polylineOverlays = mutableListOf<PolylineOverlay>()
    val restaurantMarkers = mutableListOf<Marker>()

    var selectedMarker: Marker? = null

    var hasAppliedInitialCamera: Boolean = false
}