import 'package:flutter_test/flutter_test.dart';
import 'package:spiderzone/features/species/domain/taxonomy_labels.dart';

void main() {
  test('maps common taxa to Polish with Latin in parentheses', () {
    expect(polishTaxonLabel('Animalia'), 'Zwierzęta (Animalia)');
    expect(polishTaxonLabel('Arachnida'), 'Pajęczaki (Arachnida)');
    expect(polishTaxonLabel('Theraphosidae'), 'Ptasznikowate (Theraphosidae)');
    expect(polishTaxonLabel('Squamata'), 'Łuskonośne (Squamata)');
  });

  test('unknown taxon stays unchanged', () {
    expect(polishTaxonLabel('Unknownidae'), 'Unknownidae');
  });
}
