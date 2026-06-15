import 'package:flutter/material.dart';
import 'package:spiderzone/core/theme/app_colors.dart';

/// Przypomnienia przeniesione do ustawień profilu.
class RemindersSettingsSection extends StatelessWidget {
  const RemindersSettingsSection({super.key});

  @override
  Widget build(BuildContext context) {
    return const Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          'Przypomnienia',
          style: TextStyle(
            fontWeight: FontWeight.bold,
            color: AppColors.textPrimary,
          ),
        ),
        SizedBox(height: 6),
        Text(
          'Karmienie, sprzątanie, zraszanie, linienie — moduł w ustawieniach (następny etap).',
          style: TextStyle(color: AppColors.textSecondary),
        ),
      ],
    );
  }
}
