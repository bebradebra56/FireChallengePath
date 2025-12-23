package com.firechalang.pathapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firechalang.pathapp.data.model.ChallengeType
import com.firechalang.pathapp.ui.components.FireButton
import com.firechalang.pathapp.ui.components.FireCard
import com.firechalang.pathapp.ui.components.FireProgressBar
import com.firechalang.pathapp.ui.components.StreakIndicator
import com.firechalang.pathapp.ui.theme.*
import com.firechalang.pathapp.viewmodel.ChallengeViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeDetailScreen(
    challengeId: Long,
    viewModel: ChallengeViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDailyCheckIn: (Long) -> Unit
) {
    LaunchedEffect(challengeId) {
        viewModel.selectChallenge(challengeId)
    }

    val challenge by viewModel.selectedChallenge.collectAsStateWithLifecycle()
    val currentStreak by viewModel.currentStreak.collectAsStateWithLifecycle()
    val longestStreak by viewModel.longestStreak.collectAsStateWithLifecycle()
    val checkIns by viewModel.checkIns.collectAsStateWithLifecycle()
    
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    val hasTodayCheckIn by viewModel.hasTodayCheckIn(challengeId).collectAsStateWithLifecycle(initialValue = false)

    challenge?.let { currentChallenge ->
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
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onNavigateBack) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = TextPrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Challenge Details",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = ErrorRed
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                ) {
                    // Challenge Header
                    FireCard {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = currentChallenge.name,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = when (currentChallenge.type) {
                                            ChallengeType.DAILY -> "Daily Challenge"
                                            ChallengeType.WEEKLY -> "Weekly Challenge"
                                            ChallengeType.ONE_TIME -> "One-Time Challenge"
                                        },
                                        fontSize = 16.sp,
                                        color = TextSecondary
                                    )
                                }
                                
                                Text(
                                    text = when (currentChallenge.type) {
                                        ChallengeType.DAILY -> "🔥"
                                        ChallengeType.WEEKLY -> "📅"
                                        ChallengeType.ONE_TIME -> "🎯"
                                    },
                                    fontSize = 48.sp
                                )
                            }
                            
                            if (currentChallenge.notes.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(16.dp))
                                HorizontalDivider(color = BackgroundCardElevated)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = currentChallenge.notes,
                                    fontSize = 14.sp,
                                    color = TextSecondary,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Streaks
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FireCard(modifier = Modifier.weight(1f)) {
                            Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                                Text(
                                    text = "🔥",
                                    fontSize = 48.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "$currentStreak",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FireOrange
                                )
                                Text(
                                    text = "Current Streak",
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                        
                        FireCard(modifier = Modifier.weight(1f)) {
                            Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                                Text(
                                    text = "⚡",
                                    fontSize = 48.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "$longestStreak",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentGold
                                )
                                Text(
                                    text = "Best Streak",
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Progress
                    if (currentChallenge.duration != null) {
                        FireCard {
                            Column {
                                Text(
                                    text = "Progress",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                val completedDays = checkIns.count { it.completed }
                                val progress = (completedDays.toFloat() / currentChallenge.duration.toFloat()).coerceIn(0f, 1f)
                                
                                FireProgressBar(
                                    progress = progress,
                                    showPercentage = true,
                                    height = 16
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "$completedDays / ${currentChallenge.duration} days completed",
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // Check-ins count
                    FireCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Total Check-ins",
                                    fontSize = 16.sp,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${checkIns.count { it.completed }}",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FireOrange
                                )
                            }
                            Text(
                                text = "✅",
                                fontSize = 48.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Action Button
                    if (!hasTodayCheckIn) {
                        FireButton(
                            text = "Mark Today as Done",
                            onClick = { onNavigateToDailyCheckIn(challengeId) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(SuccessGreen.copy(alpha = 0.2f))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "✅",
                                    fontSize = 24.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Today's challenge completed!",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SuccessGreen
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
        
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Challenge?") },
                text = { Text("This action cannot be undone. All progress will be lost.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteChallenge(currentChallenge)
                            onNavigateBack()
                        }
                    ) {
                        Text("Delete", color = ErrorRed)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

