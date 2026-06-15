import 'package:flutter/material.dart';
import 'package:spiderzone/core/theme/app_colors.dart';
import 'package:spiderzone/core/theme/app_tokens.dart';

/// Karta gatunku / zwierzęcia z lekkim hover (scale) na tap.
class SzCard extends StatefulWidget {
  const SzCard({
    super.key,
    required this.child,
    this.onTap,
    this.padding = const EdgeInsets.all(AppTokens.paddingCard),
  });

  final Widget child;
  final VoidCallback? onTap;
  final EdgeInsets padding;

  @override
  State<SzCard> createState() => _SzCardState();
}

class _SzCardState extends State<SzCard> {
  bool _pressed = false;

  @override
  Widget build(BuildContext context) {
    final scale = _pressed ? 0.98 : 1.0;
    return AnimatedScale(
      scale: scale,
      duration: AppTokens.durationFast,
      curve: Curves.easeOut,
      child: Card(
        clipBehavior: Clip.antiAlias,
        child: InkWell(
          onTap: widget.onTap,
          onHighlightChanged: (v) => setState(() => _pressed = v),
          child: Padding(
            padding: widget.padding,
            child: DefaultTextStyle(
              style: const TextStyle(color: AppColors.textPrimary),
              child: widget.child,
            ),
          ),
        ),
      ),
    );
  }
}
