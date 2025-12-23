package com.firechalang.pathapp.iotrtg.presentation.ui.view

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.firechalang.pathapp.iotrtg.presentation.app.FireChallengePathApplication
import com.firechalang.pathapp.iotrtg.presentation.ui.load.FireChallengePathLoadFragment
import org.koin.android.ext.android.inject

class FireChallengePathV : Fragment(){

    private lateinit var fireChallengePathPhoto: Uri
    private var fireChallengePathFilePathFromChrome: ValueCallback<Array<Uri>>? = null

    private val fireChallengePathTakeFile: ActivityResultLauncher<PickVisualMediaRequest> = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        fireChallengePathFilePathFromChrome?.onReceiveValue(arrayOf(it ?: Uri.EMPTY))
        fireChallengePathFilePathFromChrome = null
    }

    private val fireChallengePathTakePhoto: ActivityResultLauncher<Uri> = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            fireChallengePathFilePathFromChrome?.onReceiveValue(arrayOf(fireChallengePathPhoto))
            fireChallengePathFilePathFromChrome = null
        } else {
            fireChallengePathFilePathFromChrome?.onReceiveValue(null)
            fireChallengePathFilePathFromChrome = null
        }
    }

    private val fireChallengePathDataStore by activityViewModels<FireChallengePathDataStore>()


    private val fireChallengePathViFun by inject<FireChallengePathViFun>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "Fragment onCreate")
        CookieManager.getInstance().setAcceptCookie(true)
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (fireChallengePathDataStore.fireChallengePathView.canGoBack()) {
                        fireChallengePathDataStore.fireChallengePathView.goBack()
                        Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "WebView can go back")
                    } else if (fireChallengePathDataStore.fireChallengePathViList.size > 1) {
                        Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "WebView can`t go back")
                        fireChallengePathDataStore.fireChallengePathViList.removeAt(fireChallengePathDataStore.fireChallengePathViList.lastIndex)
                        Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "WebView list size ${fireChallengePathDataStore.fireChallengePathViList.size}")
                        fireChallengePathDataStore.fireChallengePathView.destroy()
                        val previousWebView = fireChallengePathDataStore.fireChallengePathViList.last()
                        fireChallengePathAttachWebViewToContainer(previousWebView)
                        fireChallengePathDataStore.fireChallengePathView = previousWebView
                    }
                }

            })
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (fireChallengePathDataStore.fireChallengePathIsFirstCreate) {
            fireChallengePathDataStore.fireChallengePathIsFirstCreate = false
            fireChallengePathDataStore.fireChallengePathContainerView = FrameLayout(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                id = View.generateViewId()
            }
            return fireChallengePathDataStore.fireChallengePathContainerView
        } else {
            return fireChallengePathDataStore.fireChallengePathContainerView
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "onViewCreated")
        if (fireChallengePathDataStore.fireChallengePathViList.isEmpty()) {
            fireChallengePathDataStore.fireChallengePathView = FireChallengePathVi(requireContext(), object :
                FireChallengePathCallBack {
                override fun fireChallengePathHandleCreateWebWindowRequest(fireChallengePathVi: FireChallengePathVi) {
                    fireChallengePathDataStore.fireChallengePathViList.add(fireChallengePathVi)
                    Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "WebView list size = ${fireChallengePathDataStore.fireChallengePathViList.size}")
                    Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "CreateWebWindowRequest")
                    fireChallengePathDataStore.fireChallengePathView = fireChallengePathVi
                    fireChallengePathVi.fireChallengePathSetFileChooserHandler { callback ->
                        fireChallengePathHandleFileChooser(callback)
                    }
                    fireChallengePathAttachWebViewToContainer(fireChallengePathVi)
                }

            }, fireChallengePathWindow = requireActivity().window).apply {
                fireChallengePathSetFileChooserHandler { callback ->
                    fireChallengePathHandleFileChooser(callback)
                }
            }
            fireChallengePathDataStore.fireChallengePathView.fireChallengePathFLoad(arguments?.getString(
                FireChallengePathLoadFragment.FIRE_CHALLENGE_PATH_D) ?: "")
//            ejvview.fLoad("www.google.com")
            fireChallengePathDataStore.fireChallengePathViList.add(fireChallengePathDataStore.fireChallengePathView)
            fireChallengePathAttachWebViewToContainer(fireChallengePathDataStore.fireChallengePathView)
        } else {
            fireChallengePathDataStore.fireChallengePathViList.forEach { webView ->
                webView.fireChallengePathSetFileChooserHandler { callback ->
                    fireChallengePathHandleFileChooser(callback)
                }
            }
            fireChallengePathDataStore.fireChallengePathView = fireChallengePathDataStore.fireChallengePathViList.last()

            fireChallengePathAttachWebViewToContainer(fireChallengePathDataStore.fireChallengePathView)
        }
        Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "WebView list size = ${fireChallengePathDataStore.fireChallengePathViList.size}")
    }

    private fun fireChallengePathHandleFileChooser(callback: ValueCallback<Array<Uri>>?) {
        Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "handleFileChooser called, callback: ${callback != null}")

        fireChallengePathFilePathFromChrome = callback

        val listItems: Array<out String> = arrayOf("Select from file", "To make a photo")
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                0 -> {
                    Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "Launching file picker")
                    fireChallengePathTakeFile.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
                1 -> {
                    Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "Launching camera")
                    fireChallengePathPhoto = fireChallengePathViFun.fireChallengePathSavePhoto()
                    fireChallengePathTakePhoto.launch(fireChallengePathPhoto)
                }
            }
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Choose a method")
            .setItems(listItems, listener)
            .setCancelable(true)
            .setOnCancelListener {
                Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "File chooser canceled")
                callback?.onReceiveValue(null)
                fireChallengePathFilePathFromChrome = null
            }
            .create()
            .show()
    }

    private fun fireChallengePathAttachWebViewToContainer(w: FireChallengePathVi) {
        fireChallengePathDataStore.fireChallengePathContainerView.post {
            (w.parent as? ViewGroup)?.removeView(w)
            fireChallengePathDataStore.fireChallengePathContainerView.removeAllViews()
            fireChallengePathDataStore.fireChallengePathContainerView.addView(w)
        }
    }


}