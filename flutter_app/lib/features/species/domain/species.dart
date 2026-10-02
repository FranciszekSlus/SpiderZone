class Species {
  const Species({
    required this.id,
    required this.latinName,
    this.catalogId = '',
    this.commonName = '',
    this.category = 'other',
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
    this.imageUrl = '',
  });

  final String id;
  /// Opcjonalne ID numeryczne z CSV (np. "1").
  final String catalogId;
  final String latinName;
  final String commonName;
  final String category;
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
  final String imageUrl;

  String get displayGenus {
    if (genus.trim().isNotEmpty) return genus.trim();
    final parts = latinName.trim().split(RegExp(r'\s+'));
    return parts.isNotEmpty ? parts.first : '';
  }

  bool get hasPolishName => commonName.trim().isNotEmpty;

  bool get hasCareData =>
      occurrence.isNotEmpty ||
      lifeMode.isNotEmpty ||
      temperatureDay.isNotEmpty ||
      humidity.isNotEmpty ||
      difficulty.isNotEmpty ||
      housing.isNotEmpty;

  String get citesLabel {
    final v = cites.trim().toLowerCase();
    if (v == 'true' || v == 'tak' || v == '1' || v == 'yes') return 'Tak';
    if (v == 'false' || v == 'nie' || v == '0' || v == 'no') return 'Nie';
    return cites.trim().isEmpty ? '—' : cites.trim();
  }

  String displayField(String value) =>
      value.trim().isEmpty ? '—' : value.trim();
}
