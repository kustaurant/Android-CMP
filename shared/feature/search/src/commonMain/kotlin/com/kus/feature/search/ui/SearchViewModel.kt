package com.kus.feature.search.ui

import UiError
import UiState
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.feature.search.state.SearchUiState
import com.kus.shared.domain.search.usecase.GetSearchResultUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getSearchResultUseCase: GetSearchResultUseCase,
) : ViewModel() {
    private val _searchTerm = MutableStateFlow(TextFieldValue(""))
    val searchTerm = _searchTerm.asStateFlow()
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeSearchTerm()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchTerm() {
        viewModelScope.launch {
            _searchTerm
                .debounce(DEBOUNCE_DELAY)
                .distinctUntilChanged()
                .collectLatest { term ->
                    val query = term.text.trim()

                    if (query.isBlank()) {
                        _uiState.value = SearchUiState()
                        return@collectLatest
                    }

                    search(query)
                }
        }
    }

    private fun search(term: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    uiState = UiState.Loading,
                    items = emptyList(),
                    page = 1,
                    isLastPage = false,
                )
            }

            runCatching {
                getSearchResultUseCase(term, 1)
            }.onSuccess { result ->
                _uiState.update {
                    it.copy(
                        uiState = UiState.Success(Unit),
                        items = result.items,
                        page = 1,
                        isLastPage = !result.hasNext,
                    )
                }
            }.onFailure { e ->
                _uiState.update {
                    it.copy(
                        uiState = UiState.Failure(UiError.Message(e.message ?: "검색 실패"))
                    )
                }
            }
        }
    }

    fun loadNextPage() {
        val state = _uiState.value
        val term = _searchTerm.value

        if (state.isPaging || state.isLastPage || term.text.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isPaging = true) }

            val nextPage = state.page + 1

            runCatching {
                getSearchResultUseCase(term.text, nextPage)
            }.onSuccess { result ->
                val merged = (state.items + result.items).distinctBy { it.id }

                _uiState.update {
                    it.copy(
                        items = merged,
                        page = nextPage,
                        isLastPage = !result.hasNext,
                        isPaging = false,
                    )
                }

            }.onFailure {
                _uiState.update { it.copy(isPaging = false) }
            }
        }
    }

    fun updateSearchTerm(new: TextFieldValue) {
        _searchTerm.value = new
    }

    companion object {
        private const val DEBOUNCE_DELAY = 500L
    }
}
