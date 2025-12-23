package com.firechalang.pathapp.data.dao

import androidx.room.*
import com.firechalang.pathapp.data.model.DailyCheckIn
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyCheckInDao {
    @Query("SELECT * FROM daily_check_ins WHERE challengeId = :challengeId ORDER BY date DESC")
    fun getCheckInsForChallenge(challengeId: Long): Flow<List<DailyCheckIn>>

    @Query("SELECT * FROM daily_check_ins WHERE date >= :startDate AND date <= :endDate")
    fun getCheckInsBetweenDates(startDate: Long, endDate: Long): Flow<List<DailyCheckIn>>

    @Query("SELECT * FROM daily_check_ins WHERE challengeId = :challengeId AND date >= :startDate AND date <= :endDate")
    fun getCheckInsForChallengeInDateRange(
        challengeId: Long,
        startDate: Long,
        endDate: Long
    ): Flow<List<DailyCheckIn>>

    @Query("SELECT * FROM daily_check_ins WHERE completed = 1 ORDER BY date DESC")
    fun getAllCompletedCheckIns(): Flow<List<DailyCheckIn>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckIn(checkIn: DailyCheckIn)

    @Update
    suspend fun updateCheckIn(checkIn: DailyCheckIn)

    @Delete
    suspend fun deleteCheckIn(checkIn: DailyCheckIn)

    @Query("DELETE FROM daily_check_ins WHERE challengeId = :challengeId")
    suspend fun deleteCheckInsForChallenge(challengeId: Long)

    @Query("DELETE FROM daily_check_ins")
    suspend fun deleteAllCheckIns()
}

