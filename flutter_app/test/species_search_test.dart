import 'package:flutter_test/flutter_test.dart';
import 'package:spiderzone/features/species/data/species_csv_loader.dart';
import 'package:spiderzone/features/species/domain/species.dart';
import 'package:spiderzone/features/species/domain/species_filters.dart';
import 'package:spiderzone/features/species/domain/species_search.dart';

void main() {
  final sampleCatalog = [
    const Species(
      id: 'brachypelma_hamorii',
      latinName: 'Brachypelma hamorii',
      commonName: 'Ptasznik meksykański',
      family: 'Ptaszniki (Theraphosidae)',
      phylum: 'Pajęczaki (Arachnida)',
    ),
    const Species(
      id: 'chromatopelma_cyaneopubescens',
      latinName: 'Chromatopelma cyaneopubescens',
      commonName: 'Ptasznik zielononogi',
      family: 'Ptaszniki (Theraphosidae)',
      phylum: 'Pajęczaki (Arachnida)',
    ),
    const Species(
      id: 'acanthoscurria_geniculata',
      latinName: 'Acanthoscurria geniculata',
      commonName: 'Ptasznik białokolankowy',
      family: 'Ptaszniki (Theraphosidae)',
      phylum: 'Pajęczaki (Arachnida)',
    ),
  ];

  group('searchSpecies', () {
    test('returns sorted catalog when query is empty', () {
      final results = searchSpecies(sampleCatalog, query: '');
      expect(results, hasLength(3));
      expect(results.first.latinName, 'Acanthoscurria geniculata');
    });

    test('matches latin name while typing', () {
      final results = searchSpecies(sampleCatalog, query: 'ham');
      expect(results.map((s) => s.latinName), ['Brachypelma hamorii']);
    });

    test('matches polish common name', () {
      final results = searchSpecies(sampleCatalog, query: 'białokol');
      expect(results.single.latinName, 'Acanthoscurria geniculata');
    });

    test('applies family filter', () {
      final results = searchSpecies(
        sampleCatalog,
        query: 'ptasznik',
        filters: const SpeciesFilters(onlyWithPolishName: true),
      );
      expect(results, hasLength(3));
    });
  });

  group('SpeciesCsvLoader', () {
    test('parses basic CSV row', () {
      const csv = '''
Nazwa naukowa;Nazwa polska;Gromada;Rząd;Podrząd;Rodzina
Brachypelma hamorii;Ptasznik meksykański;Pajęczaki;Pająki;Ptasznikowate;Theraphosidae
''';

      final species = SpeciesCsvLoader().parseCsv(csv);
      expect(species, hasLength(1));
      expect(species.first.latinName, 'Brachypelma hamorii');
      expect(species.first.commonName, 'Ptasznik meksykański');
      expect(species.first.id, 'brachypelma_hamorii');
    });
  });
}
