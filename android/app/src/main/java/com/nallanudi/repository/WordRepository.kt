package com.nallanudi.repository

import com.nallanudi.data.dao.ProgressDao
import com.nallanudi.data.dao.WordDao
import com.nallanudi.data.entity.ProgressEntity
import com.nallanudi.data.entity.WordEntity
import com.nallanudi.data.local.SeedData
import com.nallanudi.data.local.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class WordRepository(
    private val wordDao: WordDao,
    private val progressDao: ProgressDao,
    private val userPreferences: UserPreferences,
) {
    // --- Words ---
    fun getAllWords(): Flow<List<WordEntity>> = wordDao.getAllWords()
    fun getWordById(id: String): Flow<WordEntity?> = wordDao.getWordByIdFlow(id)
    suspend fun getWordByIdDirect(id: String): WordEntity? = wordDao.getWordById(id)
    fun searchWords(query: String, subject: String): Flow<List<WordEntity>> = wordDao.searchWords(query, subject)
    fun getSavedWords(): Flow<List<WordEntity>> = wordDao.getSavedWords()
    fun getWordsBySubject(subject: String): Flow<List<WordEntity>> = wordDao.getWordsBySubject(subject)
    fun getSavedCount(): Flow<Int> = wordDao.getSavedCount()
    fun getTotalCount(): Flow<Int> = wordDao.getTotalCount()

    suspend fun toggleSaved(wordId: String) {
        val word = wordDao.getWordById(wordId) ?: return
        wordDao.setSaved(wordId, !word.isSaved)
    }

    suspend fun setSaved(wordId: String, saved: Boolean) = wordDao.setSaved(wordId, saved)

    fun getWordOfTheDay(): Flow<WordEntity?> = wordDao.getAllWords().map { words ->
        if (words.isEmpty()) null
        else {
            val dayOfYear = LocalDate.now().dayOfYear
            words[dayOfYear % words.size]
        }
    }

    // --- Progress / Spaced Repetition ---
    fun getProgress(wordId: String): Flow<ProgressEntity?> = progressDao.getProgressFlow(wordId)
    fun getAllProgress(): Flow<List<ProgressEntity>> = progressDao.getAllProgress()
    fun getWordsForReview(timestamp: Long): Flow<List<ProgressEntity>> = progressDao.getWordsForReview(timestamp)
    fun getLearnedCount(): Flow<Int> = progressDao.getLearnedCount()
    fun getAccuracyStats(): Flow<ProgressDao.AccuracyStats?> = progressDao.getAccuracyStats()

    suspend fun recordFlashcardResult(wordId: String, difficulty: String) {
        val now = System.currentTimeMillis()
        val oneDayMs = 86_400_000L
        val existing = progressDao.getProgress(wordId)

        val (correctDelta, wrongDelta) = when (difficulty) {
            "easy" -> 1 to 0
            "hard" -> 0 to 0
            else -> 0 to 1
        }

        val nextReview = when (difficulty) {
            "easy" -> now + (3 * oneDayMs)
            "hard" -> now + oneDayMs
            else -> now
        }

        progressDao.upsert(ProgressEntity(
            wordId = wordId,
            difficulty = difficulty,
            lastReviewed = now,
            nextReview = nextReview,
            correctCount = (existing?.correctCount ?: 0) + correctDelta,
            wrongCount = (existing?.wrongCount ?: 0) + wrongDelta,
        ))
    }

    suspend fun resetProgress() {
        val allProgress = progressDao.getAllProgress().first()
        allProgress.forEach { progressDao.deleteProgress(it.wordId) }
        userPreferences.resetAll()
    }

    // --- User Preferences ---
    val streak: Flow<Int> = userPreferences.streak
    val soundEnabled: Flow<Boolean> = userPreferences.soundEnabled
    val onboardingCompleted: Flow<Boolean> = userPreferences.onboardingCompleted

    suspend fun updateStreak() = userPreferences.updateStreak()
    suspend fun setSoundEnabled(enabled: Boolean) = userPreferences.setSoundEnabled(enabled)
    suspend fun setOnboardingCompleted() = userPreferences.setOnboardingCompleted()

    // --- Seed ---
    suspend fun seedIfNeeded() {
        val count = wordDao.getTotalCount().first()
        if (count == 0) {
            wordDao.insertAll(SeedData.getWords())
        }
    }
}
