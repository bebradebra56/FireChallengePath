package com.firechalang.pathapp.data.model

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null
)

object AchievementsList {
    val achievements = listOf(
        Achievement("first_challenge", "First Challenge", "Create your first challenge", "🎯"),
        Achievement("week_streak", "Week Warrior", "Complete 7 days in a row", "🔥"),
        Achievement("month_streak", "Monthly Master", "Complete 30 days in a row", "💪"),
        Achievement("complete_5", "Challenge Champion", "Complete 5 challenges", "🏆"),
        Achievement("complete_10", "Path Legend", "Complete 10 challenges", "👑"),
        Achievement("streak_50", "Unstoppable", "Reach a 50-day streak", "⚡")
    )
}

