package com.firechalang.pathapp.iotrtg.domain.usecases

import android.util.Log
import com.firechalang.pathapp.iotrtg.data.repo.FireChallengePathRepository
import com.firechalang.pathapp.iotrtg.data.utils.FireChallengePathPushToken
import com.firechalang.pathapp.iotrtg.data.utils.FireChallengePathSystemService
import com.firechalang.pathapp.iotrtg.domain.model.FireChallengePathTrackerEntity
import com.firechalang.pathapp.iotrtg.domain.model.FireChallengePathParam
import com.firechalang.pathapp.iotrtg.presentation.app.FireChallengePathApplication

class FireChallengePathGetAllUseCase(
    private val fireChallengePathRepository: FireChallengePathRepository,
    private val fireChallengePathSystemService: FireChallengePathSystemService,
    private val fireChallengePathPushToken: FireChallengePathPushToken,
) {
    suspend operator fun invoke(conversion: MutableMap<String, Any>?) : FireChallengePathTrackerEntity?{
        val params = FireChallengePathParam(
            fireChallengePathLocale = fireChallengePathSystemService.fireChallengePathGetLocale(),
            fireChallengePathPushToken = fireChallengePathPushToken.fireChallengePathGetToken(),
            fireChallengePathAfId = fireChallengePathSystemService.fireChallengePathGetAppsflyerId()
        )
        Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "Params for request: $params")
        return fireChallengePathRepository.fireChallengePathGetClient(params, conversion)
    }



}