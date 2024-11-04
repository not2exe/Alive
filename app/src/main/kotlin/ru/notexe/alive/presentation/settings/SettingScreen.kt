package ru.notexe.alive.presentation.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.notexe.alive.R
import ru.notexe.alive.presentation.settings.stateholder.SettingsViewModel
import ru.notexe.alive.theme.AliveTheme

@Composable
internal fun SettingsScreen(
    settingsViewModel: SettingsViewModel = koinViewModel(),
) {
    val state by settingsViewModel.state.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AliveTheme.tokens.background)
            .safeDrawingPadding()
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        TextField(
            label = {
                Text(
                    text = stringResource(R.string.generate_count),
                    color = AliveTheme.tokens.primary,
                )
            },
            modifier = Modifier.fillMaxWidth(),
            value = state.countGenerateInput,
            onValueChange = settingsViewModel::onGenerateInputValueChange,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = settingsViewModel::onGenerateClick
        ) {
            Text(
                text = stringResource(R.string.generate),
                color = AliveTheme.tokens.primary,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = settingsViewModel::onDeleteClick,
        ) {
            Text(
                text = stringResource(R.string.delete_all),
                color = AliveTheme.tokens.primary,
            )
        }
    }
    if (state.isGenerateInProgress) {
        Dialog(
            onDismissRequest = settingsViewModel::onDismissGenerateDialog,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Text(
                    text = stringResource(R.string.generated, state.generatedCount),
                    color = AliveTheme.tokens.primary,
                )
                Button(onClick = settingsViewModel::onDismissGenerateDialog) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = AliveTheme.tokens.primary,
                    )
                }
            }
        }
        BackHandler(onBack = settingsViewModel::onDismissGenerateDialog)
    }
    if (state.deleteInProgress) {
        Dialog(
            onDismissRequest = {},
        ) {
            Text(
                text = stringResource(R.string.deleting_in_progress),
                color = AliveTheme.tokens.primary,
            )
        }
    }
}