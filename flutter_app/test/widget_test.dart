import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:spiderzone/app.dart';
import 'package:spiderzone/features/species/application/species_providers.dart';
import 'package:spiderzone/features/species/domain/species.dart';

void main() {
  testWidgets('SpiderZone app renders home screen', (tester) async {
    await tester.pumpWidget(
      ProviderScope(
        overrides: [
          speciesCatalogProvider.overrideWith(
            (ref) async => const [
              Species(
                id: 'acanthoscurria_geniculata',
                latinName: 'Acanthoscurria geniculata',
                commonName: 'Ptasznik białokolankowy',
                kingdom: 'Animalia',
                phylum: 'Arthropoda',
                taxonomicClass: 'Arachnida',
                taxonomicOrder: 'Araneae',
                family: 'Theraphosidae',
              ),
            ],
          ),
        ],
        child: const SpiderZoneApp(),
      ),
    );

    await tester.pump();
    await tester.pump(const Duration(milliseconds: 100));

    expect(find.text('SpiderZone'), findsOneWidget);
    expect(find.textContaining('gatunków w bazie'), findsOneWidget);
  });
}
