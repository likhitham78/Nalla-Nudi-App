package com.nallanudi.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey val id: String,
    val english: String,
    val kannada: String,
    val explanation: String,
    val example: String,
    val subject: String,
    val isSaved: Boolean = false,
)
