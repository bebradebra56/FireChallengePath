package com.firechalang.pathapp.data.database

import androidx.room.TypeConverter
import com.firechalang.pathapp.data.model.ChallengeType

class Converters {
    @TypeConverter
    fun fromChallengeType(value: ChallengeType): String {
        return value.name
    }

    @TypeConverter
    fun toChallengeType(value: String): ChallengeType {
        return ChallengeType.valueOf(value)
    }
}

