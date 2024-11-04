package ru.notexe.alive.data.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.notexe.alive.data.models.ChangeDto

internal class LineListConverters {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @TypeConverter
    fun lineListToString(list: List<ChangeDto>): String = json.encodeToString(list)

    @TypeConverter
    fun stringToLineList(string: String): List<ChangeDto> = json.decodeFromString(string)
}