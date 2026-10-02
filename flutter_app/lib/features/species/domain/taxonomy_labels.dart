/// Polskie etykiety taksonomii (do drzewa i karty gatunku).
///
/// Klucze = nazwy naukowe z CSV (bez względu na wielkość liter).
String polishTaxonLabel(String scientific) {
  final key = scientific.trim();
  if (key.isEmpty || key == '—') return key;
  final pl = _labels[key.toLowerCase()];
  if (pl == null) return key;
  if (pl.toLowerCase() == key.toLowerCase()) return pl;
  return '$pl ($key)';
}

/// Same bez powtórzenia łaciny w nawiasie — np. chipy.
String polishTaxonShort(String scientific) {
  final key = scientific.trim();
  if (key.isEmpty || key == '—') return key;
  return _labels[key.toLowerCase()] ?? key;
}

const _labels = <String, String>{
  // Królestwo
  'animalia': 'Zwierzęta',

  // Typ (phylum)
  'arthropoda': 'Stawonogi',
  'chordata': 'Strunowce',

  // Gromada (class)
  'arachnida': 'Pajęczaki',
  'amphibia': 'Płazy',
  'insecta': 'Owady',
  'reptilia': 'Gady',

  // Rząd (order)
  'araneae': 'Pająki',
  'mantodea': 'Modliszki',
  'anura': 'Płazy bezogonowe',
  'squamata': 'Łuskonośne',
  'caudata': 'Płazy ogoniaste',
  'testudines': 'Żółwie',

  // Rodzina (family) — pajęczaki
  'theraphosidae': 'Ptasznikowate',
  'salticidae': 'Skakunowate',
  'araneidae': 'Krzyżakowate',
  'lycosidae': 'Pogońcowate',
  'ctenidae': 'Wędrowcowate',
  'pholcidae': 'Tkańcowate',
  'sparassidae': 'Łowcowate',
  'eresidae': 'Poskoczowate',
  'pisauridae': 'Pisaury',
  'theridiidae': 'Omatnikowate',
  'nephilidae': 'Nephile',

  // Rodzina — modliszki
  'mantidae': 'Modliszkowate',
  'hymenopodidae': 'Modliszki orchideowe',
  'empusidae': 'Empuzowate',
  'deroplatyidae': 'Modliszki liściowe',
  'acanthopidae': 'Acantopidy',
  'choeradodidae': 'Choeradodidy',

  // Rodzina — płazy
  'dendrobatidae': 'Drzewołazowate',
  'salamandridae': 'Salamandrowate',
  'ceratophryidae': 'Rogatkowate',
  'pelodryadidae': 'Rzekotki australijskie',
  'phyllomedusidae': 'Liściożaby',
  'ambystomatidae': 'Ambystomowate',
  'pipidae': 'Grzbietorodne',
  'bombinatoridae': 'Kumakowate',
  'pyxicephalidae': 'Afrykańskie żaby bydlęce',
  'hylidae': 'Rzekotkowate',
  'mantellidae': 'Mantellowate',
  'megophryidae': 'Ropuszki azjatyckie',
  'microhylidae': 'Wąskoustowate',

  // Rodzina — gady
  'agamidae': 'Agamowate',
  'chamaeleonidae': 'Kameleonowate',
  'colubridae': 'Połozowate',
  'eublepharidae': 'Eublefarowate',
  'pythonidae': 'Pytonowate',
  'boidae': 'Boowate',
  'diplodactylidae': 'Diplodaktyle',
  'gekkonidae': 'Gekonowate',
  'scincidae': 'Scynkowate',
  'varanidae': 'Waranowate',
  'testudinidae': 'Żółwie lądowe',
};
