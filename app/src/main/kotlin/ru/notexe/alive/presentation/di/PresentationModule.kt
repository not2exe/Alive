package ru.notexe.alive.presentation.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.notexe.alive.presentation.FramesPresentationMapper
import ru.notexe.alive.presentation.stateholder.AliveMainViewModel

internal fun presentationModule() = module {
    viewModel {
        AliveMainViewModel(
            framesRepository = get(),
            framesPresentationMapper = get()
        )
    }
    factory { FramesPresentationMapper() }
}