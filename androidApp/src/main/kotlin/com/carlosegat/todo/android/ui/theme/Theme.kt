package com.carlosegat.todo.android.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = CatppuccinLatte.Mauve,
    onPrimary = CatppuccinLatte.Base,
    primaryContainer = CatppuccinLatte.Mauve,
    onPrimaryContainer = CatppuccinLatte.Base,
    secondary = CatppuccinLatte.Lavender,
    onSecondary = CatppuccinLatte.Base,
    tertiary = CatppuccinLatte.Peach,
    onTertiary = CatppuccinLatte.Base,
    background = CatppuccinLatte.Base,
    onBackground = CatppuccinLatte.Text,
    surface = CatppuccinLatte.Base,
    onSurface = CatppuccinLatte.Text,
    surfaceVariant = CatppuccinLatte.Surface0,
    onSurfaceVariant = CatppuccinLatte.Subtext0,
    outline = CatppuccinLatte.Overlay0,
    outlineVariant = CatppuccinLatte.Surface1,
    error = CatppuccinLatte.Red,
    onError = CatppuccinLatte.Base,
)

private val DarkColorScheme = darkColorScheme(
    primary = CatppuccinMocha.Mauve,
    onPrimary = CatppuccinMocha.Base,
    primaryContainer = CatppuccinMocha.Mauve,
    onPrimaryContainer = CatppuccinMocha.Base,
    secondary = CatppuccinMocha.Lavender,
    onSecondary = CatppuccinMocha.Base,
    tertiary = CatppuccinMocha.Peach,
    onTertiary = CatppuccinMocha.Base,
    background = CatppuccinMocha.Base,
    onBackground = CatppuccinMocha.Text,
    surface = CatppuccinMocha.Base,
    onSurface = CatppuccinMocha.Text,
    surfaceVariant = CatppuccinMocha.Surface0,
    onSurfaceVariant = CatppuccinMocha.Subtext0,
    outline = CatppuccinMocha.Overlay0,
    outlineVariant = CatppuccinMocha.Surface1,
    error = CatppuccinMocha.Red,
    onError = CatppuccinMocha.Base,
)

@Composable
fun TodoAppTheme(
    // App is dark-only for now (Catppuccin Mocha), regardless of the system setting.
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
