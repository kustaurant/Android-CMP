package com.kus.feature.search.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel: ViewModel() {
    private val _searchTerm = MutableStateFlow("")
    val searchTerm = _searchTerm.asStateFlow()
    private val _resultItems = MutableStateFlow<List<Int>>(listOf(1,2,3,4,5,6,7,8,9, 10, 11, 12, 13)) // TODO : 도메인 모델 변경
    val resultItems = _resultItems.asStateFlow()

    fun updateSearchTerm(new: String) {
        _searchTerm.value = new
    }
}