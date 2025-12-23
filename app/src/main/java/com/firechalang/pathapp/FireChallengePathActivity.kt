package com.firechalang.pathapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.firechalang.pathapp.iotrtg.FireChallengePathGlobalLayoutUtil
import com.firechalang.pathapp.iotrtg.fireChallengePathSetupSystemBars
import com.firechalang.pathapp.iotrtg.presentation.app.FireChallengePathApplication
import com.firechalang.pathapp.iotrtg.presentation.pushhandler.FireChallengePathPushHandler
import org.koin.android.ext.android.inject

class FireChallengePathActivity : AppCompatActivity() {

    private val fireChallengePathPushHandler by inject<FireChallengePathPushHandler>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        fireChallengePathSetupSystemBars()
        WindowCompat.setDecorFitsSystemWindows(window, false)


        setContentView(R.layout.activity_fire_challenge_path)

        val fireChallengePathRootView = findViewById<View>(android.R.id.content)
        FireChallengePathGlobalLayoutUtil().fireChallengePathAssistActivity(this)
        ViewCompat.setOnApplyWindowInsetsListener(fireChallengePathRootView) { fireChallengePathView, fireChallengePathInsets ->
            val fireChallengePathSystemBars = fireChallengePathInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val fireChallengePathDisplayCutout = fireChallengePathInsets.getInsets(WindowInsetsCompat.Type.displayCutout())
            val fireChallengePathIme = fireChallengePathInsets.getInsets(WindowInsetsCompat.Type.ime())


            val fireChallengePathTopPadding = maxOf(fireChallengePathSystemBars.top, fireChallengePathDisplayCutout.top)
            val fireChallengePathLeftPadding = maxOf(fireChallengePathSystemBars.left, fireChallengePathDisplayCutout.left)
            val fireChallengePathRightPadding = maxOf(fireChallengePathSystemBars.right, fireChallengePathDisplayCutout.right)
            window.setSoftInputMode(FireChallengePathApplication.fireChallengePathInputMode)

            if (window.attributes.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN) {
                Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "ADJUST PUN")
                val fireChallengePathBottomInset = maxOf(fireChallengePathSystemBars.bottom, fireChallengePathDisplayCutout.bottom)

                fireChallengePathView.setPadding(fireChallengePathLeftPadding, fireChallengePathTopPadding, fireChallengePathRightPadding, 0)

                fireChallengePathView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = fireChallengePathBottomInset
                }
            } else {
                Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "ADJUST RESIZE")

                val fireChallengePathBottomInset = maxOf(fireChallengePathSystemBars.bottom, fireChallengePathDisplayCutout.bottom, fireChallengePathIme.bottom)

                fireChallengePathView.setPadding(fireChallengePathLeftPadding, fireChallengePathTopPadding, fireChallengePathRightPadding, 0)

                fireChallengePathView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = fireChallengePathBottomInset
                }
            }



            WindowInsetsCompat.CONSUMED
        }
        Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "Activity onCreate()")
        fireChallengePathPushHandler.fireChallengePathHandlePush(intent.extras)
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            fireChallengePathSetupSystemBars()
        }
    }

    override fun onResume() {
        super.onResume()
        fireChallengePathSetupSystemBars()
    }
}