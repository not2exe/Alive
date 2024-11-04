package ru.notexe.alive.presentation.settings.state

import androidx.compose.runtime.Immutable

@Immutable
data class SettingsState(
    val isGenerateInProgress: Boolean,
    val generatedCount: Int,
    val countGenerateInput: String,
    val deleteInProgress: Boolean,
)