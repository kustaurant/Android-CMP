package com.kus.feature.my.event

sealed interface MyNavigationEvent {
    data object NavigateToLogin: MyNavigationEvent
    data object NavigateToUp: MyNavigationEvent
}
