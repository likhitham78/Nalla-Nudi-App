package com.nallanudi.data.dao

import androidx.room.*
import com.nallanudi.data.entity.ProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {

    @Query("SELECT * FROM progress WHERE wordId = :wordId")
    suspend fun getProgress(wordId: String): ProgressEntity?

    @Query("SELECT * FROM progress WHERE wordId = :wordId")
    fun getProgressFlow(wordId: String): Flow<ProgressEntity?>

    @Query("SELECT * FROM progress")
    fun getAllProgress(): Flow<List<ProgressEntity>>

    @Query("""
        SELECT p.* FROM progress p
        INNER JOIN words w ON p.wordId = w.id
        WHERE p.nextReview <= :timestamp
        ORDER BY p.nextReview ASC
    """)
    fun getWordsForReview(timestamp: Long): Flow<List<ProgressEntity>>

    @Query("SELECT COUNT(*) FROM progress WHERE difficulty = 'easy'")
    fun getLearnedCount(): Flow<Int>

    @Query("SELECT SUM(correctCount) as totalCorrect, SUM(wrongCount) as totalWrong FROM progress")
    fun getAccuracyStats(): Flow<AccuracyStats?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(progress: ProgressEntity)

    data class AccuracyStats(val totalCorrect: Int, val totalWrong: Int)
}
