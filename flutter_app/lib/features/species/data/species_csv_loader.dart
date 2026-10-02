import 'package:flutter/services.dart';
import 'package:spiderzone/features/species/domain/species.dart';

const _assetFile = 'assets/terrarium_species.csv';

class SpeciesCsvLoader {
  Future<List<Species>> loadFromAssets() async {
    final raw = await rootBundle.loadString(_assetFile);
    return parseCsv(raw);
  }

  List<Species> parseCsv(String raw) {
    final delimiter = _detectDelimiter(raw);
    final rows = parseCsvRows(raw, delimiter: delimiter);
    if (rows.isEmpty) return [];

    final header = rows.first.map((h) => h.trim()).toList();
    final seenLatin = <String>{};
    final result = <Species>[];

    for (final parts in rows.skip(1)) {
      final row = _parseRow(header, parts);
      if (row == null) continue;

      final latin = row.latinName.trim();
      if (latin.isEmpty) continue;
      final key = latin.toLowerCase();
      if (!seenLatin.add(key)) continue;

      result.add(
        Species(
          id: latinToStableId(latin),
          catalogId: row.catalogId,
          latinName: latin,
          commonName: row.commonName.trim(),
          category: inferCategory(
            phylum: row.phylum,
            taxonomicClass: row.taxonomicClass,
            taxonomicOrder: row.taxonomicOrder,
            family: row.family,
          ),
          kingdom: row.kingdom.trim(),
          phylum: row.phylum.trim(),
          taxonomicClass: row.taxonomicClass.trim(),
          taxonomicOrder: row.taxonomicOrder.trim(),
          suborder: row.suborder.trim(),
          family: row.family.trim(),
          genus: row.genus.trim(),
          description: row.description,
          temperament: row.temperament,
          housing: row.housing,
          feeding: row.feeding,
          appearance: row.appearance,
          occurrence: row.occurrence,
          lifeMode: row.lifeMode,
          temperatureDay: row.temperatureDay,
          temperatureNight: row.temperatureNight,
          humidity: row.humidity,
          sizeMale: row.sizeMale,
          sizeFemale: row.sizeFemale,
          venom: row.venom,
          difficulty: row.difficulty,
          cites: row.cites,
          diet: row.diet,
          documentedBreeding: row.documentedBreeding,
          careNotes: row.careNotes,
          funFact: row.funFact,
        ),
      );
    }

    result.sort((a, b) => a.latinName.toLowerCase().compareTo(b.latinName.toLowerCase()));
    return result;
  }
}

class _ParsedRow {
  const _ParsedRow({
    required this.latinName,
    required this.commonName,
    this.catalogId = '',
    this.kingdom = '',
    this.phylum = '',
    this.taxonomicClass = '',
    this.taxonomicOrder = '',
    this.suborder = '',
    this.family = '',
    this.genus = '',
    this.description = '',
    this.temperament = '',
    this.housing = '',
    this.feeding = '',
    this.appearance = '',
    this.occurrence = '',
    this.lifeMode = '',
    this.temperatureDay = '',
    this.temperatureNight = '',
    this.humidity = '',
    this.sizeMale = '',
    this.sizeFemale = '',
    this.venom = '',
    this.difficulty = '',
    this.cites = '',
    this.diet = '',
    this.documentedBreeding = '',
    this.careNotes = '',
    this.funFact = '',
  });

  final String catalogId;
  final String latinName;
  final String commonName;
  final String kingdom;
  final String phylum;
  final String taxonomicClass;
  final String taxonomicOrder;
  final String suborder;
  final String family;
  final String genus;
  final String description;
  final String temperament;
  final String housing;
  final String feeding;
  final String appearance;
  final String occurrence;
  final String lifeMode;
  final String temperatureDay;
  final String temperatureNight;
  final String humidity;
  final String sizeMale;
  final String sizeFemale;
  final String venom;
  final String difficulty;
  final String cites;
  final String diet;
  final String documentedBreeding;
  final String careNotes;
  final String funFact;
}

_ParsedRow? _parseRow(List<String> header, List<String> parts) {
  String col(List<String> names) {
    final index = header.indexWhere(
      (h) => names.any((n) => h.toLowerCase() == n.toLowerCase()),
    );
    if (index < 0 || index >= parts.length) return '';
    return parts[index].trim();
  }

  final latin = col(['species', 'latinName', 'Nazwa naukowa']);
  if (latin.isEmpty) return null;

  return _ParsedRow(
    catalogId: col(['id']),
    latinName: latin,
    commonName: col(['commonName', 'Nazwa polska']),
    kingdom: col(['kingdom']),
    phylum: col(['phylum', 'Gromada']),
    taxonomicClass: col(['class', 'taxonomicClass']),
    taxonomicOrder: col(['order', 'Rząd', 'Rzad']),
    suborder: col(['suborder', 'Podrząd', 'Podrzad']),
    family: col(['family', 'Rodzina']),
    genus: col(['genus']),
    description: col(['description']),
    temperament: col(['temperament', 'Temperament']),
    housing: col(['housing']),
    feeding: col(['feeding']),
    appearance: col(['appearance']),
    occurrence: col(['occurrence', 'Występowanie']),
    lifeMode: col(['lifeMode', 'Tryb życia', 'Tryb zycia']),
    temperatureDay: col(['temperatureDay', 'Temperatura dzień', 'Temperatura dzien']),
    temperatureNight: col(['temperatureNight', 'Temperatura noc']),
    humidity: col(['humidity', 'Wilgotność', 'Wilgotnosc']),
    sizeMale: col(['sizeMale', 'Wielkość samiec', 'Wielkosc samiec']),
    sizeFemale: col(['sizeFemale', 'Wielkość samica', 'Wielkosc samica']),
    venom: col(['venom', 'Jad']),
    difficulty: col(['difficulty', 'Zaawansowanie']),
    cites: col(['cites', 'CITES']),
    diet: col(['diet', 'Dieta']),
    documentedBreeding: col([
      'documentedBreeding',
      'Udokumentowane rozmnażenia',
      'Udokumentowane rozmnozenia',
    ]),
    careNotes: col(['careNotes']),
    funFact: col(['funFact']),
  );
}

String _detectDelimiter(String raw) {
  final firstLine = raw.split(RegExp(r'\r?\n')).firstWhere(
        (l) => l.trim().isNotEmpty,
        orElse: () => '',
      );
  final commas = ','.allMatches(firstLine).length;
  final semis = ';'.allMatches(firstLine).length;
  return semis > commas ? ';' : ',';
}

/// Parser CSV z obsługą cudzysłowów.
List<List<String>> parseCsvRows(String raw, {required String delimiter}) {
  final normalized = raw.replaceAll('\r\n', '\n').replaceAll('\r', '\n');
  final rows = <List<String>>[];
  var row = <String>[];
  final field = StringBuffer();
  var inQuotes = false;
  var i = 0;

  void endField() {
    row.add(field.toString());
    field.clear();
  }

  void endRow() {
    endField();
    if (row.any((c) => c.trim().isNotEmpty)) {
      rows.add(row);
    }
    row = <String>[];
  }

  while (i < normalized.length) {
    final ch = normalized[i];
    if (inQuotes) {
      if (ch == '"') {
        if (i + 1 < normalized.length && normalized[i + 1] == '"') {
          field.write('"');
          i += 2;
          continue;
        }
        inQuotes = false;
        i++;
        continue;
      }
      field.write(ch);
      i++;
      continue;
    }

    if (ch == '"') {
      inQuotes = true;
      i++;
      continue;
    }
    if (ch == delimiter) {
      endField();
      i++;
      continue;
    }
    if (ch == '\n') {
      endRow();
      i++;
      continue;
    }
    field.write(ch);
    i++;
  }

  if (field.isNotEmpty || row.isNotEmpty) {
    endRow();
  }
  return rows;
}

String latinToStableId(String latin) =>
    latin.toLowerCase().replaceAll(RegExp(r'[^a-z0-9]+'), '_').replaceAll(RegExp(r'^_|_$'), '');

String inferCategory({
  required String phylum,
  required String taxonomicClass,
  required String taxonomicOrder,
  required String family,
}) {
  final p = phylum.toLowerCase();
  final c = taxonomicClass.toLowerCase();
  final o = taxonomicOrder.toLowerCase();
  final f = family.toLowerCase();

  if (f.contains('theraphosidae')) return 'tarantula';
  if (c.contains('amphibia') || o.contains('anura') || o.contains('caudata')) {
    return 'amphibian';
  }
  if (c.contains('reptilia') ||
      o.contains('squamata') ||
      o.contains('testudines')) {
    return 'reptile';
  }
  if (o.contains('mantodea') || c.contains('insecta')) return 'insect';
  if (p.contains('pajęczak') || c.contains('arachnida')) return 'arachnid';
  if (p.contains('arthropoda') && o.contains('araneae')) return 'arachnid';
  if (p.contains('gady') || p.contains('reptil')) return 'reptile';
  if (p.contains('płaz') || p.contains('amphib')) return 'amphibian';
  if (p.contains('owad') || p.contains('insect')) return 'insect';
  return 'other';
}
