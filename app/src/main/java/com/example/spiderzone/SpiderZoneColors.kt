package com.example.spiderzone

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class SpiderZonePalette(
    val background: Color,
    val surface: Color,
    val surfaceAlt: Color,
    val primary: Color,
    val primaryHover: Color,
    val accent: Color,
    val danger: Color,
    val textPrimary: Color,
    val textSecondary: Color
)

internal val TerrariumNightPalette = SpiderZonePalette(
    background = Color(0xFF0E1411),
    surface = Color(0xFF17211B),
    surfaceAlt = Color(0xFF203027),
    primary = Color(0xFF6FBF73),
    primaryHover = Color(0xFFA5D6A7),
    accent = Color(0xFFD98E32),
    danger = Color(0xFFD65A4A),
    textPrimary = Color(0xFFE7EEE9),
    textSecondary = Color(0xFF9DAEA4)
)

internal val LightPalette = SpiderZonePalette(
    background = Color(0xFFF4F6F4),
    surface = Color(0xFFFFFFFF),
    surfaceAlt = Color(0xFFE7EFE8),
    primary = Color(0xFF2E7D32),
    primaryHover = Color(0xFF43A047),
    accent = Color(0xFFC77725),
    danger = Color(0xFFC44B3F),
    textPrimary = Color(0xFF1B1B1B),
    textSecondary = Color(0xFF5F6368)
)

internal val LocalSpiderZonePalette = compositionLocalOf { TerrariumNightPalette }

object SpiderZoneColors {
    val Background: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalSpiderZonePalette.current.background

    val Surface: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalSpiderZonePalette.current.surface

    val SurfaceAlt: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalSpiderZonePalette.current.surfaceAlt

    val Primary: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalSpiderZonePalette.current.primary

    val PrimaryHover: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalSpiderZonePalette.current.primaryHover

    val Accent: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalSpiderZonePalette.current.accent

    val Danger: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalSpiderZonePalette.current.danger

    val TextPrimary: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalSpiderZonePalette.current.textPrimary

    val TextSecondary: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalSpiderZonePalette.current.textSecondary
}
