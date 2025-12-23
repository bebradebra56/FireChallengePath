package com.firechalang.pathapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.firechalang.pathapp.data.database.AppDatabase
import com.firechalang.pathapp.data.datastore.PreferencesManager
import com.firechalang.pathapp.data.repository.ChallengeRepository

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = AppDatabase.getDatabase(context)
        val repository = ChallengeRepository(
            database.challengeDao(),
            database.dailyCheckInDao()
        )
        val preferencesManager = PreferencesManager(context)

        return when {
            modelClass.isAssignableFrom(ChallengeViewModel::class.java) -> {
                ChallengeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(preferencesManager, repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

