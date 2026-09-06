class Species {
  const Species({
    required this.id,
    required this.latinName,
    this.commonName = '',
    this.category = 'other',
    this.phylum = '',
    this.taxonomicClass = '',
    this.suborder = '',
    this.family = '',
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
    this.careNotes = '',
    this.funFact = '',
    this.imageUrl = '',
  });

  final String id;
  final String latinName;
  final String commonName;
  final String category;
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
  final String careNotes;
  final String funFact;
  final String imageUrl;

  String get genus {
    final parts = latinName.trim().split(RegExp(r'\s+'));
    return parts.isNotEmpty ? parts.first : '';
  }

  bool get hasPolishName => commonName.trim().isNotEmpty;

  bool get hasCareData =>
      occurrence.isNotEmpty ||
      lifeMode.isNotEmpty ||
      temperatureDay.isNotEmpty ||
      humidity.isNotEmpty ||
      difficulty.isNotEmpty;

  String displayField(String value) =>
      value.trim().isEmpty ? '—' : value.trim();
}
