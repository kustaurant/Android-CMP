package com.kus.feature.tier.ui.map

data class MapCameraState(
    val latitude: Double,
    val longitude: Double,
    val zoom: Double,
    val tilt: Double = 0.0,
    val bearing: Double = 0.0,
)