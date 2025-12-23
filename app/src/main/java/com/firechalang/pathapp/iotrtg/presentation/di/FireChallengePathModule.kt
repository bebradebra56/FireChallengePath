package com.firechalang.pathapp.iotrtg.presentation.di

import com.firechalang.pathapp.iotrtg.data.repo.FireChallengePathRepository
import com.firechalang.pathapp.iotrtg.data.shar.FireChallengePathSharedPreference
import com.firechalang.pathapp.iotrtg.data.utils.FireChallengePathPushToken
import com.firechalang.pathapp.iotrtg.data.utils.FireChallengePathSystemService
import com.firechalang.pathapp.iotrtg.domain.usecases.FireChallengePathGetAllUseCase
import com.firechalang.pathapp.iotrtg.presentation.pushhandler.FireChallengePathPushHandler
import com.firechalang.pathapp.iotrtg.presentation.ui.load.FireChallengePathLoadViewModel
import com.firechalang.pathapp.iotrtg.presentation.ui.view.FireChallengePathViFun
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val fireChallengePathModule = module {
    factory {
        FireChallengePathPushHandler()
    }
    single {
        FireChallengePathRepository()
    }
    single {
        FireChallengePathSharedPreference(get())
    }
    factory {
        FireChallengePathPushToken()
    }
    factory {
        FireChallengePathSystemService(get())
    }
    factory {
        FireChallengePathGetAllUseCase(
            get(), get(), get()
        )
    }
    factory {
        FireChallengePathViFun(get())
    }
    viewModel {
        FireChallengePathLoadViewModel(get(), get(), get())
    }
}