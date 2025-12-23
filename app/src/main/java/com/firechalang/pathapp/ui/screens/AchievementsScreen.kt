package com.firechalang.pathapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firechalang.pathapp.data.model.Achievement
import com.firechalang.pathapp.data.model.AchievementsList
import com.firechalang.pathapp.ui.theme.*
import com.firechalang.pathapp.viewmodel.ChallengeViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.draw.alpha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    viewModel: ChallengeViewModel,
    onNavigateBack: () -> Unit
) {
    val activeChallengesCount by viewModel.activeChallengesCount.collectAsStateWithLifecycle()
    val completedChallengesCount by viewModel.completedChallengesCount.collectAsStateWithLifecycle()
    val currentStreak by viewModel.currentStreak.collectAsStateWithLifecycle()
    val allCheckIns by viewModel.getAllCompletedCheckIns().collectAsStateWithLifecycle(initialValue = emptyList())

    val unlockedAchievements = remember(activeChallengesCount, completedChallengesCount, currentStreak, allCheckIns) {
        AchievementsList.achievements.map { achievement ->
            val isUnlocked = when (achievement.id) {
                "first_challenge" -> (activeChallengesCount + completedChallengesCount) > 0
                "week_streak" -> currentStreak >= 7
                "month_streak" -> currentStreak >= 30
                "complete_5" -> completedChallengesCount >= 5
                "complete_10" -> completedChallengesCount >= 10
                "streak_50" -> currentStreak >= 50
                else -> false
            }
            achievement.copy(isUnlocked = isUnlocked)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                DarkRed.copy(alpha = 0.3f),
                                BackgroundDark
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Achievements",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Progress
                val unlockedCount = unlockedAchievements.count { it.isUnlocked }
                val totalCount = unlockedAchievements.size

                Text(
                    text = "$unlockedCount / $totalCount Unlocked",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = FireOrange
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Achievements Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(unlockedAchievements) { achievement ->
                        AchievementCard(achievement = achievement)
                    }
                }
            }
        }
    }
}

@Composable
fun AchievementCard(achievement: Achievement) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (achievement.isUnlocked) {
                    Brush.linearGradient(
                        colors = listOf(
                            FireRed.copy(alpha = 0.3f),
                            FireOrange.copy(alpha = 0.2f)
                        )
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(
                            BackgroundCard,
                            BackgroundCard
                        )
                    )
                }
            )
            .border(
                width = if (achievement.isUnlocked) 2.dp else 1.dp,
                color = if (achievement.isUnlocked) FireOrange else TextTertiary,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = achievement.icon,
                fontSize = 48.sp,
                modifier = Modifier.alpha(if (achievement.isUnlocked) 1f else 0.3f)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = achievement.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (achievement.isUnlocked) FireOrange else TextSecondary,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = achievement.description,
                fontSize = 12.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
            
            if (achievement.isUnlocked) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "✓ Unlocked",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SuccessGreen
                )
            }
        }
    }
}

