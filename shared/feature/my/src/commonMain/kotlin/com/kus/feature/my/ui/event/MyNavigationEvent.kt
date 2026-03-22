package com.kus.feature.my.ui.event

sealed interface MyNavigationEvent {
    data object NavigateToLogin: MyNavigationEvent
    data object NavigateToUp: MyNavigationEvent
}
