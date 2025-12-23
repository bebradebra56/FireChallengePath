package com.firechalang.pathapp.iotrtg.domain.model

import com.google.gson.annotations.SerializedName


data class FireChallengePathTrackerEntity (
    @SerializedName("ok")
    val fireChallengePathOk: String,
    @SerializedName("url")
    val fireChallengePathUrl: String,
    @SerializedName("expires")
    val fireChallengePathExpires: Long,
)