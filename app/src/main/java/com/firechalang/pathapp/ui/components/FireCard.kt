package com.firechalang.pathapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.firechalang.pathapp.ui.theme.*

@Composable
fun FireCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else Modifier
            ),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundCard
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            content()
        }
    }
}

@Composable
fun FireGradientCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        FireRed.copy(alpha = 0.3f),
                        FireOrange.copy(alpha = 0.2f)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(FireRed, FireOrange)
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else Modifier
            )
            .padding(20.dp)
    ) {
        content()
    }
}

@Composable
fun StatsCard(
    title: String,
    value: String,
    icon: String,
    modifier: Modifier = Modifier,
    gradient: Boolean = false
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (gradient) {
                    Brush.linearGradient(
                        colors = listOf(
                            FireRed.copy(alpha = 0.2f),
                            FireOrange.copy(alpha = 0.1f)
                        )
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(BackgroundCard, BackgroundCard)
                    )
                }
            )
            .border(
                width = if (gradient) 1.dp else 0.dp,
                color = if (gradient) FireOrange.copy(alpha = 0.5f) else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = icon,
                style = androidx.compose.material3.MaterialTheme.typography.headlineLarge
            )
            Column {
                Text(
                    text = value,
                    style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                    color = if (gradient) FireOrange else TextPrimary
                )
                Text(
                    text = title,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }
    }
}

