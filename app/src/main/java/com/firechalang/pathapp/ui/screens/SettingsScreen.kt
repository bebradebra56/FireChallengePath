package com.firechalang.pathapp.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firechalang.pathapp.ui.components.FireCard
import com.firechalang.pathapp.ui.theme.*
import com.firechalang.pathapp.viewmodel.SettingsViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateToReminders: () -> Unit,
    onNavigateToAchievements: () -> Unit,
    onNavigateToPath: () -> Unit,
    onNavigateToCalendar: () -> Unit
) {
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    var showResetDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

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
                    text = "Settings",
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


                // Features Section
                Text(
                    text = "FEATURES",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                FireCard {
                    Column {
                        SettingItem(
                            icon = "🏆",
                            title = "Achievements",
                            subtitle = "View your badges",
                            onClick = onNavigateToAchievements
                        )
                        
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = BackgroundCardElevated
                        )
                        
                        SettingItem(
                            icon = "🛤️",
                            title = "Fire Path",
                            subtitle = "Your journey visualization",
                            onClick = onNavigateToPath
                        )
                        
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = BackgroundCardElevated
                        )
                        
                        SettingItem(
                            icon = "📅",
                            title = "Calendar",
                            subtitle = "View check-in history",
                            onClick = onNavigateToCalendar
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Data Section
                Text(
                    text = "DATA",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                FireCard {
                    SettingItem(
                        icon = "🗑️",
                        title = "Reset Progress",
                        subtitle = "Delete all data",
                        onClick = { showResetDialog = true },
                        isDestructive = true
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // About Section
                Text(
                    text = "ABOUT",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                FireCard {
                    Column {
                        SettingItem(
                            icon = "📄",
                            title = "Privacy Policy",
                            subtitle = "How we protect your data",
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://firechallenngepath.com/privacy-policy.html"))
                                context.startActivity(intent)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset All Progress?") },
            text = { Text("This will permanently delete all your challenges, check-ins, and progress. This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetAllProgress()
                        showResetDialog = false
                    }
                ) {
                    Text("Reset", color = ErrorRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SettingItem(
    icon: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (isDestructive) {
                        ErrorRed.copy(alpha = 0.2f)
                    } else {
                        BackgroundCardElevated
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = icon,
                fontSize = 24.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isDestructive) ErrorRed else TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = TextSecondary
            )
        }

        Text(
            text = "›",
            fontSize = 24.sp,
            color = TextTertiary
        )
    }
}

