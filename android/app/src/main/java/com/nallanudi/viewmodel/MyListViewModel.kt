package com.nallanudi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nallanudi.data.entity.WordEntity
import com.nallanudi.repository.WordRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MyListUiState(
    val savedWords: List<WordEntity> = emptyList(),
    val isLoading: Boolean = true,
)

class MyListViewModel(private val repository: WordRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MyListUiState())
    val uiState: StateFlow<MyListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getSavedWords().collect { words ->
                _uiState.update { it.copy(savedWords = words, isLoading = false) }
            }
        }
    }

    fun toggleSaved(wordId: String) {
        viewModelScope.launch { repository.toggleSaved(wordId) }
    }
}
