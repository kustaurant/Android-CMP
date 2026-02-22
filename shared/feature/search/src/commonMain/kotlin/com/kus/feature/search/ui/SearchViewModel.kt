package com.kus.feature.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.feature.search.state.SearchUiState
import com.kus.shared.domain.search.usecase.GetSearchResultUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getSearchResultUseCase: GetSearchResultUseCase,
) : ViewModel() {
    private val _searchTerm = MutableStateFlow("")
    val searchTerm = _searchTerm.asStateFlow()
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    @OptIn(FlowPreview::class)
    private fun getResult() = viewModelScope.launch {
        _searchTerm
            .debounce(DEBOUNCE_DELAY)
            .collectLatest { searchTerm ->
                runCatching {
                    getSearchResultUseCase(searchTerm)
                }.onSuccess { results ->
                    _uiState.update { it.copy(results = UiState.Success(results)) }
                }.onFailure {
                    _uiState.update { it.copy(results = UiState.Failure(UiError.Network)) }
                }
            }
    }

    fun updateSearchTerm(new: String) {
        _searchTerm.value = new

        if (searchTerm.value.isEmpty()) _uiState.update { it.copy(results = UiState.Idle) }
        else getResult()
    }

    companion object {
        private const val DEBOUNCE_DELAY = 500L
    }
}
