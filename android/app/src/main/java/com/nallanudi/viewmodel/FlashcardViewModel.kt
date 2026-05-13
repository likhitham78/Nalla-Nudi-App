package com.nallanudi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nallanudi.data.entity.WordEntity
import com.nallanudi.repository.WordRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class FlashcardUiState(
    val deck: List<WordEntity> = emptyList(),
    val currentIndex: Int = 0,
    val isFlipped: Boolean = false,
    val isFinished: Boolean = false,
    val isDeckEmpty: Boolean = false,
    val knownCount: Int = 0,
    val hardCount: Int = 0,
    val againCount: Int = 0,
    val mode: FlashcardMode = FlashcardMode.ALL,
)

enum class FlashcardMode { ALL, SAVED, REVIEW }

class FlashcardViewModel(private val repository: WordRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(FlashcardUiState())
    val uiState: StateFlow<FlashcardUiState> = _uiState.asStateFlow()

    fun loadDeck(mode: FlashcardMode) {
        _uiState.update {
            it.copy(
                mode = mode, currentIndex = 0, isFinished = false,
                isFlipped = false, isDeckEmpty = false,
                knownCount = 0, hardCount = 0, againCount = 0,
            )
        }

        viewModelScope.launch {
            val flow: Flow<List<WordEntity>> = when (mode) {
                FlashcardMode.SAVED -> repository.getSavedWords()
                FlashcardMode.REVIEW -> {
                    repository.getWordsForReview(System.currentTimeMillis()).map { progressList ->
                        progressList.mapNotNull { repository.getWordByIdDirect(it.wordId) }
                    }
                }
                FlashcardMode.ALL -> repository.getAllWords()
            }

            flow.collect { words ->
                val shuffled = words.shuffled()
                _uiState.update {
                    it.copy(deck = shuffled, isDeckEmpty = shuffled.isEmpty())
                }
            }
        }
    }

    fun flipCard() { _uiState.update { it.copy(isFlipped = !it.isFlipped) } }

    fun markEasy() {
        val state = _uiState.value
        val wordId = state.deck.getOrNull(state.currentIndex)?.id ?: return
        viewModelScope.launch { repository.recordFlashcardResult(wordId, "easy") }
        advanceCard(state.knownCount + 1, state.hardCount, state.againCount)
    }

    fun markHard() {
        val state = _uiState.value
        val wordId = state.deck.getOrNull(state.currentIndex)?.id ?: return
        viewModelScope.launch { repository.recordFlashcardResult(wordId, "hard") }
        advanceCard(state.knownCount, state.hardCount + 1, state.againCount)
    }

    fun markAgain() {
        val state = _uiState.value
        val wordId = state.deck.getOrNull(state.currentIndex)?.id ?: return
        viewModelScope.launch { repository.recordFlashcardResult(wordId, "again") }
        advanceCard(state.knownCount, state.hardCount, state.againCount + 1)
    }

    private fun advanceCard(known: Int, hard: Int, again: Int) {
        val state = _uiState.value
        val nextIndex = state.currentIndex + 1
        if (nextIndex >= state.deck.size) {
            _uiState.update { it.copy(isFinished = true, knownCount = known, hardCount = hard, againCount = again) }
        } else {
            _uiState.update {
                it.copy(currentIndex = nextIndex, isFlipped = false, knownCount = known, hardCount = hard, againCount = again)
            }
        }
    }

    fun restart() {
        _uiState.update {
            it.copy(currentIndex = 0, isFlipped = false, isFinished = false, knownCount = 0, hardCount = 0, againCount = 0)
        }
    }
}
