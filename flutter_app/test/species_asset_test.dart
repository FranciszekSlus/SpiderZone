import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:spiderzone/features/species/data/species_csv_loader.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  test('loads terrarium_species.csv from Flutter assets', () async {
    final raw = await rootBundle.loadString('assets/terrarium_species.csv');
    final species = SpeciesCsvLoader().parseCsv(raw);

    expect(species.length, greaterThan(200));
    expect(
      species.any((s) => s.latinName.contains('Brachypelma')),
      isTrue,
    );
  });
}
