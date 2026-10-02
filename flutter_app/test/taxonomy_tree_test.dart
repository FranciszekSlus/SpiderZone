import 'package:flutter_test/flutter_test.dart';
import 'package:spiderzone/features/species/domain/species.dart';
import 'package:spiderzone/features/species/domain/taxonomy_node.dart';

void main() {
  test('legacy tree: phylum → order → suborder → family → species', () {
    const catalog = [
      Species(
        id: 'brachypelma_hamorii',
        latinName: 'Brachypelma hamorii',
        commonName: 'Ptasznik meksykański',
        phylum: 'Pajęczaki',
        taxonomicClass: 'Pająki',
        suborder: 'Ptasznikowate',
        family: 'Theraphosidae',
      ),
      Species(
        id: 'acanthoscurria_geniculata',
        latinName: 'Acanthoscurria geniculata',
        commonName: 'Ptasznik białokolankowy',
        phylum: 'Pajęczaki',
        taxonomicClass: 'Pająki',
        suborder: 'Ptasznikowate',
        family: 'Theraphosidae',
      ),
    ];

    final root = buildTaxonomyTree(catalog);
    expect(root.subtitle, contains('2'));
    expect(root.children, hasLength(1));
    expect(root.children.first.title, 'Pajęczaki');

    final family =
        root.children.first.children.first.children.first.children.first;
    expect(family.title, 'Ptasznikowate');
    expect(family.subtitle, contains('Theraphosidae'));
    expect(family.children, hasLength(2));
    expect(family.children.every((n) => n.isSpecies), isTrue);
  });

  test('new tree: kingdom → phylum → class → order → family → species', () {
    const catalog = [
      Species(
        id: 'acanthoscurria_geniculata',
        latinName: 'Acanthoscurria geniculata',
        kingdom: 'Animalia',
        phylum: 'Arthropoda',
        taxonomicClass: 'Arachnida',
        taxonomicOrder: 'Araneae',
        family: 'Theraphosidae',
      ),
      Species(
        id: 'ceratophrys_ornata',
        latinName: 'Ceratophrys ornata',
        kingdom: 'Animalia',
        phylum: 'Chordata',
        taxonomicClass: 'Amphibia',
        taxonomicOrder: 'Anura',
        family: 'Ceratophryidae',
      ),
    ];

    final root = buildTaxonomyTree(catalog);
    expect(root.children.single.title, 'Zwierzęta');
    final phyla = root.children.single.children.map((n) => n.title).toSet();
    expect(phyla, containsAll(['Stawonogi', 'Strunowce']));
  });
}
