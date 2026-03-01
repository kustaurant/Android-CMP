package com.kus.feature.my.ui.state

import UiState

data class MyUiState (
    val selectedTab: Int = 0,
    val pageState: MyPageState = MyPageState(),
    val userProfileState: UiState<String> = UiState.Loading,
)
