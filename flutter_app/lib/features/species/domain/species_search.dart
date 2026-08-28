import 'package:spiderzone/features/species/domain/species.dart';
import 'package:spiderzone/features/species/domain/species_filters.dart';

const _maxResults = 40;

List<Species> searchSpecies(
  List<Species> catalog, {
  required String query,
  SpeciesFilters filters = SpeciesFilters.empty,
}) {
  final normalizedQuery = _normalize(query);
  Iterable<Species> results = catalog;

  results = results.where(_matchesFilters(filters));

  if (normalizedQuery.isNotEmpty) {
    results = results.where((species) => _matchesQuery(species, normalizedQuery));
    results = results.toList()
      ..sort((a, b) => _compareRelevance(a, b, normalizedQuery));
  } else {
    results = results.toList()
      ..sort((a, b) => a.latinName.toLowerCase().compareTo(b.latinName.toLowerCase()));
  }

  return results.take(_maxResults).toList();
}

bool Function(Species) _matchesFilters(SpeciesFilters filters) {
  return (species) {
    if (filters.onlyTheraphosidae &&
        !species.family.toLowerCase().contains('theraphosidae')) {
      return false;
    }
    if (filters.family != null &&
        !species.family.toLowerCase().contains(filters.family!.toLowerCase())) {
      return false;
    }
    if (filters.onlyWithPolishName && !species.hasPolishName) {
      return false;
    }
    if (filters.difficulty != null &&
        !species.difficulty.toLowerCase().contains(filters.difficulty!.toLowerCase())) {
      return false;
    }
    return true;
  };
}

bool _matchesQuery(Species species, String query) {
  final latin = _normalize(species.latinName);
  final common = _normalize(species.commonName);
  final family = _normalize(species.family);
  final genus = _normalize(species.genus);

  if (latin.contains(query) ||
      common.contains(query) ||
      family.contains(query) ||
      genus.contains(query)) {
    return true;
  }

  // Wyszukiwanie po początku słów (np. "ham" → Brachypelma hamorii).
  final tokens = [
    ...latin.split(RegExp(r'\s+')),
    ...common.split(RegExp(r'\s+')),
  ];
  return tokens.any((token) => token.startsWith(query));
}

int _compareRelevance(Species a, Species b, String query) {
  final scoreA = _relevanceScore(a, query);
  final scoreB = _relevanceScore(b, query);
  if (scoreA != scoreB) return scoreB.compareTo(scoreA);
  return a.latinName.toLowerCase().compareTo(b.latinName.toLowerCase());
}

int _relevanceScore(Species species, String query) {
  final latin = _normalize(species.latinName);
  final common = _normalize(species.commonName);

  if (latin == query || common == query) return 100;
  if (latin.startsWith(query) || common.startsWith(query)) return 80;
  if (species.genus.toLowerCase() == query) return 70;
  if (latin.contains(query) || common.contains(query)) return 50;
  return 10;
}

String _normalize(String value) => value.trim().toLowerCase();

List<String> distinctFamilies(List<Species> catalog) {
  return catalog
      .map((s) => s.family.trim())
      .where((f) => f.isNotEmpty)
      .toSet()
      .toList()
    ..sort((a, b) => a.toLowerCase().compareTo(b.toLowerCase()));
}
