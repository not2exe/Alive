package ru.notexe.alive.presentation.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.notexe.alive.presentation.main.stateholder.FramesPresentationMapper
import ru.notexe.alive.presentation.main.stateholder.AliveMainViewModel
import ru.notexe.alive.presentation.settings.stateholder.SettingsViewModel

internal fun presentationModule() = module {
    viewModel {
        AliveMainViewModel(
            framesRepository = get(),
            framesPresentationMapper = get()
        )
    }
    factory { FramesPresentationMapper() }

    viewModel {
        SettingsViewModel(
            settingsRepository = get(),
        )
    }
}