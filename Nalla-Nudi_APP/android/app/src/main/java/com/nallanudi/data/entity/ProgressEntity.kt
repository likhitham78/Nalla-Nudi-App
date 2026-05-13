package com.nallanudi.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "progress",
    foreignKeys = [
        ForeignKey(
            entity = WordEntity::class,
            parentColumns = ["id"],
            childColumns = ["wordId"],
            onDelete = ForeignKey.Cascade,
        )
    ]
)
data class ProgressEntity(
    @PrimaryKey val wordId: String,
    val difficulty: String = "new",
    val lastReviewed: Long = 0L,
    val nextReview: Long = 0L,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
)
