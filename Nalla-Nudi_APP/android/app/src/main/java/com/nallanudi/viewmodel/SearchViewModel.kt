package com.nallanudi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nallanudi.data.entity.WordEntity
import com.nallanudi.repository.WordRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val subject: String = "",
    val results: List<WordEntity> = emptyList(),
    val isLoading: Boolean = false,
)

@OptIn(FlowPreview::class)
class SearchViewModel(private val repository: WordRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collectLatest { query ->
                    _uiState.update { it.copy(isLoading = true) }
                    repository.searchWords(query, _uiState.value.subject).collect { results ->
                        _uiState.update { it.copy(results = results, isLoading = false) }
                    }
                }
        }
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
        _searchQuery.value = query
    }

    fun onSubjectChange(subject: String) {
        val newSubject = if (_uiState.value.subject == subject) "" else subject
        _uiState.update { it.copy(subject = newSubject) }
        _searchQuery.value = _uiState.value.query // re-trigger search
    }

    fun toggleSaved(wordId: String) {
        viewModelScope.launch { repository.toggleSaved(wordId) }
    }
}
