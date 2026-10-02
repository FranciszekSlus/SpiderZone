import 'package:spiderzone/features/species/domain/species.dart';
import 'package:spiderzone/features/species/domain/taxonomy_labels.dart';

/// Węzeł drzewa taksonomii na Home.
///
/// Nowy CSV: kingdom → phylum → class → order → family → species.
class TaxonomyNode {
  const TaxonomyNode({
    required this.title,
    this.subtitle,
    this.children = const [],
    this.speciesId,
  });

  final String title;
  final String? subtitle;
  final List<TaxonomyNode> children;
  final String? speciesId;

  bool get isLeaf => children.isEmpty;
  bool get isSpecies => speciesId != null;
}

TaxonomyNode buildTaxonomyTree(List<Species> species) {
  if (species.isEmpty) {
    return const TaxonomyNode(
      title: 'Baza gatunków',
      subtitle: 'Brak wpisów w katalogu',
    );
  }

  final hasKingdom = species.any((s) => s.kingdom.trim().isNotEmpty);
  final hasOrder = species.any((s) => s.taxonomicOrder.trim().isNotEmpty);

  if (hasKingdom || hasOrder) {
    final kingdomNodes = _groupSorted(
      species,
      (s) => s.kingdom.ifBlank('Animalia'),
    ).entries.map((e) => _buildKingdomBranch(e.key, e.value)).toList();

    return TaxonomyNode(
      title: 'Terrarium',
      subtitle: '${species.length} gatunków',
      children: kingdomNodes,
    );
  }

  // Legacy: gromada → rząd → podrząd → rodzina → gatunek
  final phylumNodes = _groupSorted(species, (s) => s.phylum.ifBlank('Inne'))
      .entries
      .map((e) => _buildLegacyPhylumBranch(e.key, e.value))
      .toList();

  return TaxonomyNode(
    title: 'Terrarium',
    subtitle: '${species.length} gatunków',
    children: phylumNodes,
  );
}

TaxonomyNode _buildKingdomBranch(String kingdom, List<Species> species) {
  final phylumNodes = _groupSorted(species, (s) => s.phylum.ifBlank('—'))
      .entries
      .map((e) => _buildPhylumBranch(e.key, e.value))
      .toList();
  return _branch(title: kingdom, levelLabel: 'Królestwo', children: phylumNodes);
}

TaxonomyNode _buildPhylumBranch(String phylum, List<Species> species) {
  final classNodes = _groupSorted(species, (s) => s.taxonomicClass.ifBlank('—'))
      .entries
      .map((e) => _buildClassBranch(e.key, e.value))
      .toList();
  return _branch(title: phylum, levelLabel: 'Typ', children: classNodes);
}

TaxonomyNode _buildClassBranch(String className, List<Species> species) {
  final orderNodes = _groupSorted(species, (s) => s.taxonomicOrder.ifBlank('—'))
      .entries
      .map((e) => _buildOrderBranch(e.key, e.value))
      .toList();
  return _branch(title: className, levelLabel: 'Gromada', children: orderNodes);
}

TaxonomyNode _buildOrderBranch(String order, List<Species> species) {
  return _branch(
    title: order,
    levelLabel: 'Rząd',
    children: _buildFamilyBranches(species),
  );
}

List<TaxonomyNode> _buildFamilyBranches(List<Species> species) {
  return _groupSorted(species, (s) => s.family.ifBlank('Rodzina nieznana'))
      .entries
      .map((e) {
        final leaves = [...e.value]
          ..sort((a, b) => a.latinName.toLowerCase().compareTo(b.latinName.toLowerCase()));
        return _branch(
          title: e.key,
          levelLabel: 'Rodzina',
          children: leaves.map(_speciesLeaf).toList(),
        );
      })
      .toList();
}

TaxonomyNode _buildLegacyPhylumBranch(String phylum, List<Species> species) {
  final orderNodes = _groupSorted(species, (s) => s.taxonomicClass.ifBlank('—'))
      .entries
      .map((e) => _buildLegacyOrderBranch(e.key, e.value))
      .toList();
  return _branch(title: phylum, levelLabel: 'Gromada', children: orderNodes);
}

TaxonomyNode _buildLegacyOrderBranch(String order, List<Species> species) {
  final withSuborder = species.where((s) => s.suborder.trim().isNotEmpty).toList();
  final withoutSuborder = species.where((s) => s.suborder.trim().isEmpty).toList();
  final children = <TaxonomyNode>[];

  for (final entry in _groupSorted(withSuborder, (s) => s.suborder.trim()).entries) {
    children.add(
      _branch(
        title: entry.key,
        levelLabel: 'Podrząd',
        children: _buildFamilyBranches(entry.value),
      ),
    );
  }
  if (withoutSuborder.isNotEmpty) {
    children.addAll(_buildFamilyBranches(withoutSuborder));
  }
  return _branch(title: order, levelLabel: 'Rząd', children: children);
}

TaxonomyNode _speciesLeaf(Species species) => TaxonomyNode(
      title: species.latinName,
      subtitle: species.commonName.trim().isEmpty ? null : species.commonName,
      speciesId: species.id,
    );

TaxonomyNode _branch({
  required String title,
  required String levelLabel,
  required List<TaxonomyNode> children,
}) {
  final count = children.fold<int>(0, (sum, n) => sum + _countLeaves(n));
  final polish = polishTaxonShort(title);
  final hasTranslation = polish.toLowerCase() != title.trim().toLowerCase();
  return TaxonomyNode(
    title: polish,
    subtitle: hasTranslation
        ? '$title · $levelLabel · $count gat.'
        : '$levelLabel · $count gat.',
    children: children,
  );
}

int _countLeaves(TaxonomyNode node) =>
    node.isLeaf ? 1 : node.children.fold(0, (sum, n) => sum + _countLeaves(n));

Map<String, List<Species>> _groupSorted(
  List<Species> species,
  String Function(Species) keyOf,
) {
  final map = <String, List<Species>>{};
  for (final s in species) {
    map.putIfAbsent(keyOf(s), () => []).add(s);
  }
  final keys = map.keys.toList()
    ..sort((a, b) => a.toLowerCase().compareTo(b.toLowerCase()));
  return {for (final k in keys) k: map[k]!};
}

extension on String {
  String ifBlank(String fallback) => trim().isEmpty ? fallback : this;
}
