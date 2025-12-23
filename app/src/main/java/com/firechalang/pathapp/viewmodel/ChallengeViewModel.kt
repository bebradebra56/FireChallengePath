package com.firechalang.pathapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firechalang.pathapp.data.model.Challenge
import com.firechalang.pathapp.data.model.ChallengeType
import com.firechalang.pathapp.data.model.DailyCheckIn
import com.firechalang.pathapp.data.repository.ChallengeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

class ChallengeViewModel(
    private val repository: ChallengeRepository
) : ViewModel() {

    val allActiveChallenges: StateFlow<List<Challenge>> = repository.getAllActiveChallenges()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allChallenges: StateFlow<List<Challenge>> = repository.getAllChallenges()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedChallenges: StateFlow<List<Challenge>> = repository.getCompletedChallenges()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeChallengesCount: StateFlow<Int> = repository.getActiveChallengesCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val completedChallengesCount: StateFlow<Int> = repository.getCompletedChallengesCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _currentStreak = MutableStateFlow(0)
    val currentStreak: StateFlow<Int> = _currentStreak.asStateFlow()
    
    private val _longestStreak = MutableStateFlow(0)
    val longestStreak: StateFlow<Int> = _longestStreak.asStateFlow()

    private val _selectedChallenge = MutableStateFlow<Challenge?>(null)
    val selectedChallenge: StateFlow<Challenge?> = _selectedChallenge.asStateFlow()

    private val _checkIns = MutableStateFlow<List<DailyCheckIn>>(emptyList())
    val checkIns: StateFlow<List<DailyCheckIn>> = _checkIns.asStateFlow()
    
    init {
        // Load global current streak
        viewModelScope.launch {
            allActiveChallenges.collect { challenges ->
                if (challenges.isNotEmpty()) {
                    // Get streak from first active challenge for home screen
                    val streak = repository.getCurrentStreak(challenges.first().id)
                    _currentStreak.value = streak
                }
            }
        }
    }

    fun selectChallenge(challengeId: Long) {
        viewModelScope.launch {
            repository.getChallengeById(challengeId).collect { challenge ->
                _selectedChallenge.value = challenge
                challenge?.let {
                    loadCheckInsForChallenge(challengeId)
                    loadCurrentStreak(challengeId)
                    loadLongestStreak(challengeId)
                }
            }
        }
    }

    private fun loadCheckInsForChallenge(challengeId: Long) {
        viewModelScope.launch {
            repository.getCheckInsForChallenge(challengeId).collect { checkIns ->
                _checkIns.value = checkIns
            }
        }
    }

    private fun loadCurrentStreak(challengeId: Long) {
        viewModelScope.launch {
            val streak = repository.getCurrentStreak(challengeId)
            _currentStreak.value = streak
        }
    }
    
    private fun loadLongestStreak(challengeId: Long) {
        viewModelScope.launch {
            val longest = repository.getLongestStreak(challengeId)
            _longestStreak.value = longest
        }
    }

    fun createChallenge(
        name: String,
        type: ChallengeType,
        duration: Int?,
        startDate: Long,
        notes: String
    ) {
        viewModelScope.launch {
            val challenge = Challenge(
                name = name,
                type = type,
                duration = duration,
                startDate = startDate,
                notes = notes
            )
            repository.insertChallenge(challenge)
        }
    }

    fun updateChallenge(challenge: Challenge) {
        viewModelScope.launch {
            repository.updateChallenge(challenge)
        }
    }

    fun deleteChallenge(challenge: Challenge) {
        viewModelScope.launch {
            repository.deleteChallenge(challenge)
        }
    }

    fun completeChallenge(challengeId: Long) {
        viewModelScope.launch {
            _selectedChallenge.value?.let { challenge ->
                repository.updateChallenge(
                    challenge.copy(
                        isCompleted = true,
                        isActive = false
                    )
                )
            }
        }
    }

    fun checkInToday(challengeId: Long, completed: Boolean) {
        viewModelScope.launch {
            val today = getTodayTimestamp()
            val checkIn = DailyCheckIn(
                challengeId = challengeId,
                date = today,
                completed = completed
            )
            repository.insertCheckIn(checkIn)
            
            if (completed) {
                loadCurrentStreak(challengeId)
            } else {
                _currentStreak.value = 0
            }
            
            // Reload challenge data to update progress
            selectChallenge(challengeId)
        }
    }

    fun getCheckInsForDateRange(startDate: Long, endDate: Long): Flow<List<DailyCheckIn>> {
        return repository.getCheckInsBetweenDates(startDate, endDate)
    }

    fun getAllCompletedCheckIns(): Flow<List<DailyCheckIn>> {
        return repository.getAllCompletedCheckIns()
    }

    suspend fun getLongestStreak(challengeId: Long): Int {
        return repository.getLongestStreak(challengeId)
    }

    fun resetAllData() {
        viewModelScope.launch {
            repository.deleteAllData()
        }
    }

    private fun getTodayTimestamp(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun hasTodayCheckIn(challengeId: Long): Flow<Boolean> {
        return flow {
            val today = getTodayTimestamp()
            val tomorrow = today + 24 * 60 * 60 * 1000
            repository.getCheckInsBetweenDates(today, tomorrow).collect { checkIns ->
                emit(checkIns.any { it.challengeId == challengeId })
            }
        }
    }
}

