import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:spiderzone/core/theme/app_colors.dart';
import 'package:spiderzone/core/theme/app_theme.dart';

void main() {
  testWidgets('dark theme uses SpiderZone primary color', (tester) async {
    await tester.pumpWidget(
      MaterialApp(
        theme: AppTheme.dark,
        home: const Scaffold(
          body: Center(child: Text('test')),
        ),
      ),
    );

    final theme = Theme.of(tester.element(find.text('test')));
    expect(theme.colorScheme.primary, AppColors.primary);
    expect(theme.scaffoldBackgroundColor, AppColors.background);
  });
}
