package com.kus.feature.my.ui

import androidx.lifecycle.ViewModel
import com.kus.feature.my.ui.state.MyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MyViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MyUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadMyInfo()
    }

    fun loadMyInfo() {

    }

    fun onTabSelected(tabIndex: Int) {
        _uiState.update { it.copy(selectedTab = tabIndex) }
    }
}
