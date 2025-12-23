package com.firechalang.pathapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firechalang.pathapp.data.datastore.PreferencesManager
import com.firechalang.pathapp.data.repository.ChallengeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesManager: PreferencesManager,
    private val repository: ChallengeRepository
) : ViewModel() {

    val themeMode: StateFlow<String> = preferencesManager.themeMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "fire")

    val reminderEnabled: StateFlow<Boolean> = preferencesManager.reminderEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val reminderTime: StateFlow<Pair<Int, Int>> = preferencesManager.reminderTime
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Pair(9, 0))

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            preferencesManager.setThemeMode(mode)
        }
    }

    fun setReminderEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setReminderEnabled(enabled)
        }
    }

    fun setReminderTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            preferencesManager.setReminderTime(hour, minute)
        }
    }

    fun resetAllProgress() {
        viewModelScope.launch {
            repository.deleteAllData()
            preferencesManager.resetAllData()
        }
    }
}

