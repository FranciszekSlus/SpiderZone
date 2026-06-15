import 'package:flutter/material.dart';
import 'package:spiderzone/core/theme/app_colors.dart';
import 'package:spiderzone/core/theme/app_tokens.dart';

enum SzButtonVariant { primary, secondary, danger }

class SzPrimaryButton extends StatelessWidget {
  const SzPrimaryButton({
    super.key,
    required this.label,
    required this.onPressed,
    this.variant = SzButtonVariant.primary,
    this.expanded = true,
  });

  final String label;
  final VoidCallback? onPressed;
  final SzButtonVariant variant;
  final bool expanded;

  @override
  Widget build(BuildContext context) {
    final Widget button;
    switch (variant) {
      case SzButtonVariant.primary:
        button = FilledButton(onPressed: onPressed, child: Text(label));
      case SzButtonVariant.secondary:
        button = OutlinedButton(onPressed: onPressed, child: Text(label));
      case SzButtonVariant.danger:
        button = FilledButton(
          onPressed: onPressed,
          style: FilledButton.styleFrom(
            backgroundColor: AppColors.danger,
            foregroundColor: Colors.white,
          ),
          child: Text(label),
        );
    }

    final wrapped = expanded
        ? SizedBox(width: double.infinity, child: button)
        : button;

    return AnimatedOpacity(
      opacity: onPressed == null ? 0.5 : 1,
      duration: AppTokens.durationFast,
      child: wrapped,
    );
  }
}
