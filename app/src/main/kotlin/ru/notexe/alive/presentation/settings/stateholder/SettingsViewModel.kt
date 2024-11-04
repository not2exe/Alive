package ru.notexe.alive.presentation.settings.stateholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.notexe.alive.domain.SettingsRepository
import ru.notexe.alive.presentation.settings.state.SettingsState

internal class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<SettingsState>(
        SettingsState(
            isGenerateInProgress = false,
            generatedCount = 0,
            countGenerateInput = "",
            deleteInProgress = false,
        )
    )
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    @Volatile
    private var generatingJob: Job? = null

    fun onGenerateInputValueChange(value: String) {
        _state.update {
            it.copy(
                countGenerateInput = value,
            )
        }
    }

    fun onGenerateClick() {
        val count = _state.value.countGenerateInput.trim().toIntOrNull()
        if (count != null && count > 0) {
            startGenerate(count)
        }
    }

    fun onDismissGenerateDialog() {
        generatingJob?.cancel()
        _state.update {
            it.copy(
                isGenerateInProgress = false,
                generatedCount = 0,
            )
        }
    }


    fun onDeleteClick() = viewModelScope.launch {
        _state.update {
            it.copy(
                deleteInProgress = true,
            )
        }

        settingsRepository.deleteAll()

        _state.update {
            it.copy(
                deleteInProgress = false,
            )
        }
    }

    private fun startGenerate(count: Int) {
        generatingJob = viewModelScope.launch {
            launch {
                _state.update {
                    it.copy(
                        isGenerateInProgress = true,
                    )
                }
                settingsRepository.startGenerate(count)
                _state.update {
                    it.copy(
                        isGenerateInProgress = false,
                    )
                }
            }
            launch {
                settingsRepository.generatedFramesCount.collect { count ->
                    _state.update { state ->
                        state.copy(
                            generatedCount = count,
                        )
                    }
                }
            }
        }
    }
}