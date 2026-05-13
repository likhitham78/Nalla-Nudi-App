package com.nallanudi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nallanudi.data.entity.ProgressEntity
import com.nallanudi.data.entity.WordEntity
import com.nallanudi.repository.WordRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class WordDetailUiState(
    val word: WordEntity? = null,
    val progress: ProgressEntity? = null,
    val isSaved: Boolean = false,
)

class WordDetailViewModel(private val repository: WordRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(WordDetailUiState())
    val uiState: StateFlow<WordDetailUiState> = _uiState.asStateFlow()

    fun loadWord(wordId: String) {
        viewModelScope.launch {
            repository.getWordById(wordId).collect { word ->
                _uiState.update { it.copy(word = word, isSaved = word?.isSaved ?: false) }
            }
        }
        viewModelScope.launch {
            repository.getProgress(wordId).collect { progress ->
                _uiState.update { it.copy(progress = progress) }
            }
        }
    }

    fun toggleSaved() {
        val wordId = _uiState.value.word?.id ?: return
        viewModelScope.launch { repository.toggleSaved(wordId) }
    }
}
