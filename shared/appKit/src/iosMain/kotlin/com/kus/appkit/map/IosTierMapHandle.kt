@file:OptIn(ExperimentalForeignApi::class)
package com.kus.appkit.map

import cocoapods.NMapsMap.NMFNaverMapView
import com.kus.feature.tier.ui.map.TierMapHandle
import kotlinx.cinterop.ExperimentalForeignApi

class IosTierMapHandle(val mapView: NMFNaverMapView) : TierMapHandle
