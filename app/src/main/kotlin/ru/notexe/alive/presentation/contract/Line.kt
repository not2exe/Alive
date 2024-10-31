package ru.notexe.alive.presentation.contract

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset

@Immutable
data class Line(
    val start: Offset,
    val end: Offset,
)