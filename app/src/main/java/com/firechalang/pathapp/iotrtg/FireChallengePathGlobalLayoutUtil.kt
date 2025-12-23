package com.firechalang.pathapp.iotrtg

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.widget.FrameLayout
import com.firechalang.pathapp.iotrtg.presentation.app.FireChallengePathApplication

class FireChallengePathGlobalLayoutUtil {

    private var fireChallengePathMChildOfContent: View? = null
    private var fireChallengePathUsableHeightPrevious = 0

    fun fireChallengePathAssistActivity(activity: Activity) {
        val content = activity.findViewById<FrameLayout>(android.R.id.content)
        fireChallengePathMChildOfContent = content.getChildAt(0)

        fireChallengePathMChildOfContent?.viewTreeObserver?.addOnGlobalLayoutListener {
            possiblyResizeChildOfContent(activity)
        }
    }

    private fun possiblyResizeChildOfContent(activity: Activity) {
        val fireChallengePathUsableHeightNow = fireChallengePathComputeUsableHeight()
        if (fireChallengePathUsableHeightNow != fireChallengePathUsableHeightPrevious) {
            val fireChallengePathUsableHeightSansKeyboard = fireChallengePathMChildOfContent?.rootView?.height ?: 0
            val fireChallengePathHeightDifference = fireChallengePathUsableHeightSansKeyboard - fireChallengePathUsableHeightNow

            if (fireChallengePathHeightDifference > (fireChallengePathUsableHeightSansKeyboard / 4)) {
                activity.window.setSoftInputMode(FireChallengePathApplication.fireChallengePathInputMode)
            } else {
                activity.window.setSoftInputMode(FireChallengePathApplication.fireChallengePathInputMode)
            }
//            mChildOfContent?.requestLayout()
            fireChallengePathUsableHeightPrevious = fireChallengePathUsableHeightNow
        }
    }

    private fun fireChallengePathComputeUsableHeight(): Int {
        val r = Rect()
        fireChallengePathMChildOfContent?.getWindowVisibleDisplayFrame(r)
        return r.bottom - r.top  // Visible height без status bar
    }
}