package com.atlas.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AtlasColorScheme = lightColorScheme(
    primary        = NavyBlue,
    secondary      = MediumBlue,
    tertiary       = GreenAccent,
    background     = PageBg,
    surface        = CardBg,
    onPrimary      = CardBg,
    onSecondary    = CardBg,
    onTertiary     = CardBg,
    onBackground   = TextNavy,
    onSurface      = TextNavy,
)

@Composable
fun AtlasTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = NavyBlue.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    MaterialTheme(
        colorScheme = AtlasColorScheme,
        typography  = AtlasTypography,
        content     = content
    )
}
