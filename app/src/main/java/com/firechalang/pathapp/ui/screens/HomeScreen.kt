package com.firechalang.pathapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firechalang.pathapp.ui.components.FireButton
import com.firechalang.pathapp.ui.components.StatsCard
import com.firechalang.pathapp.ui.theme.*
import com.firechalang.pathapp.viewmodel.ChallengeViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

val motivationalQuotes = listOf(
    "Every step is a risk worth taking",
    "Challenge yourself, change yourself",
    "Your path starts with a single step",
    "Fire burns brightest in the darkest moments",
    "Today's challenge is tomorrow's strength",
    "Be bold, be brave, be on fire",
    "The path forward is paved with courage",
    "Rise from the ashes stronger"
)

@Composable
fun HomeScreen(
    viewModel: ChallengeViewModel,
    onNavigateToAddChallenge: () -> Unit,
    onNavigateToChallenges: () -> Unit
) {
    val activeChallengesCount by viewModel.activeChallengesCount.collectAsStateWithLifecycle()
    val completedChallengesCount by viewModel.completedChallengesCount.collectAsStateWithLifecycle()
    val currentStreak by viewModel.currentStreak.collectAsStateWithLifecycle()
    
    val randomQuote = remember { motivationalQuotes.random() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BackgroundDark,
                        DarkRed.copy(alpha = 0.2f),
                        BackgroundDark
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
            
            // Header with gradient
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                FireRed.copy(alpha = 0.15f),
                                FireOrange.copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Text(
                    text = "🔥 Your Path Today",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = randomQuote,
                    fontSize = 16.sp,
                    color = FireOrange,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 22.sp
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Stats Cards
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
                    title = "Streak",
                    value = "$currentStreak",
                    icon = "⚡",
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
                    title = "Completed",
                    value = "$completedChallengesCount",
                    icon = "✅",
                    modifier = Modifier.weight(1f)
                )
                
                StatsCard(
                    title = "Total",
                    value = "${activeChallengesCount + completedChallengesCount}",
                    icon = "📊",
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Buttons
            FireButton(
                text = "Start New Challenge",
                onClick = onNavigateToAddChallenge,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            FireButton(
                text = "View All Challenges",
                onClick = onNavigateToChallenges,
                modifier = Modifier.fillMaxWidth(),
                isSecondary = true
            )
            
            Spacer(modifier = Modifier.height(32.dp))
    }
}

