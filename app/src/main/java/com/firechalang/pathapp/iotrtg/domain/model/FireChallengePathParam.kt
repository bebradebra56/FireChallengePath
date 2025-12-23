package com.firechalang.pathapp.iotrtg.domain.model

import com.google.gson.annotations.SerializedName


private const val FIRE_CHALLENGE_PATH_A = "com.firechalang.pathapp"
private const val FIRE_CHALLENGE_PATH_B = "firechallengepath"
data class FireChallengePathParam (
    @SerializedName("af_id")
    val fireChallengePathAfId: String,
    @SerializedName("bundle_id")
    val fireChallengePathBundleId: String = FIRE_CHALLENGE_PATH_A,
    @SerializedName("os")
    val fireChallengePathOs: String = "Android",
    @SerializedName("store_id")
    val fireChallengePathStoreId: String = FIRE_CHALLENGE_PATH_A,
    @SerializedName("locale")
    val fireChallengePathLocale: String,
    @SerializedName("push_token")
    val fireChallengePathPushToken: String,
    @SerializedName("firebase_project_id")
    val fireChallengePathFirebaseProjectId: String = FIRE_CHALLENGE_PATH_B,

    )