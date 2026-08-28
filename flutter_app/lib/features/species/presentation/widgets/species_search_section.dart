import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:spiderzone/core/routing/app_routes.dart';
import 'package:spiderzone/core/theme/app_colors.dart';
import 'package:spiderzone/core/theme/app_tokens.dart';
import 'package:spiderzone/core/widgets/sz_card.dart';
import 'package:spiderzone/features/species/application/species_providers.dart';
import 'package:spiderzone/features/species/domain/species.dart';
import 'package:spiderzone/features/species/domain/species_filters.dart';

class SpeciesSearchSection extends ConsumerWidget {
  const SpeciesSearchSection({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final query = ref.watch(speciesSearchQueryProvider);
    final filters = ref.watch(speciesFiltersProvider);
    final catalog = ref.watch(speciesCatalogProvider);
    final results = ref.watch(speciesSearchResultsProvider);

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          'Wyszukiwarka gatunków',
          style: Theme.of(context).textTheme.titleMedium?.copyWith(
                fontWeight: FontWeight.w600,
                color: AppColors.textPrimary,
              ),
        ),
        const SizedBox(height: 8),
        TextField(
          onChanged: (value) =>
              ref.read(speciesSearchQueryProvider.notifier).state = value,
          decoration: InputDecoration(
            hintText: 'np. metallica, Brachypelma, białokolankowy…',
            prefixIcon: const Icon(Icons.search, color: AppColors.textSecondary),
            suffixIcon: query.isNotEmpty
                ? IconButton(
                    icon: const Icon(Icons.clear, color: AppColors.textSecondary),
                    onPressed: () =>
                        ref.read(speciesSearchQueryProvider.notifier).state = '',
                  )
                : null,
            filled: true,
            fillColor: AppColors.surface,
            border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(AppTokens.radiusButton),
              borderSide: BorderSide.none,
            ),
          ),
          style: const TextStyle(color: AppColors.textPrimary),
        ),
        const SizedBox(height: 10),
        _FilterChips(filters: filters),
        const SizedBox(height: 12),
        catalog.when(
          loading: () => const Center(
            child: Padding(
              padding: EdgeInsets.all(24),
              child: CircularProgressIndicator(color: AppColors.primary),
            ),
          ),
          error: (e, _) => Text(
            'Nie udało się wczytać bazy gatunków: $e',
            style: const TextStyle(color: AppColors.danger),
          ),
          data: (species) {
            if (query.isEmpty && !filters.hasActiveFilters) {
              return _CatalogSummary(count: species.length);
            }
            return results.when(
              loading: () => const SizedBox.shrink(),
              error: (e, _) => Text('Błąd wyszukiwania: $e'),
              data: (hits) => _SearchResults(query: query, results: hits),
            );
          },
        ),
      ],
    );
  }
}

class _CatalogSummary extends StatelessWidget {
  const _CatalogSummary({required this.count});

  final int count;

  @override
  Widget build(BuildContext context) {
    return SzCard(
      child: Row(
        children: [
          const Icon(Icons.menu_book_outlined, color: AppColors.primary),
          const SizedBox(width: 12),
          Expanded(
            child: Text(
              '$count gatunków w lokalnej bazie. Wpisz litery, aby wyszukać po nazwie łacińskiej lub polskiej.',
              style: const TextStyle(color: AppColors.textSecondary),
            ),
          ),
        ],
      ),
    );
  }
}

class _FilterChips extends ConsumerWidget {
  const _FilterChips({required this.filters});

  final SpeciesFilters filters;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final notifier = ref.read(speciesFiltersProvider.notifier);

    return Wrap(
      spacing: 8,
      runSpacing: 8,
      children: [
        FilterChip(
          label: const Text('Ptaszniki'),
          selected: filters.onlyTheraphosidae,
          onSelected: (selected) => notifier.state = filters.copyWith(
            onlyTheraphosidae: selected,
          ),
        ),
        FilterChip(
          label: const Text('Z nazwą PL'),
          selected: filters.onlyWithPolishName,
          onSelected: (selected) => notifier.state = filters.copyWith(
            onlyWithPolishName: selected,
          ),
        ),
        ActionChip(
          avatar: const Icon(Icons.tune, size: 18),
          label: Text(filters.family ?? 'Rodzina'),
          onPressed: () => _showFamilyPicker(context, ref),
        ),
        if (filters.hasActiveFilters)
          ActionChip(
            label: const Text('Wyczyść filtry'),
            onPressed: () => notifier.state = SpeciesFilters.empty,
          ),
      ],
    );
  }

  Future<void> _showFamilyPicker(BuildContext context, WidgetRef ref) async {
    final families = ref.read(speciesFamiliesProvider).valueOrNull ?? [];
    if (families.isEmpty || !context.mounted) return;

    final selected = await showModalBottomSheet<String?>(
      context: context,
      backgroundColor: AppColors.surface,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder: (context) {
        return SafeArea(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Padding(
                padding: EdgeInsets.all(16),
                child: Text(
                  'Filtruj po rodzinie',
                  style: TextStyle(
                    fontWeight: FontWeight.bold,
                    color: AppColors.textPrimary,
                  ),
                ),
              ),
              ListTile(
                title: const Text('Wszystkie rodziny'),
                onTap: () => Navigator.pop(context, ''),
              ),
              Flexible(
                child: ListView.builder(
                  shrinkWrap: true,
                  itemCount: families.length,
                  itemBuilder: (context, index) {
                    final family = families[index];
                    return ListTile(
                      title: Text(family),
                      onTap: () => Navigator.pop(context, family),
                    );
                  },
                ),
              ),
            ],
          ),
        );
      },
    );

    if (selected == null) return;
    final notifier = ref.read(speciesFiltersProvider.notifier);
    notifier.state = selected.isEmpty
        ? filters.copyWith(clearFamily: true)
        : filters.copyWith(family: selected);
  }
}

class _SearchResults extends StatelessWidget {
  const _SearchResults({required this.query, required this.results});

  final String query;
  final List<Species> results;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          query.isNotEmpty
              ? 'Wyniki (${results.length}) — wyszukiwanie na bieżąco'
              : 'Wyniki (${results.length})',
          style: const TextStyle(
            fontWeight: FontWeight.w600,
            color: AppColors.textPrimary,
          ),
        ),
        const SizedBox(height: 8),
        if (results.isEmpty)
          const Text(
            'Brak pasujących gatunków. Spróbuj innej części nazwy łacińskiej lub zwyczajowej.',
            style: TextStyle(color: AppColors.textSecondary),
          )
        else
          ListView.separated(
            shrinkWrap: true,
            physics: const NeverScrollableScrollPhysics(),
            itemCount: results.length,
            separatorBuilder: (_, __) => const SizedBox(height: 8),
            itemBuilder: (context, index) {
              final species = results[index];
              return SzCard(
                onTap: () => context.push(AppRoutes.speciesDetail(species.id)),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      species.latinName,
                      style: const TextStyle(
                        fontWeight: FontWeight.w600,
                        color: AppColors.textPrimary,
                      ),
                    ),
                    if (species.commonName.isNotEmpty) ...[
                      const SizedBox(height: 4),
                      Text(
                        species.commonName,
                        style: const TextStyle(color: AppColors.textSecondary),
                      ),
                    ],
                    if (species.family.isNotEmpty) ...[
                      const SizedBox(height: 4),
                      Text(
                        species.family,
                        style: const TextStyle(
                          fontSize: 12,
                          color: AppColors.textSecondary,
                        ),
                      ),
                    ],
                  ],
                ),
              );
            },
          ),
      ],
    );
  }
}
