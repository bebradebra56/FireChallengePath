package com.firechalang.pathapp.iotrtg.presentation.ui.load

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.firechalang.pathapp.MainActivity
import com.firechalang.pathapp.R
import com.firechalang.pathapp.databinding.FragmentLoadFireChallengePathBinding
import com.firechalang.pathapp.iotrtg.data.shar.FireChallengePathSharedPreference
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class FireChallengePathLoadFragment : Fragment(R.layout.fragment_load_fire_challenge_path) {
    private lateinit var fireChallengePathLoadBinding: FragmentLoadFireChallengePathBinding

    private val fireChallengePathLoadViewModel by viewModel<FireChallengePathLoadViewModel>()

    private val fireChallengePathSharedPreference by inject<FireChallengePathSharedPreference>()

    private var fireChallengePathUrl = ""

    private val fireChallengePathRequestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            fireChallengePathNavigateToSuccess(fireChallengePathUrl)
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                fireChallengePathSharedPreference.fireChallengePathNotificationRequest =
                    (System.currentTimeMillis() / 1000) + 259200
                fireChallengePathNavigateToSuccess(fireChallengePathUrl)
            } else {
                fireChallengePathNavigateToSuccess(fireChallengePathUrl)
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fireChallengePathLoadBinding = FragmentLoadFireChallengePathBinding.bind(view)

        fireChallengePathLoadBinding.fireChallengePathGrandButton.setOnClickListener {
            val fireChallengePathPermission = Manifest.permission.POST_NOTIFICATIONS
            fireChallengePathRequestNotificationPermission.launch(fireChallengePathPermission)
            fireChallengePathSharedPreference.fireChallengePathNotificationRequestedBefore = true
        }

        fireChallengePathLoadBinding.fireChallengePathSkipButton.setOnClickListener {
            fireChallengePathSharedPreference.fireChallengePathNotificationRequest =
                (System.currentTimeMillis() / 1000) + 259200
            fireChallengePathNavigateToSuccess(fireChallengePathUrl)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                fireChallengePathLoadViewModel.fireChallengePathHomeScreenState.collect {
                    when (it) {
                        is FireChallengePathLoadViewModel.FireChallengePathHomeScreenState.FireChallengePathLoading -> {

                        }

                        is FireChallengePathLoadViewModel.FireChallengePathHomeScreenState.FireChallengePathError -> {
                            requireActivity().startActivity(
                                Intent(
                                    requireContext(),
                                    MainActivity::class.java
                                )
                            )
                            requireActivity().finish()
                        }

                        is FireChallengePathLoadViewModel.FireChallengePathHomeScreenState.FireChallengePathSuccess -> {
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
                                val fireChallengePathPermission = Manifest.permission.POST_NOTIFICATIONS
                                val fireChallengePathPermissionRequestedBefore = fireChallengePathSharedPreference.fireChallengePathNotificationRequestedBefore

                                if (ContextCompat.checkSelfPermission(requireContext(), fireChallengePathPermission) == PackageManager.PERMISSION_GRANTED) {
                                    fireChallengePathNavigateToSuccess(it.data)
                                } else if (!fireChallengePathPermissionRequestedBefore && (System.currentTimeMillis() / 1000 > fireChallengePathSharedPreference.fireChallengePathNotificationRequest)) {
                                    // первый раз — показываем UI для запроса
                                    fireChallengePathLoadBinding.fireChallengePathNotiGroup.visibility = View.VISIBLE
                                    fireChallengePathLoadBinding.fireChallengePathLoadingGroup.visibility = View.GONE
                                    fireChallengePathUrl = it.data
                                } else if (shouldShowRequestPermissionRationale(fireChallengePathPermission)) {
                                    // временный отказ — через 3 дня можно показать
                                    if (System.currentTimeMillis() / 1000 > fireChallengePathSharedPreference.fireChallengePathNotificationRequest) {
                                        fireChallengePathLoadBinding.fireChallengePathNotiGroup.visibility = View.VISIBLE
                                        fireChallengePathLoadBinding.fireChallengePathLoadingGroup.visibility = View.GONE
                                        fireChallengePathUrl = it.data
                                    } else {
                                        fireChallengePathNavigateToSuccess(it.data)
                                    }
                                } else {
                                    // навсегда отклонено — просто пропускаем
                                    fireChallengePathNavigateToSuccess(it.data)
                                }
                            } else {
                                fireChallengePathNavigateToSuccess(it.data)
                            }
                        }

                        FireChallengePathLoadViewModel.FireChallengePathHomeScreenState.FireChallengePathNotInternet -> {
                            fireChallengePathLoadBinding.fireChallengePathStateGroup.visibility = View.VISIBLE
                            fireChallengePathLoadBinding.fireChallengePathLoadingGroup.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }


    private fun fireChallengePathNavigateToSuccess(data: String) {
        findNavController().navigate(
            R.id.action_fireChallengePathLoadFragment_to_fireChallengePathV,
            bundleOf(FIRE_CHALLENGE_PATH_D to data)
        )
    }

    companion object {
        const val FIRE_CHALLENGE_PATH_D = "fireChallengePathData"
    }
}