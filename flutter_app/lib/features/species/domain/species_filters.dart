class SpeciesFilters {
  const SpeciesFilters({
    this.family,
    this.onlyTheraphosidae = false,
    this.onlyWithPolishName = false,
    this.difficulty,
  });

  final String? family;
  final bool onlyTheraphosidae;
  final bool onlyWithPolishName;
  final String? difficulty;

  static const empty = SpeciesFilters();

  SpeciesFilters copyWith({
    String? family,
    bool clearFamily = false,
    bool? onlyTheraphosidae,
    bool? onlyWithPolishName,
    String? difficulty,
    bool clearDifficulty = false,
  }) {
    return SpeciesFilters(
      family: clearFamily ? null : (family ?? this.family),
      onlyTheraphosidae: onlyTheraphosidae ?? this.onlyTheraphosidae,
      onlyWithPolishName: onlyWithPolishName ?? this.onlyWithPolishName,
      difficulty: clearDifficulty ? null : (difficulty ?? this.difficulty),
    );
  }

  bool get hasActiveFilters =>
      family != null ||
      onlyTheraphosidae ||
      onlyWithPolishName ||
      difficulty != null;
}
