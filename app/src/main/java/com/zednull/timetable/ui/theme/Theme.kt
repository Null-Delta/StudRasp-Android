package com.zednull.timetable.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
        primary = AppEnableDark,
        secondary = AppDisableDark,
        background = AppBackgroundDark,
        onSecondary = AppEnableLightDark,
        onPrimary = AppDisableLightDark,
        primaryVariant = AppChangedGlobal,
        secondaryVariant = AppChangedGlobalText
)

private val LightColorPalette = lightColors(
        primary = AppEnable,
        secondary = AppDisable,
        background = AppBackground,
        onSecondary = AppEnableLight,
        onPrimary = AppDisableLight,
        primaryVariant = AppChangedGlobal,
        secondaryVariant = AppChangedGlobalText,
        onError = AppAlterChangedGlobalText
)

@Composable
fun TimeTableTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
    )
}