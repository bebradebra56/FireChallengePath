package com.firechalang.pathapp.iotrtg.data.utils

import android.util.Log
import com.firechalang.pathapp.iotrtg.presentation.app.FireChallengePathApplication
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class FireChallengePathPushToken {

    suspend fun fireChallengePathGetToken(
        fireChallengePathMaxAttempts: Int = 3,
        fireChallengePathDelayMs: Long = 1500
    ): String {

        repeat(fireChallengePathMaxAttempts - 1) {
            try {
                val fireChallengePathToken = FirebaseMessaging.getInstance().token.await()
                return fireChallengePathToken
            } catch (e: Exception) {
                Log.e(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "Token error (attempt ${it + 1}): ${e.message}")
                delay(fireChallengePathDelayMs)
            }
        }

        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            Log.e(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "Token error final: ${e.message}")
            "null"
        }
    }


}