import 'package:flutter/material.dart';

/// Paleta SpiderZone (dark-first, terrarium vibe).
abstract final class AppColors {
  // Tło
  static const background = Color(0xFF121212);
  static const surface = Color(0xFF1E1E1E);
  static const surfaceElevated = Color(0xFF2A2A2A);

  // Akcenty
  static const primary = Color(0xFF4CAF50);
  static const primaryHover = Color(0xFF81C784);
  static const accentOrange = Color(0xFFFF7043);
  static const accentOrangeHover = Color(0xFFFFA270);

  // Tekst
  static const textPrimary = Color(0xFFE0E0E0);
  static const textSecondary = Color(0xFFA0A0A0);

  // Stany
  static const danger = Color(0xFFE53935);
  static const dangerHover = Color(0xFFEF5350);
}
