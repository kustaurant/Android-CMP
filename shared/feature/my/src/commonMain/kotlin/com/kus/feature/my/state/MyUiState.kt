package com.kus.feature.my.state

import UiState
import com.kus.shared.domain.model.my.MyInfo

data class MyUiState (
    val selectedTab: Int = 0,
    val pageState: MyPageState = MyPageState(),
    val userProfileState: UiState<MyInfo> = UiState.Loading,
    val toastMessage: String? = null,
)
