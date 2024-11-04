package ru.notexe.alive.domain.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.notexe.alive.data.FramesMapper
import ru.notexe.alive.data.FramesRepositoryImpl
import ru.notexe.alive.domain.FramesRepository

fun domainModule() = module {
    single {
        FramesRepositoryImpl(
            framesDao = get(),
            framesMapper = get(),
            framesDatabase = get(),
        )
    } bind FramesRepository::class
    factory { FramesMapper() }
}