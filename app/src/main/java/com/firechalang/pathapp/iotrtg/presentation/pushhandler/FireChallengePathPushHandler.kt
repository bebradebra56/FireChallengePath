package com.firechalang.pathapp.iotrtg.presentation.pushhandler

import android.os.Bundle
import android.util.Log
import com.firechalang.pathapp.iotrtg.presentation.app.FireChallengePathApplication

class FireChallengePathPushHandler {
    fun fireChallengePathHandlePush(extras: Bundle?) {
        Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "Extras from Push = ${extras?.keySet()}")
        if (extras != null) {
            val map = fireChallengePathBundleToMap(extras)
            Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "Map from Push = $map")
            map?.let {
                if (map.containsKey("url")) {
                    FireChallengePathApplication.FIRE_CHALLENGE_PATH_FB_LI = map["url"]
                    Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "UrlFromActivity = $map")
                }
            }
        } else {
            Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "Push data no!")
        }
    }

    private fun fireChallengePathBundleToMap(extras: Bundle): Map<String, String?>? {
        val map: MutableMap<String, String?> = HashMap()
        val ks = extras.keySet()
        val iterator: Iterator<String> = ks.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            map[key] = extras.getString(key)
        }
        return map
    }

}