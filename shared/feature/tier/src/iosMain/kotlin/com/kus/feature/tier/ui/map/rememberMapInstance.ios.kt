@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.kus.feature.tier.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import cocoapods.NMapsMap.NMFNaverMapView

@Composable
actual fun rememberMapInstance(): Any {
    return remember {
        val mapView = NMFNaverMapView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0))
        MapHolder(mapView)
    }
}

