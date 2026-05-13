package com.nallanudi.data

import com.nallanudi.models.GlossaryItem
import kotlinx.coroutines.flow.Flow

class GlossaryRepository(private val glossaryDao: GlossaryDao) {
    val allItems: Flow<List<GlossaryItem>> = glossaryDao.getAllItems()
    val bookmarkedItems: Flow<List<GlossaryItem>> = glossaryDao.getBookmarkedItems()
    val recentSearches: Flow<List<GlossaryItem>> = glossaryDao.getRecentSearches()

    fun searchItems(query: String, category: String?): Flow<List<GlossaryItem>> {
        return glossaryDao.searchItems(query, category)
    }

    suspend fun toggleBookmark(item: GlossaryItem) {
        glossaryDao.updateItem(item.copy(isBookmarked = !item.isBookmarked))
    }

    suspend fun markAsSearched(item: GlossaryItem) {
        glossaryDao.updateItem(item.copy(lastSearchedAt = System.currentTimeMillis()))
    }

    suspend fun insertAll(items: List<GlossaryItem>) {
        glossaryDao.insertAll(items)
    }
}
