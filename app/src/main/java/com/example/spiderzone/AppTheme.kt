package com.example.spiderzone

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class AppThemeMode(val storageValue: String, val label: String) {
    SYSTEM("system", "Domyślny (system)"),
    LIGHT("light", "Jasny"),
    DARK("dark", "Ciemny");

    companion object {
        fun fromStorage(value: String?): AppThemeMode =
            entries.firstOrNull { it.storageValue == value } ?: SYSTEM
    }
}

class AppThemePreferences(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getThemeMode(): AppThemeMode = AppThemeMode.fromStorage(prefs.getString(KEY_THEME_MODE, null))

    fun setThemeMode(mode: AppThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.storageValue).apply()
    }

    companion object {
        private const val PREFS_NAME = "spiderzone_app"
        private const val KEY_THEME_MODE = "theme_mode"
    }
}

data class SpiderZonePalette(
    val background: Color,
    val surface: Color,
    val primary: Color,
    val primaryHover: Color,
    val textPrimary: Color,
    val textSecondary: Color
)

private val DarkPalette = SpiderZonePalette(
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    primary = Color(0xFF4CAF50),
    primaryHover = Color(0xFF81C784),
    textPrimary = Color(0xFFE0E0E0),
    textSecondary = Color(0xFFA0A0A0)
)

private val LightPalette = SpiderZonePalette(
    background = Color(0xFFF4F6F4),
    surface = Color(0xFFFFFFFF),
    primary = Color(0xFF2E7D32),
    primaryHover = Color(0xFF43A047),
    textPrimary = Color(0xFF1B1B1B),
    textSecondary = Color(0xFF5F6368)
)

private val LocalSpiderZonePalette = compositionLocalOf { DarkPalette }

@Composable
fun resolveDarkTheme(mode: AppThemeMode): Boolean = when (mode) {
    AppThemeMode.DARK -> true
    AppThemeMode.LIGHT -> false
    AppThemeMode.SYSTEM -> isSystemInDarkTheme()
}

object SpiderZoneColors {
    val Background: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalSpiderZonePalette.current.background

    val Surface: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalSpiderZonePalette.current.surface

    val Primary: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalSpiderZonePalette.current.primary

    val PrimaryHover: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalSpiderZonePalette.current.primaryHover

    val TextPrimary: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalSpiderZonePalette.current.textPrimary

    val TextSecondary: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalSpiderZonePalette.current.textSecondary
}

@Composable
fun SpiderZoneTheme(
    mode: AppThemeMode,
    content: @Composable () -> Unit
) {
    val darkTheme = resolveDarkTheme(mode)
    val palette = remember(darkTheme) { if (darkTheme) DarkPalette else LightPalette }
    val colorScheme = remember(palette, darkTheme) {
        if (darkTheme) {
            darkColorScheme(
                primary = palette.primary,
                background = palette.background,
                surface = palette.surface,
                onPrimary = Color.Black,
                onBackground = palette.textPrimary,
                onSurface = palette.textPrimary
            )
        } else {
            lightColorScheme(
                primary = palette.primary,
                background = palette.background,
                surface = palette.surface,
                onPrimary = Color.White,
                onBackground = palette.textPrimary,
                onSurface = palette.textPrimary
            )
        }
    }
    CompositionLocalProvider(LocalSpiderZonePalette provides palette) {
        MaterialTheme(colorScheme = colorScheme, content = content)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ThemeModeSelector(
    selected: AppThemeMode,
    onSelected: (AppThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Wygląd aplikacji", fontWeight = FontWeight.Bold)
        Text(
            "Motyw",
            style = MaterialTheme.typography.labelMedium,
            color = SpiderZoneColors.TextSecondary
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppThemeMode.entries.forEach { mode ->
                FilterChip(
                    selected = selected == mode,
                    onClick = { onSelected(mode) },
                    label = { Text(mode.label) }
                )
            }
        }
    }
}
