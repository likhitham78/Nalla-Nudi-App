package com.nallanudi.data

import androidx.room.*
import com.nallanudi.models.GlossaryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface GlossaryDao {
    @Query("SELECT * FROM glossary_items ORDER BY term ASC")
    fun getAllItems(): Flow<List<GlossaryItem>>

    @Query("SELECT * FROM glossary_items WHERE isBookmarked = 1")
    fun getBookmarkedItems(): Flow<List<GlossaryItem>>

    @Query("SELECT * FROM glossary_items WHERE lastSearchedAt IS NOT NULL ORDER BY lastSearchedAt DESC LIMIT 10")
    fun getRecentSearches(): Flow<List<GlossaryItem>>

    @Query("""
        SELECT * FROM glossary_items 
        WHERE (
            term LIKE :query || '%' COLLATE NOCASE OR 
            term LIKE '%' || :query || '%' COLLATE NOCASE
        )
        AND (:category IS NULL OR category = :category COLLATE NOCASE)
        ORDER BY 
            CASE 
                WHEN term = :query COLLATE NOCASE THEN 1
                WHEN term LIKE :query || '%' COLLATE NOCASE THEN 2
                ELSE 3
            END,
            term ASC
    """)
    fun searchItems(query: String, category: String?): Flow<List<GlossaryItem>>

    @Update
    suspend fun updateItem(item: GlossaryItem)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<GlossaryItem>)
}

@Database(entities = [GlossaryItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun glossaryDao(): GlossaryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "glossary_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
