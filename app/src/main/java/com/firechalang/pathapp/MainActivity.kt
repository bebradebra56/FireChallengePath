package com.firechalang.pathapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.firechalang.pathapp.navigation.NavGraph
import com.firechalang.pathapp.navigation.Screen
import com.firechalang.pathapp.ui.theme.FireChallengePathTheme
import com.firechalang.pathapp.viewmodel.ChallengeViewModel
import com.firechalang.pathapp.viewmodel.SettingsViewModel
import com.firechalang.pathapp.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var challengeViewModel: ChallengeViewModel
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val factory = ViewModelFactory(applicationContext)
        challengeViewModel = ViewModelProvider(this, factory)[ChallengeViewModel::class.java]
        settingsViewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]

        setContent {
            FireChallengePathTheme {
                NavGraph(
                    challengeViewModel = challengeViewModel,
                    settingsViewModel = settingsViewModel
                )
            }
        }
    }
}