package com.firechalang.pathapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firechalang.pathapp.ui.theme.*
import com.firechalang.pathapp.viewmodel.ChallengeViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: ChallengeViewModel,
    onNavigateBack: () -> Unit
) {
    val allCheckIns by viewModel.getAllCompletedCheckIns().collectAsStateWithLifecycle(initialValue = emptyList())
    val calendar = Calendar.getInstance()
    val currentMonth = remember { mutableIntStateOf(calendar.get(Calendar.MONTH)) }
    val currentYear = remember { mutableIntStateOf(calendar.get(Calendar.YEAR)) }
    
    val checkInDates = remember(allCheckIns) {
        allCheckIns.filter { it.completed }.map { 
            val cal = Calendar.getInstance()
            cal.timeInMillis = it.date
            Triple(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
        }.toSet()
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
                        text = "Calendar",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // Month selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        if (currentMonth.intValue == 0) {
                            currentMonth.intValue = 11
                            currentYear.intValue--
                        } else {
                            currentMonth.intValue--
                        }
                    }) {
                        Text(text = "◀", fontSize = 24.sp, color = FireOrange)
                    }

                    Text(
                        text = "${getMonthName(currentMonth.intValue)} ${currentYear.intValue}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    IconButton(onClick = {
                        if (currentMonth.intValue == 11) {
                            currentMonth.intValue = 0
                            currentYear.intValue++
                        } else {
                            currentMonth.intValue++
                        }
                    }) {
                        Text(text = "▶", fontSize = 24.sp, color = FireOrange)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Calendar grid
                CalendarGrid(
                    year = currentYear.intValue,
                    month = currentMonth.intValue,
                    checkInDates = checkInDates
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    LegendItem(color = FireOrange, text = "Completed")
                    LegendItem(color = TextTertiary, text = "Not Completed")
                }
            }
        }
    }
}

@Composable
fun CalendarGrid(
    year: Int,
    month: Int,
    checkInDates: Set<Triple<Int, Int, Int>>
) {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, 1)
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1

    Column {
        // Day headers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Days grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(300.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Empty cells before first day
            items(firstDayOfWeek) {
                Box(modifier = Modifier.aspectRatio(1f))
            }

            // Day cells
            items(daysInMonth) { dayIndex ->
                val day = dayIndex + 1
                val hasCheckIn = checkInDates.contains(Triple(year, month, day))

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(
                            if (hasCheckIn) {
                                Brush.radialGradient(
                                    colors = listOf(
                                        FireOrange.copy(alpha = 0.3f),
                                        FireRed.copy(alpha = 0.2f)
                                    )
                                )
                            } else {
                                Brush.radialGradient(
                                    colors = listOf(
                                        BackgroundCard,
                                        BackgroundCard
                                    )
                                )
                            }
                        )
                        .border(
                            width = if (hasCheckIn) 2.dp else 1.dp,
                            color = if (hasCheckIn) FireOrange else TextTertiary,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = day.toString(),
                            fontSize = 14.sp,
                            fontWeight = if (hasCheckIn) FontWeight.Bold else FontWeight.Normal,
                            color = if (hasCheckIn) FireOrange else TextSecondary
                        )
                        if (hasCheckIn) {
                            Text(
                                text = "🔥",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            color = TextSecondary
        )
    }
}

fun getMonthName(month: Int): String {
    return listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )[month]
}

