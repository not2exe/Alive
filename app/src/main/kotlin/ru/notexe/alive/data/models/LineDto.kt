package ru.notexe.alive.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class LineDto(
    @SerialName("startX")
    val startX: Float,
    @SerialName("startY")
    val startY: Float,
    @SerialName("endX")
    val endX: Float,
    @SerialName("endY")
    val endY: Float,
)