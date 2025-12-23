package com.firechalang.pathapp.iotrtg.presentation.ui.view

import android.annotation.SuppressLint
import android.widget.FrameLayout
import androidx.lifecycle.ViewModel

class FireChallengePathDataStore : ViewModel(){
    val fireChallengePathViList: MutableList<FireChallengePathVi> = mutableListOf()
    var fireChallengePathIsFirstCreate = true
    @SuppressLint("StaticFieldLeak")
    lateinit var fireChallengePathContainerView: FrameLayout
    @SuppressLint("StaticFieldLeak")
    lateinit var fireChallengePathView: FireChallengePathVi

}