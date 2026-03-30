@file:OptIn(ExperimentalForeignApi::class)
package com.kus.appkit.util

import cocoapods.NMapsMap.NMGLatLng
import kotlinx.cinterop.ExperimentalForeignApi

fun closeLine(coords: List<NMGLatLng>): List<NMGLatLng> {
    if (coords.size < 2) return coords
    val first = coords.first()
    val last = coords.last()
    return if (first.lat() == last.lat() && first.lng() == last.lng()) coords else coords + first
}

fun closeRing(coords: List<NMGLatLng>): List<NMGLatLng> {
    if (coords.size < 3) return coords
    val first = coords.first()
    val last = coords.last()
    return if (first.lat() == last.lat() && first.lng() == last.lng()) coords else coords + first
}

fun isClockwise(coords: List<NMGLatLng>): Boolean {
    var sum = 0.0
    for (i in 0 until coords.size - 1) {
        val p1 = coords[i]
        val p2 = coords[i + 1]
        sum += (p2.lng() - p1.lng()) * (p2.lat() + p1.lat())
    }
    return sum > 0
}

fun normalizeRing(coords: List<NMGLatLng>): List<NMGLatLng> {
    val closed = closeRing(coords)
    return if (isClockwise(closed)) closed.reversed() else closed
}
