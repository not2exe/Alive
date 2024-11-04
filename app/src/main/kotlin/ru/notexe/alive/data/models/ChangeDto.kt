package ru.notexe.alive.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class ChangeDto(
    @SerialName("dragAmountX")
    val dragAmountX: Float,
    @SerialName("dragAmountY")
    val dragAmountY: Float,
)