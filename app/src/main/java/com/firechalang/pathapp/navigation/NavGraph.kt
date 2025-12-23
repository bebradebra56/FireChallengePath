package com.firechalang.pathapp.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.firechalang.pathapp.ui.components.BottomNavigationBar
import com.firechalang.pathapp.ui.screens.*
import com.firechalang.pathapp.ui.theme.*
import com.firechalang.pathapp.viewmodel.ChallengeViewModel
import com.firechalang.pathapp.viewmodel.SettingsViewModel

@Composable
fun NavGraph(
    challengeViewModel: ChallengeViewModel,
    settingsViewModel: SettingsViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Challenges.route,
        Screen.Stats.route,
        Screen.Settings.route
    )

    Scaffold(
        containerColor = BackgroundDark,
        contentColor = TextPrimary,
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            navController = navController,
            startDestination = Screen.Home.route
        ) {

            // Main screens with bottom nav
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = challengeViewModel,
                    onNavigateToAddChallenge = {
                        navController.navigate(Screen.AddChallenge.route)
                    },
                    onNavigateToChallenges = {
                        navController.navigate(Screen.Challenges.route)
                    }
                )
            }

            composable(Screen.Challenges.route) {
                ChallengesScreen(
                    viewModel = challengeViewModel,
                    onNavigateToAddChallenge = {
                        navController.navigate(Screen.AddChallenge.route)
                    },
                    onNavigateToChallengeDetail = { challengeId ->
                        navController.navigate(Screen.ChallengeDetail.createRoute(challengeId))
                    }
                )
            }

            composable(Screen.Stats.route) {
                StatsScreen(
                    viewModel = challengeViewModel
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    viewModel = settingsViewModel,
                    onNavigateToReminders = {
                        navController.navigate(Screen.Reminders.route)
                    },
                    onNavigateToAchievements = {
                        navController.navigate(Screen.Achievements.route)
                    },
                    onNavigateToPath = {
                        navController.navigate(Screen.Path.route)
                    },
                    onNavigateToCalendar = {
                        navController.navigate(Screen.Calendar.route)
                    }
                )
            }

            // Add Challenge
            composable(Screen.AddChallenge.route) {
                AddChallengeScreen(
                    viewModel = challengeViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Challenge Detail
            composable(
                route = Screen.ChallengeDetail.route,
                arguments = listOf(navArgument("challengeId") { type = NavType.LongType })
            ) { backStackEntry ->
                val challengeId = backStackEntry.arguments?.getLong("challengeId") ?: 0L
                ChallengeDetailScreen(
                    challengeId = challengeId,
                    viewModel = challengeViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDailyCheckIn = { id ->
                        navController.navigate(Screen.DailyCheckIn.createRoute(id))
                    }
                )
            }

            // Daily Check-In
            composable(
                route = Screen.DailyCheckIn.route,
                arguments = listOf(navArgument("challengeId") { type = NavType.LongType })
            ) { backStackEntry ->
                val challengeId = backStackEntry.arguments?.getLong("challengeId") ?: 0L
                DailyCheckInScreen(
                    challengeId = challengeId,
                    viewModel = challengeViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Path
            composable(Screen.Path.route) {
                PathScreen(
                    viewModel = challengeViewModel
                )
            }

            // Calendar
            composable(Screen.Calendar.route) {
                CalendarScreen(
                    viewModel = challengeViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Achievements
            composable(Screen.Achievements.route) {
                AchievementsScreen(
                    viewModel = challengeViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Reminders
            composable(Screen.Reminders.route) {
                RemindersScreen(
                    viewModel = settingsViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
