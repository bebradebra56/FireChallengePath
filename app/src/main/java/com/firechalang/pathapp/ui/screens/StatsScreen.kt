package com.firechalang.pathapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firechalang.pathapp.ui.components.FireCard
import com.firechalang.pathapp.ui.components.StatsCard
import com.firechalang.pathapp.ui.theme.*
import com.firechalang.pathapp.viewmodel.ChallengeViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun StatsScreen(
    viewModel: ChallengeViewModel
) {
    val activeChallengesCount by viewModel.activeChallengesCount.collectAsStateWithLifecycle()
    val completedChallengesCount by viewModel.completedChallengesCount.collectAsStateWithLifecycle()
    val currentStreak by viewModel.currentStreak.collectAsStateWithLifecycle()
    val allCheckIns by viewModel.getAllCompletedCheckIns().collectAsStateWithLifecycle(initialValue = emptyList())
    
    val totalCheckIns = allCheckIns.size
    val thisWeekCheckIns = remember(allCheckIns) {
        val weekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
        allCheckIns.count { it.date >= weekAgo && it.completed }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
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
                    .padding(24.dp)
            ) {
                Text(
                    text = "Your Statistics",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // Overview Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatsCard(
                        title = "Active",
                        value = "$activeChallengesCount",
                        icon = "🔥",
                        modifier = Modifier.weight(1f),
                        gradient = true
                    )
                    StatsCard(
                        title = "Completed",
                        value = "$completedChallengesCount",
                        icon = "✅",
                        modifier = Modifier.weight(1f),
                        gradient = true
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatsCard(
                        title = "Current Streak",
                        value = "$currentStreak",
                        icon = "⚡",
                        modifier = Modifier.weight(1f)
                    )
                    StatsCard(
                        title = "Total Check-ins",
                        value = "$totalCheckIns",
                        icon = "📊",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Activity Card
                FireCard {
                    Column {
                        Text(
                            text = "Activity",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        ActivityRow(
                            label = "This Week",
                            value = "$thisWeekCheckIns check-ins",
                            icon = "📅"
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        ActivityRow(
                            label = "Total Challenges",
                            value = "${activeChallengesCount + completedChallengesCount}",
                            icon = "🎯"
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        ActivityRow(
                            label = "Success Rate",
                            value = if (totalCheckIns > 0) {
                                "${(completedChallengesCount * 100 / (activeChallengesCount + completedChallengesCount).coerceAtLeast(1))}%"
                            } else "0%",
                            icon = "📈"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Motivation Card
                FireCard {
                    Column {
                        Text(
                            text = "🏆",
                            fontSize = 48.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Keep pushing forward!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = FireOrange
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Every challenge you complete brings you closer to your goals.",
                            fontSize = 14.sp,
                            color = TextSecondary,
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ActivityRow(
    label: String,
    value: String,
    icon: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Text(
                text = icon,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                fontSize = 16.sp,
                color = TextSecondary
            )
        }
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = FireOrange
        )
    }
}

