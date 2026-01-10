package com.kus.kustaurant.navigation

enum class BottomTab(
    val key: String,
    val title: String,
) {
    HOME("home", "홈"),
    DRAW("draw", "뽑기"),
    TIER("tier", "티어"),
    COMMUNITY("community", "커뮤니티"),
    MY("my", "마이페이지");

    companion object {
        fun fromRoute(route: String?): BottomTab {
            if (route == null) return HOME

            return when {
                route.contains("Home") -> HOME
                route.contains("Draw") -> DRAW
                route.contains("Tier") -> TIER
                route.contains("Community") -> COMMUNITY
                route.contains("My") -> MY
                else -> HOME
            }
        }
    }
}
