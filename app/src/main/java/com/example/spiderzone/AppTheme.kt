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

@Composable
fun resolveDarkTheme(mode: AppThemeMode): Boolean = when (mode) {
    AppThemeMode.DARK -> true
    AppThemeMode.LIGHT -> false
    AppThemeMode.SYSTEM -> isSystemInDarkTheme()
}

@Composable
fun SpiderZoneTheme(
    mode: AppThemeMode,
    content: @Composable () -> Unit
) {
    val darkTheme = resolveDarkTheme(mode)
    val palette = remember(darkTheme) { if (darkTheme) TerrariumNightPalette else LightPalette }
    val colorScheme = remember(palette, darkTheme) {
        if (darkTheme) {
            darkColorScheme(
                primary = palette.primary,
                secondary = palette.accent,
                error = palette.danger,
                background = palette.background,
                surface = palette.surface,
                onPrimary = Color.Black,
                onBackground = palette.textPrimary,
                onSurface = palette.textPrimary
            )
        } else {
            lightColorScheme(
                primary = palette.primary,
                secondary = palette.accent,
                error = palette.danger,
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
