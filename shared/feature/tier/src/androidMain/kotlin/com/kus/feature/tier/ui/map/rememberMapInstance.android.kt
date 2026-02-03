package com.kus.feature.tier.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.naver.maps.map.MapView

@Composable
actual fun rememberMapInstance(): Any {
    val context = LocalContext.current
    return remember {
        val mv = MapView(context).apply { onCreate(android.os.Bundle()) }
        val holder = MapHolder(mv)
        mv.getMapAsync { map ->
            map.addOnLoadListener {
                holder.isLoaded = true
            }
        }
        holder
    }
}