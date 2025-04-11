package com.ivangarzab.resources.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = darkBackground,
    surface = darkSurface,
    surfaceVariant = darkSurfaceAlt,
    onBackground = darkText,
    onSurface = darkSubtext,
    onSurfaceVariant = darkSubtextAlt
)

val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = lightBackground,
    surface = lightSurface,
    surfaceVariant = lightSurfaceAlt,
    onBackground = lightText,
    onSurface = lightSubtext,
    onSurfaceVariant = lightSubtextAlt
)

@Composable
fun TalkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        /* TODO: Turn ON dynamic colors, if we want our app to assimilate the
            User's wallpaper or preferred color palette.
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }*/
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = defaultTypography(darkTheme),
        content = content
    )
}