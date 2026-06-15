import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:spiderzone/core/routing/app_router.dart';
import 'package:spiderzone/core/theme/app_theme.dart';

final appRouterProvider = Provider<GoRouter>((ref) => createAppRouter());

class SpiderZoneApp extends ConsumerWidget {
  const SpiderZoneApp({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final router = ref.watch(appRouterProvider);

    return MaterialApp.router(
      title: 'SpiderZone',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.dark,
      darkTheme: AppTheme.dark,
      themeMode: ThemeMode.dark,
      routerConfig: router,
    );
  }
}
