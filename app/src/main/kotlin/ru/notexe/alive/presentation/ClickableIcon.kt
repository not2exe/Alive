package ru.notexe.alive.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import ru.notexe.alive.ui.theme.AliveTheme

@Composable
internal fun ClickableIcon(
    @DrawableRes
    iconResource: Int,
    modifier: Modifier = Modifier,
    innerPaddings: PaddingValues = PaddingValues(),
    tint: Color = AliveTheme.tokens.primary,
    onClick: () -> Unit,
) {
    Icon(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(innerPaddings),
        painter = painterResource(id = iconResource),
        tint = tint,
        contentDescription = null,
    )
}