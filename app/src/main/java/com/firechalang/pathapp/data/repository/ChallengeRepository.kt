package com.firechalang.pathapp.data.repository

import com.firechalang.pathapp.data.dao.ChallengeDao
import com.firechalang.pathapp.data.dao.DailyCheckInDao
import com.firechalang.pathapp.data.model.Challenge
import com.firechalang.pathapp.data.model.DailyCheckIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.Calendar

class ChallengeRepository(
    private val challengeDao: ChallengeDao,
    private val dailyCheckInDao: DailyCheckInDao
) {
    fun getAllActiveChallenges(): Flow<List<Challenge>> = challengeDao.getAllActiveChallenges()
    
    fun getAllChallenges(): Flow<List<Challenge>> = challengeDao.getAllChallenges()
    
    fun getChallengeById(id: Long): Flow<Challenge?> = challengeDao.getChallengeById(id)
    
    fun getCompletedChallenges(): Flow<List<Challenge>> = challengeDao.getCompletedChallenges()
    
    fun getActiveChallengesCount(): Flow<Int> = challengeDao.getActiveChallengesCount()
    
    fun getCompletedChallengesCount(): Flow<Int> = challengeDao.getCompletedChallengesCount()
    
    suspend fun insertChallenge(challenge: Challenge): Long = challengeDao.insertChallenge(challenge)
    
    suspend fun updateChallenge(challenge: Challenge) = challengeDao.updateChallenge(challenge)
    
    suspend fun deleteChallenge(challenge: Challenge) {
        challengeDao.deleteChallenge(challenge)
        dailyCheckInDao.deleteCheckInsForChallenge(challenge.id)
    }
    
    suspend fun deleteAllData() {
        challengeDao.deleteAllChallenges()
        dailyCheckInDao.deleteAllCheckIns()
    }
    
    // Daily Check-In operations
    fun getCheckInsForChallenge(challengeId: Long): Flow<List<DailyCheckIn>> =
        dailyCheckInDao.getCheckInsForChallenge(challengeId)
    
    fun getCheckInsBetweenDates(startDate: Long, endDate: Long): Flow<List<DailyCheckIn>> =
        dailyCheckInDao.getCheckInsBetweenDates(startDate, endDate)
    
    fun getAllCompletedCheckIns(): Flow<List<DailyCheckIn>> =
        dailyCheckInDao.getAllCompletedCheckIns()
    
    suspend fun insertCheckIn(checkIn: DailyCheckIn) = dailyCheckInDao.insertCheckIn(checkIn)
    
    suspend fun getCurrentStreak(challengeId: Long): Int {
        val checkIns = dailyCheckInDao.getCheckInsForChallenge(challengeId).first()
        if (checkIns.isEmpty()) return 0
        
        val sortedCheckIns = checkIns.filter { it.completed }.sortedByDescending { it.date }
        if (sortedCheckIns.isEmpty()) return 0
        
        var streak = 0
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        
        for (checkIn in sortedCheckIns) {
            val checkInCalendar = Calendar.getInstance()
            checkInCalendar.timeInMillis = checkIn.date
            
            if (isSameDay(calendar, checkInCalendar)) {
                streak++
                calendar.add(Calendar.DAY_OF_YEAR, -1)
            } else {
                break
            }
        }
        
        return streak
    }
    
    suspend fun getLongestStreak(challengeId: Long): Int {
        val checkIns = dailyCheckInDao.getCheckInsForChallenge(challengeId).first()
        if (checkIns.isEmpty()) return 0
        
        val sortedCheckIns = checkIns.filter { it.completed }.sortedBy { it.date }
        if (sortedCheckIns.isEmpty()) return 0
        
        var maxStreak = 1
        var currentStreak = 1
        
        for (i in 1 until sortedCheckIns.size) {
            val prevCalendar = Calendar.getInstance()
            prevCalendar.timeInMillis = sortedCheckIns[i - 1].date
            
            val currentCalendar = Calendar.getInstance()
            currentCalendar.timeInMillis = sortedCheckIns[i].date
            
            prevCalendar.add(Calendar.DAY_OF_YEAR, 1)
            
            if (isSameDay(prevCalendar, currentCalendar)) {
                currentStreak++
                if (currentStreak > maxStreak) {
                    maxStreak = currentStreak
                }
            } else {
                currentStreak = 1
            }
        }
        
        return maxStreak
    }
    
    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
}

