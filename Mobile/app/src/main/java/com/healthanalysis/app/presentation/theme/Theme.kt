package com.healthanalysis.app.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    background = Background,
    surface = Background,
    surfaceVariant = Surface,
    onPrimary = Background,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    outline = Border
)

@Composable
fun HealthAnalysisTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
