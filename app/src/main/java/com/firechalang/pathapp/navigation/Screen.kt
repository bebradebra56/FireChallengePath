package com.firechalang.pathapp.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Challenges : Screen("challenges")
    object Stats : Screen("stats")
    object Settings : Screen("settings")
    object AddChallenge : Screen("add_challenge")
    object EditChallenge : Screen("edit_challenge/{challengeId}") {
        fun createRoute(challengeId: Long) = "edit_challenge/$challengeId"
    }
    object ChallengeDetail : Screen("challenge_detail/{challengeId}") {
        fun createRoute(challengeId: Long) = "challenge_detail/$challengeId"
    }
    object DailyCheckIn : Screen("daily_checkin/{challengeId}") {
        fun createRoute(challengeId: Long) = "daily_checkin/$challengeId"
    }
    object Path : Screen("path")
    object Calendar : Screen("calendar")
    object Achievements : Screen("achievements")
    object Reminders : Screen("reminders")
}

