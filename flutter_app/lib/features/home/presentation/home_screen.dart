import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:spiderzone/core/theme/app_colors.dart';
import 'package:spiderzone/core/theme/app_tokens.dart';
import 'package:spiderzone/core/widgets/sz_card.dart';
import 'package:spiderzone/features/species/application/species_providers.dart';
import 'package:spiderzone/features/species/domain/taxonomy_node.dart';
import 'package:spiderzone/features/species/presentation/species_search_overlay.dart';
import 'package:spiderzone/features/species/presentation/widgets/taxonomy_browser.dart';

class HomeScreen extends ConsumerWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final catalog = ref.watch(speciesCatalogProvider);

    return SafeArea(
      child: catalog.when(
        loading: () => const Center(
          child: CircularProgressIndicator(color: AppColors.primary),
        ),
        error: (e, _) => Center(
          child: Padding(
            padding: const EdgeInsets.all(AppTokens.paddingScreen),
            child: Text(
              'Nie udało się wczytać bazy gatunków:\n$e',
              textAlign: TextAlign.center,
              style: const TextStyle(color: AppColors.danger),
            ),
          ),
        ),
        data: (species) {
          final root = buildTaxonomyTree(species);
          return CustomScrollView(
            slivers: [
              SliverPadding(
                padding: const EdgeInsets.fromLTRB(
                  AppTokens.paddingScreen,
                  AppTokens.paddingScreen,
                  AppTokens.paddingScreen,
                  8,
                ),
                sliver: SliverToBoxAdapter(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        'SpiderZone',
                        style: Theme.of(context)
                            .textTheme
                            .headlineMedium
                            ?.copyWith(
                              fontWeight: FontWeight.bold,
                              color: AppColors.textPrimary,
                            ),
                      ),
                      const SizedBox(height: 6),
                      Text(
                        '${species.length} gatunków w bazie — przeglądaj drzewem albo szukaj.',
                        style: const TextStyle(color: AppColors.textSecondary),
                      ),
                      const SizedBox(height: 16),
                      _SearchEntryField(
                        onTap: () => openSpeciesSearchOverlay(context),
                      ),
                      const SizedBox(height: 20),
                    ],
                  ),
                ),
              ),
              SliverPadding(
                padding: const EdgeInsets.symmetric(
                  horizontal: AppTokens.paddingScreen,
                ),
                sliver: SliverToBoxAdapter(
                  child: TaxonomyBrowser(root: root),
                ),
              ),
            ],
          );
        },
      ),
    );
  }
}

class _SearchEntryField extends StatelessWidget {
  const _SearchEntryField({required this.onTap});

  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return Hero(
      tag: 'home-species-search',
      child: Material(
        color: Colors.transparent,
        child: SzCard(
          onTap: onTap,
          padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 12),
          child: const Row(
            children: [
              Icon(Icons.search, color: AppColors.textSecondary),
              SizedBox(width: 12),
              Expanded(
                child: Text(
                  'Szukaj gatunku…',
                  style: TextStyle(
                    color: AppColors.textSecondary,
                    fontSize: 16,
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
