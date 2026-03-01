package com.kus.kustaurant.navigation.util

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.kus.kustaurant.navigation.HIDE_BOTTOM_BAR_ROUTES

fun shouldShowBottomBar(destination: NavDestination?): Boolean {
    if (destination == null) return true

    return destination.hierarchy.none { dest ->
        HIDE_BOTTOM_BAR_ROUTES.any { routeClass ->
            dest.hasRoute(routeClass)
        }
    }
}
