package com.ivangarzab.resources.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun defaultTypography(darkTheme: Boolean = isSystemInDarkTheme()): Typography {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    return Typography(
        displayLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 42.sp,
            lineHeight = 64.sp,
            letterSpacing = 0.5.sp,
            color = colorScheme.onSurface
        ),
        displayMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = 0.5.sp,
            color = colorScheme.onSurface
        ),
        displaySmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp,
            color = colorScheme.onBackground
        ),
        headlineLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp,
            color = colorScheme.onBackground
        ),
        headlineMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp,
            color = colorScheme.onBackground
        ),
        titleLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp,
            color = colorScheme.onSurface
        ),
        titleMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp,
            color = colorScheme.onSurfaceVariant
        ),
        bodyLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
            color = colorScheme.onSurface
        ),
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp,
            color = colorScheme.onSurfaceVariant
        )
    )
}