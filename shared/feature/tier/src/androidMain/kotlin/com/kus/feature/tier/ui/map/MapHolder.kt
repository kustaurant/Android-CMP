package com.kus.feature.tier.ui.map

import com.kus.shared.domain.model.tier.TierRestaurant
import com.naver.maps.map.MapView
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PolygonOverlay
import com.naver.maps.map.overlay.PolylineOverlay

class MapHolder(val mapView: MapView) {
    var isLoaded: Boolean = false

    val polygonOverlays = mutableListOf<PolygonOverlay>()
    val polylineOverlays = mutableListOf<PolylineOverlay>()
    val restaurantMarkers = mutableListOf<Marker>()

    val markerRestaurantMap = mutableMapOf<Marker, TierRestaurant>()
    val restaurantIdMarkerMap = mutableMapOf<Long, Marker>()

    var selectedMarker: Marker? = null

    var hasAppliedInitialCamera: Boolean = false

    var lastOverlayDataKey: String? = null
    var lastMarkerDataKey: String? = null
    var lastRenderedViewportKey: String? = null
    var lastRenderedMarkerZoom: Int? = null
}