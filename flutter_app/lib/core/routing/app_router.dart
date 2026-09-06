import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:spiderzone/core/routing/app_routes.dart';
import 'package:spiderzone/features/collection/presentation/collection_screen.dart';
import 'package:spiderzone/features/home/presentation/home_screen.dart';
import 'package:spiderzone/features/profile/presentation/profile_screen.dart';
import 'package:spiderzone/features/breeding/presentation/breeding_screen.dart';
import 'package:spiderzone/features/shell/presentation/main_shell_screen.dart';
import 'package:spiderzone/features/species/presentation/species_detail_screen.dart';

final _rootNavigatorKey = GlobalKey<NavigatorState>();
final _shellNavigatorKey = GlobalKey<NavigatorState>();

GoRouter createAppRouter() {
  return GoRouter(
    navigatorKey: _rootNavigatorKey,
    initialLocation: AppRoutes.home,
    routes: [
      GoRoute(
        path: '/species/:id',
        parentNavigatorKey: _rootNavigatorKey,
        builder: (context, state) {
          final id = state.pathParameters['id']!;
          return SpeciesDetailScreen(speciesId: id);
        },
      ),
      ShellRoute(
        navigatorKey: _shellNavigatorKey,
        builder: (context, state, child) => MainShellScreen(child: child),
        routes: [
          GoRoute(
            path: AppRoutes.home,
            pageBuilder: (context, state) => const NoTransitionPage(
              child: HomeScreen(),
            ),
          ),
          GoRoute(
            path: AppRoutes.collection,
            pageBuilder: (context, state) => const NoTransitionPage(
              child: CollectionScreen(),
            ),
          ),
          GoRoute(
            path: AppRoutes.breeding,
            pageBuilder: (context, state) => const NoTransitionPage(
              child: BreedingScreen(),
            ),
          ),
          GoRoute(
            path: AppRoutes.profile,
            pageBuilder: (context, state) => const NoTransitionPage(
              child: ProfileScreen(),
            ),
          ),
        ],
      ),
    ],
  );
}
