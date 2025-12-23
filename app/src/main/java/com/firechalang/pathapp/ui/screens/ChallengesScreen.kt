package com.firechalang.pathapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firechalang.pathapp.data.model.Challenge
import com.firechalang.pathapp.data.model.ChallengeType
import com.firechalang.pathapp.ui.components.FireCard
import com.firechalang.pathapp.ui.components.FireProgressBar
import com.firechalang.pathapp.ui.theme.*
import com.firechalang.pathapp.viewmodel.ChallengeViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChallengesScreen(
    viewModel: ChallengeViewModel,
    onNavigateToAddChallenge: () -> Unit,
    onNavigateToChallengeDetail: (Long) -> Unit
) {
    val challenges by viewModel.allActiveChallenges.collectAsStateWithLifecycle()

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
                    text = "Your Challenges",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            if (challenges.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🎯",
                            fontSize = 80.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Active Challenges",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Start your journey by creating a new challenge",
                            fontSize = 16.sp,
                            color = TextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(challenges) { challenge ->
                        ChallengeCard(
                            challenge = challenge,
                            onClick = { onNavigateToChallengeDetail(challenge.id) }
                        )
                    }
                }
            }
        }

        // FAB
        FloatingActionButton(
            onClick = onNavigateToAddChallenge,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = FireOrange,
            contentColor = TextPrimary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Challenge",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun ChallengeCard(
    challenge: Challenge,
    onClick: () -> Unit
) {
    FireCard(
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                FireRed.copy(alpha = 0.3f),
                                FireOrange.copy(alpha = 0.2f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (challenge.type) {
                        ChallengeType.DAILY -> "🔥"
                        ChallengeType.WEEKLY -> "📅"
                        ChallengeType.ONE_TIME -> "🎯"
                    },
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = challenge.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when (challenge.type) {
                        ChallengeType.DAILY -> "Daily Challenge"
                        ChallengeType.WEEKLY -> "Weekly Challenge"
                        ChallengeType.ONE_TIME -> "One-Time Challenge"
                    },
                    fontSize = 14.sp,
                    color = TextSecondary
                )
                
                if (challenge.duration != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    val startDate = Date(challenge.startDate)
                    val calendar = Calendar.getInstance()
                    calendar.time = startDate
                    calendar.add(Calendar.DAY_OF_YEAR, challenge.duration)
                    val endDate = calendar.time
                    
                    val daysPassed = ((System.currentTimeMillis() - challenge.startDate) / (24 * 60 * 60 * 1000)).toInt()
                    val progress = (daysPassed.toFloat() / challenge.duration.toFloat()).coerceIn(0f, 1f)
                    
                    FireProgressBar(
                        progress = progress,
                        showPercentage = true,
                        height = 8
                    )
                }
            }
        }
    }
}

