import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:spiderzone/core/routing/app_routes.dart';
import 'package:spiderzone/core/theme/app_colors.dart';
import 'package:spiderzone/core/theme/app_tokens.dart';
import 'package:spiderzone/features/species/application/species_providers.dart';
import 'package:spiderzone/features/species/domain/species.dart';
import 'package:spiderzone/features/species/presentation/widgets/species_card.dart';

/// Otwiera pełnoekranową wyszukiwarkę gatunków z płynnym przejściem.
Future<void> openSpeciesSearchOverlay(BuildContext context) {
  return Navigator.of(context).push(
    PageRouteBuilder<void>(
      opaque: true,
      barrierColor: Colors.transparent,
      transitionDuration: AppTokens.durationNormal,
      reverseTransitionDuration: AppTokens.durationFast,
      pageBuilder: (context, animation, secondaryAnimation) {
        return const SpeciesSearchOverlay();
      },
      transitionsBuilder: (context, animation, secondaryAnimation, child) {
        final curved = CurvedAnimation(
          parent: animation,
          curve: Curves.easeOutCubic,
          reverseCurve: Curves.easeInCubic,
        );
        return FadeTransition(
          opacity: curved,
          child: SlideTransition(
            position: Tween<Offset>(
              begin: const Offset(0, 0.04),
              end: Offset.zero,
            ).animate(curved),
            child: child,
          ),
        );
      },
    ),
  );
}

class SpeciesSearchOverlay extends ConsumerStatefulWidget {
  const SpeciesSearchOverlay({super.key});

  @override
  ConsumerState<SpeciesSearchOverlay> createState() =>
      _SpeciesSearchOverlayState();
}

class _SpeciesSearchOverlayState extends ConsumerState<SpeciesSearchOverlay> {
  late final TextEditingController _controller;
  late final FocusNode _focusNode;

  @override
  void initState() {
    super.initState();
    final initial = ref.read(speciesSearchQueryProvider);
    _controller = TextEditingController(text: initial);
    _focusNode = FocusNode();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _focusNode.requestFocus();
    });
  }

  @override
  void dispose() {
    _controller.dispose();
    _focusNode.dispose();
    super.dispose();
  }

  void _onQueryChanged(String value) {
    ref.read(speciesSearchQueryProvider.notifier).state = value;
  }

  void _close() {
    ref.read(speciesSearchQueryProvider.notifier).state = '';
    Navigator.of(context).pop();
  }

  @override
  Widget build(BuildContext context) {
    final query = ref.watch(speciesSearchQueryProvider);
    final results = ref.watch(speciesSearchResultsProvider);

    return Scaffold(
      backgroundColor: AppColors.background,
      body: SafeArea(
        child: Column(
          children: [
            Padding(
              padding: const EdgeInsets.fromLTRB(
                AppTokens.paddingScreen,
                8,
                AppTokens.paddingScreen,
                8,
              ),
              child: Row(
                children: [
                  IconButton(
                    onPressed: _close,
                    icon: const Icon(Icons.arrow_back),
                    color: AppColors.textPrimary,
                  ),
                  Expanded(
                    child: Hero(
                      tag: 'home-species-search',
                      child: Material(
                        color: Colors.transparent,
                        child: TextField(
                          controller: _controller,
                          focusNode: _focusNode,
                          onChanged: _onQueryChanged,
                          textInputAction: TextInputAction.search,
                          decoration: InputDecoration(
                            hintText: 'Szukaj gatunku…',
                            prefixIcon: const Icon(
                              Icons.search,
                              color: AppColors.textSecondary,
                            ),
                            suffixIcon: query.isNotEmpty
                                ? IconButton(
                                    icon: const Icon(
                                      Icons.clear,
                                      color: AppColors.textSecondary,
                                    ),
                                    onPressed: () {
                                      _controller.clear();
                                      _onQueryChanged('');
                                    },
                                  )
                                : null,
                          ),
                          style: const TextStyle(color: AppColors.textPrimary),
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
            Expanded(child: _buildBody(query, results)),
          ],
        ),
      ),
    );
  }

  Widget _buildBody(String query, AsyncValue<List<Species>> results) {
    if (query.trim().isEmpty) {
      return const _SearchIdleHint();
    }

    return results.when(
      loading: () => const Center(
        child: CircularProgressIndicator(color: AppColors.primary),
      ),
      error: (e, _) => Center(
        child: Text(
          'Błąd wyszukiwania: $e',
          style: const TextStyle(color: AppColors.danger),
        ),
      ),
      data: (hits) {
        if (hits.isEmpty) {
          return const _EmptySearchState();
        }
        return ListView.separated(
          padding: const EdgeInsets.all(AppTokens.paddingScreen),
          itemCount: hits.length,
          separatorBuilder: (_, __) => const SizedBox(height: 8),
          itemBuilder: (context, index) {
            final species = hits[index];
            return SpeciesCard.fromSpecies(
              species,
              onTap: () => context.push(AppRoutes.speciesDetail(species.id)),
            );
          },
        );
      },
    );
  }
}

class _SearchIdleHint extends StatelessWidget {
  const _SearchIdleHint();

  @override
  Widget build(BuildContext context) {
    return const Padding(
      padding: EdgeInsets.all(AppTokens.paddingScreen),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            'Wpisuj litery — wyniki pojawią się na bieżąco',
            style: TextStyle(
              fontWeight: FontWeight.w600,
              color: AppColors.textPrimary,
            ),
          ),
          SizedBox(height: 8),
          Text(
            'Szukamy po nazwie łacińskiej, polskiej, rodzaju i rodzinie.',
            style: TextStyle(color: AppColors.textSecondary),
          ),
        ],
      ),
    );
  }
}

class _EmptySearchState extends StatelessWidget {
  const _EmptySearchState();

  @override
  Widget build(BuildContext context) {
    return const Padding(
      padding: EdgeInsets.all(AppTokens.paddingScreen),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(Icons.search_off, size: 48, color: AppColors.textSecondary),
          SizedBox(height: 16),
          Text(
            'Nie mamy jeszcze tego gatunku w bazie',
            textAlign: TextAlign.center,
            style: TextStyle(
              fontWeight: FontWeight.w600,
              fontSize: 16,
              color: AppColors.textPrimary,
            ),
          ),
          SizedBox(height: 8),
          Text(
            'Spróbuj innej części nazwy albo wróć później — baza będzie się rozrastać.',
            textAlign: TextAlign.center,
            style: TextStyle(color: AppColors.textSecondary),
          ),
        ],
      ),
    );
  }
}
