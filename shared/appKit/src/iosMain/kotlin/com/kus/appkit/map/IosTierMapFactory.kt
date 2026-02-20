@file:OptIn(ExperimentalForeignApi::class)
package com.kus.appkit.map

import cocoapods.NMapsMap.NMFNaverMapView
import com.kus.feature.tier.ui.map.TierMapFactory
import com.kus.feature.tier.ui.map.TierMapHandle
import kotlinx.cinterop.ExperimentalForeignApi

class IosTierMapFactory : TierMapFactory {
    override fun createMapHandle(): TierMapHandle {
        val view = NMFNaverMapView()
        return IosTierMapHandle(view)
    }
}
