package com.nallanudi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nallanudi.data.entity.WordEntity
import com.nallanudi.repository.WordRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

data class HomeUiState(
    val wordOfTheDay: WordEntity? = null,
    val savedCount: Int = 0,
    val totalWords: Int = 0,
    val wordsForReview: Int = 0,
    val streak: Int = 0,
    val isLoading: Boolean = true,
)

class HomeViewModel(private val repository: WordRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedIfNeeded()
            repository.updateStreak()
        }
        viewModelScope.launch {
            repository.getWordOfTheDay().collect { word ->
                _uiState.update { it.copy(wordOfTheDay = word) }
            }
        }
        viewModelScope.launch {
            repository.getSavedCount().collect { count ->
                _uiState.update { it.copy(savedCount = count) }
            }
        }
        viewModelScope.launch {
            repository.getTotalCount().collect { count ->
                _uiState.update { it.copy(totalWords = count, isLoading = false) }
            }
        }
        viewModelScope.launch {
            repository.getWordsForReview(System.currentTimeMillis()).collect { list ->
                _uiState.update { it.copy(wordsForReview = list.size) }
            }
        }
        viewModelScope.launch {
            repository.streak.collect { streak ->
                _uiState.update { it.copy(streak = streak) }
            }
        }
    }
}
