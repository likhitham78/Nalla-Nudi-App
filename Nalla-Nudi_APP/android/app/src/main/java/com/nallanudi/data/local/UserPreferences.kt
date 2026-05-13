package com.nallanudi.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.time.LocalDate

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "nallanudi_settings")

class UserPreferences(private val context: Context) {

    private object Keys {
        val STREAK = intPreferencesKey("streak")
        val LAST_OPENED_DATE = stringPreferencesKey("last_opened_date")
        val TOTAL_LEARNED = intPreferencesKey("total_learned")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    val streak: Flow<Int> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[Keys.STREAK] ?: 0 }

    val lastOpenedDate: Flow<String> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[Keys.LAST_OPENED_DATE] ?: "" }

    val totalLearned: Flow<Int> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[Keys.TOTAL_LEARNED] ?: 0 }

    val soundEnabled: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[Keys.SOUND_ENABLED] ?: true }

    val onboardingCompleted: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[Keys.ONBOARDING_COMPLETED] ?: false }

    suspend fun updateStreak() {
        val today = LocalDate.now().toString()
        context.dataStore.edit { prefs ->
            val lastDate = prefs[Keys.LAST_OPENED_DATE] ?: ""
            val currentStreak = prefs[Keys.STREAK] ?: 0

            when {
                lastDate == today -> { /* already opened today */ }
                lastDate == LocalDate.now().minusDays(1).toString() -> {
                    prefs[Keys.STREAK] = currentStreak + 1
                }
                else -> {
                    prefs[Keys.STREAK] = 1
                }
            }
            prefs[Keys.LAST_OPENED_DATE] = today
        }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.SOUND_ENABLED] = enabled }
    }

    suspend fun setOnboardingCompleted() {
        context.dataStore.edit { it[Keys.ONBOARDING_COMPLETED] = true }
    }

    suspend fun setTotalLearned(count: Int) {
        context.dataStore.edit { it[Keys.TOTAL_LEARNED] = count }
    }

    suspend fun resetAll() {
        context.dataStore.edit { prefs ->
            prefs[Keys.STREAK] = 0
            prefs[Keys.TOTAL_LEARNED] = 0
        }
    }
}
