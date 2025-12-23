package com.firechalang.pathapp.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firechalang.pathapp.ui.components.FireButton
import com.firechalang.pathapp.ui.theme.*
import com.firechalang.pathapp.viewmodel.ChallengeViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Color

@Composable
fun DailyCheckInScreen(
    challengeId: Long,
    viewModel: ChallengeViewModel,
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(challengeId) {
        viewModel.selectChallenge(challengeId)
    }

    val challenge by viewModel.selectedChallenge.collectAsStateWithLifecycle()
    var showSuccess by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "fire")
    val fireScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BackgroundDark,
                        DarkRed.copy(alpha = 0.3f),
                        BackgroundDark
                    )
                )
            )
    ) {
        if (!showSuccess) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🔥",
                    fontSize = 120.sp,
                    modifier = Modifier.scale(fireScale)
                )

                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = challenge?.name ?: "",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Did you complete today's challenge?",
                    fontSize = 18.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(48.dp))

                FireButton(
                    text = "YES",
                    onClick = {
                        viewModel.checkInToday(challengeId, true)
                        showSuccess = true
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                FireButton(
                    text = "NO",
                    onClick = {
                        viewModel.checkInToday(challengeId, false)
                        onNavigateBack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isSecondary = true
                )
            }
        } else {
            SuccessAnimation(onNavigateBack = onNavigateBack)
        }
    }
}

@Composable
fun SuccessAnimation(onNavigateBack: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2000)
        onNavigateBack()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.scale(scale)
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                SuccessGreen.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✅",
                    fontSize = 100.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Great Job!",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = SuccessGreen
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Challenge completed for today",
                fontSize = 18.sp,
                color = TextSecondary
            )
        }
    }
}

