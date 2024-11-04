package ru.notexe.alive.app.di

import ru.notexe.alive.data.di.dataModule
import ru.notexe.alive.domain.di.domainModule
import ru.notexe.alive.presentation.di.presentationModule

fun appModules() = listOf(
    dataModule(),
    domainModule(),
    presentationModule(),
)