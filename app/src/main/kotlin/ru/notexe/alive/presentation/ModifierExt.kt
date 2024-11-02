package ru.notexe.alive.presentation

import androidx.compose.ui.Modifier

inline fun Modifier.applyIf(
    condition: Boolean,
    transform: Modifier.() -> Modifier,
): Modifier {
    return if (condition) {
        this.then(transform())
    } else {
        this
    }
}