package com.nallanudi

import android.app.Application
import com.nallanudi.data.local.AppDatabase
import com.nallanudi.data.local.UserPreferences
import com.nallanudi.repository.WordRepository

class NallaNudiApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val userPreferences by lazy { UserPreferences(this) }
    val repository by lazy { WordRepository(database.wordDao(), database.progressDao(), userPreferences) }
}
