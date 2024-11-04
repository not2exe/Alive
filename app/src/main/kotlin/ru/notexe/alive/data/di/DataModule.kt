package ru.notexe.alive.data.di

import androidx.room.Room
import org.koin.dsl.module
import ru.notexe.alive.data.FramesDao
import ru.notexe.alive.data.FramesDatabase

fun dataModule() = module {
    single<FramesDatabase> {
        Room.databaseBuilder(
            get(),
            FramesDatabase::class.java,
            FramesDatabase.FRAMES_DB
        ).build()
    }

    factory<FramesDao> { get<FramesDatabase>().dao }
}