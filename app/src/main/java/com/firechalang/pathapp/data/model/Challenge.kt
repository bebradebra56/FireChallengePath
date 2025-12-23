package com.firechalang.pathapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "challenges")
data class Challenge(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: ChallengeType,
    val duration: Int? = null, // in days
    val startDate: Long, // timestamp
    val notes: String = "",
    val isActive: Boolean = true,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

enum class ChallengeType {
    DAILY,
    WEEKLY,
    ONE_TIME
}

