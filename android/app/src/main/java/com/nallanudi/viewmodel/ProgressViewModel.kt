package com.nallanudi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nallanudi.repository.WordRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProgressUiState(
    val totalWords: Int = 0,
    val learnedCount: Int = 0,
    val accuracy: Float = 0f,
    val totalCorrect: Int = 0,
    val totalWrong: Int = 0,
    val wordsForReview: Int = 0,
    val streak: Int = 0,
    val isLoading: Boolean = true,
)

class ProgressViewModel(private val repository: WordRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getTotalCount().collect { total ->
                _uiState.update { it.copy(totalWords = total) }
            }
        }
        viewModelScope.launch {
            repository.getLearnedCount().collect { learned ->
                _uiState.update { it.copy(learnedCount = learned) }
            }
        }
        viewModelScope.launch {
            repository.getAccuracyStats().collect { stats ->
                if (stats != null) {
                    val total = stats.totalCorrect + stats.totalWrong
                    val accuracy = if (total > 0) stats.totalCorrect.toFloat() / total else 0f
                    _uiState.update {
                        it.copy(accuracy = accuracy, totalCorrect = stats.totalCorrect, totalWrong = stats.totalWrong, isLoading = false)
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
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
