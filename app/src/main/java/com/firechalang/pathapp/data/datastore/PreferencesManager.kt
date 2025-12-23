package com.firechalang.pathapp.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {
    
    companion object {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val REMINDER_ENABLED = booleanPreferencesKey("reminder_enabled")
        val REMINDER_TIME_HOUR = intPreferencesKey("reminder_time_hour")
        val REMINDER_TIME_MINUTE = intPreferencesKey("reminder_time_minute")
    }
    
    val themeMode: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[THEME_MODE] ?: "fire"
    }
    
    val reminderEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[REMINDER_ENABLED] ?: false
    }
    
    val reminderTime: Flow<Pair<Int, Int>> = context.dataStore.data.map { preferences ->
        val hour = preferences[REMINDER_TIME_HOUR] ?: 9
        val minute = preferences[REMINDER_TIME_MINUTE] ?: 0
        Pair(hour, minute)
    }
    
    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE] = mode
        }
    }
    
    suspend fun setReminderEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[REMINDER_ENABLED] = enabled
        }
    }
    
    suspend fun setReminderTime(hour: Int, minute: Int) {
        context.dataStore.edit { preferences ->
            preferences[REMINDER_TIME_HOUR] = hour
            preferences[REMINDER_TIME_MINUTE] = minute
        }
    }
    
    suspend fun resetAllData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

