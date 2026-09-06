import 'package:flutter/services.dart';
import 'package:spiderzone/features/species/domain/species.dart';

const _assetFile = 'assets/terrarium_species.csv';

class SpeciesCsvLoader {
  Future<List<Species>> loadFromAssets() async {
    final raw = await rootBundle.loadString(_assetFile);
    return parseCsv(raw);
  }

  List<Species> parseCsv(String raw) {
    final lines = raw.split('\n').map((l) => l.trim()).where((l) => l.isNotEmpty);
    final iterator = lines.iterator;
    if (!iterator.moveNext()) return [];

    final header = iterator.current.split(';').map((h) => h.trim()).toList();
    final hasCareColumns = header.any((h) => h.toLowerCase() == 'występowanie');

    final seenLatin = <String>{};
    final result = <Species>[];

    while (iterator.moveNext()) {
      final row = hasCareColumns
          ? _parseWithHeader(iterator.current, header)
          : _parseBasicRow(iterator.current);
      if (row == null) continue;

      final latin = row.latinName.trim();
      if (latin.isEmpty) continue;

      final key = latin.toLowerCase();
      if (!seenLatin.add(key)) continue;

      result.add(
        Species(
          id: latinToStableId(latin),
          latinName: latin,
          commonName: row.polishName.trim(),
          category: inferCategory(row.phylum, row.family),
          phylum: row.phylum.trim(),
          taxonomicClass: row.taxonomicClass.trim(),
          suborder: row.suborder.trim(),
          family: row.family.trim(),
          occurrence: row.occurrence,
          lifeMode: row.lifeMode,
          temperatureDay: row.temperatureDay,
          temperatureNight: row.temperatureNight,
          humidity: row.humidity,
          sizeMale: row.sizeMale,
          sizeFemale: row.sizeFemale,
          temperament: row.temperament,
          venom: row.venom,
          difficulty: row.difficulty,
          cites: row.cites,
          diet: row.diet,
          documentedBreeding: row.documentedBreeding,
        ),
      );
    }

    result.sort((a, b) => a.latinName.toLowerCase().compareTo(b.latinName.toLowerCase()));
    return result;
  }
}

class _CsvSpeciesRow {
  const _CsvSpeciesRow({
    required this.latinName,
    required this.polishName,
    required this.phylum,
    required this.taxonomicClass,
    required this.suborder,
    required this.family,
    this.occurrence = '',
    this.lifeMode = '',
    this.temperatureDay = '',
    this.temperatureNight = '',
    this.humidity = '',
    this.sizeMale = '',
    this.sizeFemale = '',
    this.temperament = '',
    this.venom = '',
    this.difficulty = '',
    this.cites = '',
    this.diet = '',
    this.documentedBreeding = '',
  });

  final String latinName;
  final String polishName;
  final String phylum;
  final String taxonomicClass;
  final String suborder;
  final String family;
  final String occurrence;
  final String lifeMode;
  final String temperatureDay;
  final String temperatureNight;
  final String humidity;
  final String sizeMale;
  final String sizeFemale;
  final String temperament;
  final String venom;
  final String difficulty;
  final String cites;
  final String diet;
  final String documentedBreeding;
}

_CsvSpeciesRow? _parseBasicRow(String line) {
  final parts = line.split(';');
  if (parts.length < 6) return null;
  return _CsvSpeciesRow(
    latinName: parts[0],
    polishName: parts[1],
    phylum: parts[2],
    taxonomicClass: parts[3],
    suborder: parts[4],
    family: parts[5],
  );
}

_CsvSpeciesRow? _parseWithHeader(String line, List<String> header) {
  final parts = line.split(';');
  if (parts.length < 6) return null;

  String col(List<String> names) {
    final index = header.indexWhere(
      (h) => names.any((n) => h.toLowerCase() == n.toLowerCase()),
    );
    if (index < 0 || index >= parts.length) return '';
    return parts[index].trim();
  }

  return _CsvSpeciesRow(
    latinName: col(['Nazwa naukowa']),
    polishName: col(['Nazwa polska']),
    phylum: col(['Gromada']),
    taxonomicClass: col(['Rząd', 'Rzad']),
    suborder: col(['Podrząd', 'Podrzad']),
    family: col(['Rodzina']),
    occurrence: col(['Występowanie']),
    lifeMode: col(['Tryb życia', 'Tryb zycia']),
    temperatureDay: col(['Temperatura dzień', 'Temperatura dzien']),
    temperatureNight: col(['Temperatura noc']),
    humidity: col(['Wilgotność', 'Wilgotnosc']),
    sizeMale: col(['Wielkość samiec', 'Wielkosc samiec']),
    sizeFemale: col(['Wielkość samica', 'Wielkosc samica']),
    temperament: col(['Temperament']),
    venom: col(['Jad']),
    difficulty: col(['Zaawansowanie']),
    cites: col(['CITES']),
    diet: col(['Dieta']),
    documentedBreeding: col(['Udokumentowane rozmnażenia', 'Udokumentowane rozmnozenia']),
  );
}

String latinToStableId(String latin) =>
    latin.toLowerCase().replaceAll(RegExp(r'[^a-z0-9]+'), '_').replaceAll(RegExp(r'^_|_$'), '');

String inferCategory(String phylum, String family) {
  final p = phylum.toLowerCase();
  final f = family.toLowerCase();
  if (p.contains('pajęczak') || f.contains('theraphosidae')) return 'tarantula';
  if (p.contains('gady') || p.contains('reptil')) return 'reptile';
  if (p.contains('płaz') || p.contains('amphib')) return 'amphibian';
  if (p.contains('owad') || p.contains('insect')) return 'insect';
  if (p.contains('równonog') || p.contains('isopod')) return 'isopod';
  return 'other';
}
