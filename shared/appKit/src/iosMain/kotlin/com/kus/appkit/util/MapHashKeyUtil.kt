@file:OptIn(ExperimentalForeignApi::class)

package com.kus.appkit.util

import cocoapods.NMapsMap.NMGLatLngBounds
import com.kus.appkit.map.ViewportKey
import com.kus.shared.domain.model.tier.TierMapData
import kotlinx.cinterop.ExperimentalForeignApi

fun buildOverlayDataKey(mapData: TierMapData): Int {
    var result = mapData.solidPolygonCoordsList.hashCode()
    result = 31 * result + mapData.dashedPolygonCoordsList.hashCode()
    return result
}

fun buildMarkerDataKey(mapData: TierMapData): Int {
    var result = mapData.favoriteTierRestaurants.hashCode()
    result = 31 * result + mapData.tieredTierRestaurants.hashCode()
    result = 31 * result + mapData.nonTieredRestaurants.hashCode()
    return result
}

fun NMGLatLngBounds.toViewportKey(): ViewportKey {
    fun Double.round6(): Double = kotlin.math.round(this * 1_000_000.0) / 1_000_000.0

    return ViewportKey(
        south = southWestLat().round6(),
        west = southWestLng().round6(),
        north = northEastLat().round6(),
        east = northEastLng().round6(),
    )
}