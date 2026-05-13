package com.nallanudi.data.dao

import androidx.room.*
import com.nallanudi.data.entity.WordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Query("SELECT * FROM words ORDER BY english ASC")
    fun getAllWords(): Flow<List<WordEntity>>

    @Query("SELECT * FROM words WHERE id = :id")
    suspend fun getWordById(id: String): WordEntity?

    @Query("SELECT * FROM words WHERE id = :id")
    fun getWordByIdFlow(id: String): Flow<WordEntity?>

    @Query("""
        SELECT * FROM words
        WHERE (english LIKE '%' || :query || '%' OR kannada LIKE '%' || :query || '%')
        AND (:subject = '' OR subject = :subject)
        ORDER BY english ASC
    """)
    fun searchWords(query: String, subject: String): Flow<List<WordEntity>>

    @Query("SELECT * FROM words WHERE isSaved = 1 ORDER BY english ASC")
    fun getSavedWords(): Flow<List<WordEntity>>

    @Query("SELECT * FROM words WHERE subject = :subject ORDER BY english ASC")
    fun getWordsBySubject(subject: String): Flow<List<WordEntity>>

    @Query("SELECT * FROM words ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomWord(): WordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(words: List<WordEntity>)

    @Query("UPDATE words SET isSaved = :saved WHERE id = :id")
    suspend fun setSaved(id: String, saved: Boolean)

    @Query("SELECT COUNT(*) FROM words WHERE isSaved = 1")
    fun getSavedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM words")
    fun getTotalCount(): Flow<Int>
}
