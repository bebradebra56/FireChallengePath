package com.firechalang.pathapp.iotrtg.data.repo

import android.util.Log
import com.firechalang.pathapp.iotrtg.domain.model.FireChallengePathTrackerEntity
import com.firechalang.pathapp.iotrtg.domain.model.FireChallengePathParam
import com.firechalang.pathapp.iotrtg.presentation.app.FireChallengePathApplication.Companion.FIRE_CHALLENGE_PATH_MAIN_TAG
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FireChallengePathApi {
    @Headers("Content-Type: application/json")
    @POST("config.php")
    fun fireChallengePathGetClient(
        @Body jsonString: JsonObject,
    ): Call<FireChallengePathTrackerEntity>
}


private const val FIRE_CHALLENGE_PATH_MAIN = "https://firechallenngepath.com/"
class FireChallengePathRepository {

    suspend fun fireChallengePathGetClient(
        fireChallengePathParam: FireChallengePathParam,
        fireChallengePathConversion: MutableMap<String, Any>?
    ): FireChallengePathTrackerEntity? {
        val gson = Gson()
        val api = fireChallengePathGetApi(FIRE_CHALLENGE_PATH_MAIN, null)

        val fireChallengePathJsonObject = gson.toJsonTree(fireChallengePathParam).asJsonObject
        fireChallengePathConversion?.forEach { (key, value) ->
            val element: JsonElement = gson.toJsonTree(value)
            fireChallengePathJsonObject.add(key, element)
        }
        return try {
            val fireChallengePathRequest: Call<FireChallengePathTrackerEntity> = api.fireChallengePathGetClient(
                jsonString = fireChallengePathJsonObject,
            )
            val fireChallengePathResult = fireChallengePathRequest.awaitResponse()
            Log.d(FIRE_CHALLENGE_PATH_MAIN_TAG, "Retrofit: Result code: ${fireChallengePathResult.code()}")
            if (fireChallengePathResult.code() == 200) {
                Log.d(FIRE_CHALLENGE_PATH_MAIN_TAG, "Retrofit: Get request success")
                Log.d(FIRE_CHALLENGE_PATH_MAIN_TAG, "Retrofit: Code = ${fireChallengePathResult.code()}")
                Log.d(FIRE_CHALLENGE_PATH_MAIN_TAG, "Retrofit: ${fireChallengePathResult.body()}")
                fireChallengePathResult.body()
            } else {
                null
            }
        } catch (e: java.lang.Exception) {
            Log.d(FIRE_CHALLENGE_PATH_MAIN_TAG, "Retrofit: Get request failed")
            Log.d(FIRE_CHALLENGE_PATH_MAIN_TAG, "Retrofit: ${e.message}")
            null
        }
    }


    private fun fireChallengePathGetApi(url: String, client: OkHttpClient?) : FireChallengePathApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(client ?: OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create()
    }


}
