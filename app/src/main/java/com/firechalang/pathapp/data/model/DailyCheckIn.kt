package com.firechalang.pathapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_check_ins")
data class DailyCheckIn(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val challengeId: Long,
    val date: Long, // timestamp
    val completed: Boolean,
    val createdAt: Long = System.currentTimeMillis()
)

