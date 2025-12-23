package com.firechalang.pathapp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firechalang.pathapp.ui.theme.*

@Composable
fun FireProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    showPercentage: Boolean = true,
    height: Int = 12
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1000, easing = EaseOutCubic),
        label = "progress"
    )

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp)
                .clip(RoundedCornerShape(height.dp / 2))
                .background(BackgroundCardElevated)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .clip(RoundedCornerShape(height.dp / 2))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                FireRed,
                                FireOrange,
                                FireYellow
                            )
                        )
                    )
            )
        }
        
        if (showPercentage) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun CircularFireProgress(
    progress: Float,
    text: String,
    modifier: Modifier = Modifier,
    size: Int = 120
) {
    Box(
        modifier = modifier.size(size.dp),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.CircularProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxSize(),
            color = FireOrange,
            strokeWidth = 8.dp,
            trackColor = BackgroundCardElevated,
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${(progress * 100).toInt()}%",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = FireOrange
            )
            Text(
                text = text,
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun StreakIndicator(
    streak: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "flame")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "🔥",
            fontSize = 32.sp,
            modifier = Modifier.offset(y = (-2).dp)
        )
        Column {
            Text(
                text = "$streak days",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = FireOrange.copy(alpha = alpha)
            )
            Text(
                text = "Current Streak",
                fontSize = 14.sp,
                color = TextSecondary
            )
        }
    }
}

