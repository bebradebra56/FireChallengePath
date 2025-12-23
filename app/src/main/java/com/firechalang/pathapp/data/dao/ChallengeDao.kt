package com.firechalang.pathapp.data.dao

import androidx.room.*
import com.firechalang.pathapp.data.model.Challenge
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenges WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getAllActiveChallenges(): Flow<List<Challenge>>

    @Query("SELECT * FROM challenges ORDER BY createdAt DESC")
    fun getAllChallenges(): Flow<List<Challenge>>

    @Query("SELECT * FROM challenges WHERE id = :id")
    fun getChallengeById(id: Long): Flow<Challenge?>

    @Query("SELECT * FROM challenges WHERE isCompleted = 1")
    fun getCompletedChallenges(): Flow<List<Challenge>>

    @Query("SELECT COUNT(*) FROM challenges WHERE isActive = 1")
    fun getActiveChallengesCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM challenges WHERE isCompleted = 1")
    fun getCompletedChallengesCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenge(challenge: Challenge): Long

    @Update
    suspend fun updateChallenge(challenge: Challenge)

    @Delete
    suspend fun deleteChallenge(challenge: Challenge)

    @Query("DELETE FROM challenges")
    suspend fun deleteAllChallenges()
}

