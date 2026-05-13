package com.nallanudi.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "glossary_items")
data class GlossaryItem(
    @PrimaryKey
    val id: String,
    val term: String,
    val kannadaTerm: String,
    val kannadaDefinition: String,
    val category: String,
    val pronunciation: String,
    val example: String,
    val isBookmarked: Boolean = false,
    val lastSearchedAt: Long? = null
)
