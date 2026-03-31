package com.kus.feature.tier.ui.map

import com.kus.shared.domain.model.tier.TierMapData
import com.kus.shared.domain.model.tier.TierRestaurant
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import java.util.Locale

fun isVisibleInViewport(
    viewportBounds: LatLngBounds,
    restaurant: TierRestaurant,
): Boolean {
    return viewportBounds.contains(LatLng(restaurant.latitude, restaurant.longitude))
}

fun expandedContentBounds(
    bounds: LatLngBounds,
    extraRatio: Double = 0.15,
): LatLngBounds {
    val south = bounds.southWest.latitude
    val west = bounds.southWest.longitude
    val north = bounds.northEast.latitude
    val east = bounds.northEast.longitude

    val latPadding = (north - south) * extraRatio
    val lngPadding = (east - west) * extraRatio

    return LatLngBounds(
        LatLng(south - latPadding, west - lngPadding),
        LatLng(north + latPadding, east + lngPadding),
    )
}

fun LatLngBounds.toViewportKey(precision: Int = 4): String {
    fun Double.q(): String = "%.${precision}f".format(Locale.US, this)

    return listOf(
        southWest.latitude.q(),
        southWest.longitude.q(),
        northEast.latitude.q(),
        northEast.longitude.q(),
    ).joinToString(",")
}

fun buildOverlayDataKey(mapData: TierMapData): String {
    return buildString {
        append(mapData.solidPolygonCoordsList.hashCode())
        append('_')
        append(mapData.dashedPolygonCoordsList.hashCode())
    }
}

fun buildMarkerDataKey(mapData: TierMapData): String {
    return buildString {
        append(mapData.favoriteTierRestaurants.hashCode())
        append('_')
        append(mapData.tieredTierRestaurants.hashCode())
        append('_')
        append(mapData.nonTieredRestaurants.hashCode())
    }
}