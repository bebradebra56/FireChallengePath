package com.firechalang.pathapp.iotrtg.data.shar

import android.content.Context
import androidx.core.content.edit

class FireChallengePathSharedPreference(context: Context) {
    private val fireChallengePathPrefs = context.getSharedPreferences("fireChallengePathSharedPrefsAb", Context.MODE_PRIVATE)

    var fireChallengePathSavedUrl: String
        get() = fireChallengePathPrefs.getString(FIRE_CHALLENGE_PATH_SAVED_URL, "") ?: ""
        set(value) = fireChallengePathPrefs.edit { putString(FIRE_CHALLENGE_PATH_SAVED_URL, value) }

    var fireChallengePathExpired : Long
        get() = fireChallengePathPrefs.getLong(FIRE_CHALLENGE_PATH_EXPIRED, 0L)
        set(value) = fireChallengePathPrefs.edit { putLong(FIRE_CHALLENGE_PATH_EXPIRED, value) }

    var fireChallengePathAppState: Int
        get() = fireChallengePathPrefs.getInt(FIRE_CHALLENGE_PATH_APPLICATION_STATE, 0)
        set(value) = fireChallengePathPrefs.edit { putInt(FIRE_CHALLENGE_PATH_APPLICATION_STATE, value) }

    var fireChallengePathNotificationRequest: Long
        get() = fireChallengePathPrefs.getLong(FIRE_CHALLENGE_PATH_NOTIFICAITON_REQUEST, 0L)
        set(value) = fireChallengePathPrefs.edit { putLong(FIRE_CHALLENGE_PATH_NOTIFICAITON_REQUEST, value) }

    var fireChallengePathNotificationRequestedBefore: Boolean
        get() = fireChallengePathPrefs.getBoolean(FIRE_CHALLENGE_PATH_NOTIFICATION_REQUEST_BEFORE, false)
        set(value) = fireChallengePathPrefs.edit { putBoolean(
            FIRE_CHALLENGE_PATH_NOTIFICATION_REQUEST_BEFORE, value) }

    companion object {
        private const val FIRE_CHALLENGE_PATH_SAVED_URL = "fireChallengePathSavedUrl"
        private const val FIRE_CHALLENGE_PATH_EXPIRED = "fireChallengePathExpired"
        private const val FIRE_CHALLENGE_PATH_APPLICATION_STATE = "fireChallengePathApplicationState"
        private const val FIRE_CHALLENGE_PATH_NOTIFICAITON_REQUEST = "fireChallengePathNotificationRequest"
        private const val FIRE_CHALLENGE_PATH_NOTIFICATION_REQUEST_BEFORE = "fireChallengePathNotificationRequestedBefore"
    }
}