import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:spiderzone/features/species/data/species_csv_loader.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  test('loads expanded terrarium_species.csv from Flutter assets', () async {
    final raw = await rootBundle.loadString('assets/terrarium_species.csv');
    final species = SpeciesCsvLoader().parseCsv(raw);

    expect(species.length, greaterThanOrEqualTo(150));
    expect(
      species.any((s) => s.latinName == 'Acanthoscurria geniculata'),
      isTrue,
    );
    final sample = species.firstWhere(
      (s) => s.latinName == 'Acanthoscurria geniculata',
    );
    expect(sample.commonName, contains('białokol'));
    expect(sample.housing, isNotEmpty);
    expect(sample.description, isNotEmpty);
    expect(sample.kingdom, 'Animalia');
    expect(sample.family, 'Theraphosidae');
  });
}
