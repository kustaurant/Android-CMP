@file:OptIn(ExperimentalForeignApi::class)
package com.kus.appkit.map

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import cocoapods.NMapsMap.NMFMapView
import cocoapods.NMapsMap.NMFMarker
import cocoapods.NMapsMap.NMFOverlayImage
import cocoapods.NMapsMap.NMGLatLng
import cocoapods.NMapsMap.NMGLatLngBounds
import com.kus.appkit.util.buildMarkerDataKey
import com.kus.appkit.util.toViewportKey
import com.kus.shared.domain.model.tier.TierMapData
import com.kus.shared.domain.model.tier.TierRestaurant
import kotlinx.cinterop.ExperimentalForeignApi

fun refreshVisibleMarkersIos(
    mapView: NMFMapView,
    mapData: TierMapData,
    currentZoom: Int,
    mapHolder: MapHolder,
    selectedRestaurantId: Long?,
    onRestaurantSelected: (Long) -> Unit,
    tierMarkerSize: Double,
) {
    val markerDataKey = buildMarkerDataKey(mapData)
    val viewportBounds = expandedContentBounds(mapView.contentBounds)
    val viewportKey = viewportBounds.toViewportKey()

    val shouldSkip =
        mapHolder.lastMarkerDataKey == markerDataKey &&
                mapHolder.lastRenderedMarkerZoom == currentZoom &&
                mapHolder.lastRenderedViewportKey == viewportKey

    if (shouldSkip) {
        updateSelectedMarkerOnlyIos(
            mapHolder = mapHolder,
            selectedRestaurantId = selectedRestaurantId,
            tierMarkerSize = tierMarkerSize,
        )
        return
    }

    clearMarkersOnlyIos(mapHolder)

    fun containsPoint(
        bounds: NMGLatLngBounds,
        point: NMGLatLng,
    ): Boolean {
        return point.lat() >= bounds.southWestLat() &&
                point.lat() <= bounds.northEastLat() &&
                point.lng() >= bounds.southWestLng() &&
                point.lng() <= bounds.northEastLng()
    }

    fun isVisibleInViewport(restaurant: TierRestaurant): Boolean {
        val point = NMGLatLng().apply {
            setLat(restaurant.latitude)
            setLng(restaurant.longitude)
        }
        return containsPoint(viewportBounds, point)
    }

    fun addMarkerIfVisible(restaurant: TierRestaurant) {
        if (!isVisibleInViewport(restaurant)) return

        createRestaurantMarkerIos(
            mapView = mapView,
            restaurant = restaurant,
            selectedRestaurantId = selectedRestaurantId,
            onRestaurantSelected = onRestaurantSelected,
            restaurantMarkers = mapHolder.restaurantMarkers,
            markerRestaurantMap = mapHolder.markerRestaurantMap,
            restaurantIdMarkerMap = mapHolder.restaurantIdMarkerMap,
            mapHolder = mapHolder,
            tierMarkerSize = tierMarkerSize,
        )
    }

    mapData.favoriteTierRestaurants.forEach(::addMarkerIfVisible)
    mapData.tieredTierRestaurants.forEach(::addMarkerIfVisible)

    mapData.nonTieredRestaurants
        .filter { it.zoom <= currentZoom }
        .forEach { group ->
            group.tierRestaurants.forEach(::addMarkerIfVisible)
        }

    mapHolder.selectedMarker = selectedRestaurantId
        ?.let { mapHolder.restaurantIdMarkerMap[it] }

    mapHolder.lastMarkerDataKey = markerDataKey
    mapHolder.lastRenderedMarkerZoom = currentZoom
    mapHolder.lastRenderedViewportKey = viewportKey
}

fun clearMarkersOnlyIos(mapHolder: MapHolder) {
    mapHolder.restaurantMarkers.forEach { it.mapView = null }
    mapHolder.restaurantMarkers.clear()
    mapHolder.markerRestaurantMap.clear()
    mapHolder.restaurantIdMarkerMap.clear()
    mapHolder.selectedMarker = null
}

fun clearAllMapObjectsIos(mapHolder: MapHolder) {
    clearRegionOverlaysIos(mapHolder)
    clearMarkersOnlyIos(mapHolder)
}

fun applyMarkerAppearanceIos(
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

fun updateSelectedMarkerIos(
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

fun createRestaurantMarkerIos(
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

fun updateSelectedMarkerOnlyIos(
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

fun getSelectedMarkerIconIos(restaurant: TierRestaurant): NMFOverlayImage {
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

fun getMarkerIconIos(restaurant: TierRestaurant): NMFOverlayImage {
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
