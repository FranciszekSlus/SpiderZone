import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:spiderzone/core/routing/app_routes.dart';
import 'package:spiderzone/core/theme/app_colors.dart';

class MainShellScreen extends StatelessWidget {
  const MainShellScreen({super.key, required this.child});

  final Widget child;

  int _indexFromLocation(String location) {
    if (location.startsWith(AppRoutes.species)) return 1;
    if (location.startsWith(AppRoutes.collection)) return 2;
    if (location.startsWith(AppRoutes.breeding)) return 3;
    if (location.startsWith(AppRoutes.profile)) return 4;
    return 0;
  }

  void _onTap(BuildContext context, int index) {
    const routes = [
      AppRoutes.home,
      AppRoutes.species,
      AppRoutes.collection,
      AppRoutes.breeding,
      AppRoutes.profile,
    ];
    context.go(routes[index]);
  }

  @override
  Widget build(BuildContext context) {
    final location = GoRouterState.of(context).uri.toString();
    final index = _indexFromLocation(location);

    return Scaffold(
      body: AnimatedSwitcher(
        duration: const Duration(milliseconds: 250),
        switchInCurve: Curves.easeOut,
        switchOutCurve: Curves.easeIn,
        transitionBuilder: (child, animation) {
          final offsetAnimation = Tween<Offset>(
            begin: const Offset(0.04, 0),
            end: Offset.zero,
          ).animate(animation);
          return FadeTransition(
            opacity: animation,
            child: SlideTransition(position: offsetAnimation, child: child),
          );
        },
        child: KeyedSubtree(
          key: ValueKey(location),
          child: child,
        ),
      ),
      bottomNavigationBar: NavigationBar(
        selectedIndex: index,
        onDestinationSelected: (i) => _onTap(context, i),
        destinations: const [
          NavigationDestination(
            icon: Icon(Icons.home_outlined),
            selectedIcon: Icon(Icons.home),
            label: 'Home',
          ),
          NavigationDestination(
            icon: Icon(Icons.search_outlined),
            selectedIcon: Icon(Icons.search),
            label: 'Gatunki',
          ),
          NavigationDestination(
            icon: Icon(Icons.pets_outlined),
            selectedIcon: Icon(Icons.pets),
            label: 'Hodowla',
          ),
          NavigationDestination(
            icon: Icon(Icons.favorite_outline),
            selectedIcon: Icon(Icons.favorite),
            label: 'Rozmnażanie',
          ),
          NavigationDestination(
            icon: Icon(Icons.person_outline),
            selectedIcon: Icon(Icons.person),
            label: 'Profil',
          ),
        ],
      ),
      backgroundColor: AppColors.background,
    );
  }
}
