package com.firechalang.pathapp.iotrtg.presentation.ui.load

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firechalang.pathapp.iotrtg.data.shar.FireChallengePathSharedPreference
import com.firechalang.pathapp.iotrtg.data.utils.FireChallengePathSystemService
import com.firechalang.pathapp.iotrtg.domain.usecases.FireChallengePathGetAllUseCase
import com.firechalang.pathapp.iotrtg.presentation.app.FireChallengePathAppsFlyerState
import com.firechalang.pathapp.iotrtg.presentation.app.FireChallengePathApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FireChallengePathLoadViewModel(
    private val fireChallengePathGetAllUseCase: FireChallengePathGetAllUseCase,
    private val fireChallengePathSharedPreference: FireChallengePathSharedPreference,
    private val fireChallengePathSystemService: FireChallengePathSystemService
) : ViewModel() {

    private val _fireChallengePathHomeScreenState: MutableStateFlow<FireChallengePathHomeScreenState> =
        MutableStateFlow(FireChallengePathHomeScreenState.FireChallengePathLoading)
    val fireChallengePathHomeScreenState = _fireChallengePathHomeScreenState.asStateFlow()

    private var fireChallengePathGetApps = false


    init {
        viewModelScope.launch {
            when (fireChallengePathSharedPreference.fireChallengePathAppState) {
                0 -> {
                    if (fireChallengePathSystemService.fireChallengePathIsOnline()) {
                        FireChallengePathApplication.fireChallengePathConversionFlow.collect {
                            when(it) {
                                FireChallengePathAppsFlyerState.FireChallengePathDefault -> {}
                                FireChallengePathAppsFlyerState.FireChallengePathError -> {
                                    fireChallengePathSharedPreference.fireChallengePathAppState = 2
                                    _fireChallengePathHomeScreenState.value =
                                        FireChallengePathHomeScreenState.FireChallengePathError
                                    fireChallengePathGetApps = true
                                }
                                is FireChallengePathAppsFlyerState.FireChallengePathSuccess -> {
                                    if (!fireChallengePathGetApps) {
                                        fireChallengePathGetData(it.fireChallengePathData)
                                        fireChallengePathGetApps = true
                                    }
                                }
                            }
                        }
                    } else {
                        _fireChallengePathHomeScreenState.value =
                            FireChallengePathHomeScreenState.FireChallengePathNotInternet
                    }
                }
                1 -> {
                    if (fireChallengePathSystemService.fireChallengePathIsOnline()) {
                        if (FireChallengePathApplication.FIRE_CHALLENGE_PATH_FB_LI != null) {
                            _fireChallengePathHomeScreenState.value =
                                FireChallengePathHomeScreenState.FireChallengePathSuccess(
                                    FireChallengePathApplication.FIRE_CHALLENGE_PATH_FB_LI.toString()
                                )
                        } else if (System.currentTimeMillis() / 1000 > fireChallengePathSharedPreference.fireChallengePathExpired) {
                            Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "Current time more then expired, repeat request")
                            FireChallengePathApplication.fireChallengePathConversionFlow.collect {
                                when(it) {
                                    FireChallengePathAppsFlyerState.FireChallengePathDefault -> {}
                                    FireChallengePathAppsFlyerState.FireChallengePathError -> {
                                        _fireChallengePathHomeScreenState.value =
                                            FireChallengePathHomeScreenState.FireChallengePathSuccess(
                                                fireChallengePathSharedPreference.fireChallengePathSavedUrl
                                            )
                                        fireChallengePathGetApps = true
                                    }
                                    is FireChallengePathAppsFlyerState.FireChallengePathSuccess -> {
                                        if (!fireChallengePathGetApps) {
                                            fireChallengePathGetData(it.fireChallengePathData)
                                            fireChallengePathGetApps = true
                                        }
                                    }
                                }
                            }
                        } else {
                            Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "Current time less then expired, use saved url")
                            _fireChallengePathHomeScreenState.value =
                                FireChallengePathHomeScreenState.FireChallengePathSuccess(
                                    fireChallengePathSharedPreference.fireChallengePathSavedUrl
                                )
                        }
                    } else {
                        _fireChallengePathHomeScreenState.value =
                            FireChallengePathHomeScreenState.FireChallengePathNotInternet
                    }
                }
                2 -> {
                    _fireChallengePathHomeScreenState.value =
                        FireChallengePathHomeScreenState.FireChallengePathError
                }
            }
        }
    }


    private suspend fun fireChallengePathGetData(conversation: MutableMap<String, Any>?) {
        val fireChallengePathData = fireChallengePathGetAllUseCase.invoke(conversation)
        if (fireChallengePathSharedPreference.fireChallengePathAppState == 0) {
            if (fireChallengePathData == null) {
                fireChallengePathSharedPreference.fireChallengePathAppState = 2
                _fireChallengePathHomeScreenState.value =
                    FireChallengePathHomeScreenState.FireChallengePathError
            } else {
                fireChallengePathSharedPreference.fireChallengePathAppState = 1
                fireChallengePathSharedPreference.apply {
                    fireChallengePathExpired = fireChallengePathData.fireChallengePathExpires
                    fireChallengePathSavedUrl = fireChallengePathData.fireChallengePathUrl
                }
                _fireChallengePathHomeScreenState.value =
                    FireChallengePathHomeScreenState.FireChallengePathSuccess(fireChallengePathData.fireChallengePathUrl)
            }
        } else  {
            if (fireChallengePathData == null) {
                _fireChallengePathHomeScreenState.value =
                    FireChallengePathHomeScreenState.FireChallengePathSuccess(
                        fireChallengePathSharedPreference.fireChallengePathSavedUrl
                    )
            } else {
                fireChallengePathSharedPreference.apply {
                    fireChallengePathExpired = fireChallengePathData.fireChallengePathExpires
                    fireChallengePathSavedUrl = fireChallengePathData.fireChallengePathUrl
                }
                _fireChallengePathHomeScreenState.value =
                    FireChallengePathHomeScreenState.FireChallengePathSuccess(fireChallengePathData.fireChallengePathUrl)
            }
        }
    }


    sealed class FireChallengePathHomeScreenState {
        data object FireChallengePathLoading : FireChallengePathHomeScreenState()
        data object FireChallengePathError : FireChallengePathHomeScreenState()
        data class FireChallengePathSuccess(val data: String) : FireChallengePathHomeScreenState()
        data object FireChallengePathNotInternet: FireChallengePathHomeScreenState()
    }
}