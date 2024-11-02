package ru.notexe.alive.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

@Immutable
internal data class Shapes(
    val roundedCornerShape16: RoundedCornerShape = RoundedCornerShape(16.dp),
    val roundedCornerShape20: RoundedCornerShape = RoundedCornerShape(20.dp),
)