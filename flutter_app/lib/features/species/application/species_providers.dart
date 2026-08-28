import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:spiderzone/features/species/data/species_csv_loader.dart';
import 'package:spiderzone/features/species/domain/species.dart';
import 'package:spiderzone/features/species/domain/species_filters.dart';
import 'package:spiderzone/features/species/domain/species_search.dart';

final speciesCatalogProvider = FutureProvider<List<Species>>((ref) async {
  return SpeciesCsvLoader().loadFromAssets();
});

final speciesFamiliesProvider = Provider<AsyncValue<List<String>>>((ref) {
  return ref.watch(speciesCatalogProvider).whenData(distinctFamilies);
});

final speciesSearchQueryProvider = StateProvider<String>((ref) => '');

final speciesFiltersProvider = StateProvider<SpeciesFilters>((ref) {
  return SpeciesFilters.empty;
});

final speciesSearchResultsProvider = Provider<AsyncValue<List<Species>>>((ref) {
  final catalog = ref.watch(speciesCatalogProvider);
  final query = ref.watch(speciesSearchQueryProvider);
  final filters = ref.watch(speciesFiltersProvider);

  return catalog.whenData(
    (species) => searchSpecies(species, query: query, filters: filters),
  );
});

final speciesByIdProvider = Provider.family<AsyncValue<Species?>, String>((ref, id) {
  return ref.watch(speciesCatalogProvider).whenData((catalog) {
    for (final species in catalog) {
      if (species.id == id ||
          species.latinName.toLowerCase() == id.toLowerCase()) {
        return species;
      }
    }
    return null;
  });
});
