import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:spiderzone/core/theme/app_colors.dart';
import 'package:spiderzone/core/theme/app_tokens.dart';
import 'package:spiderzone/core/widgets/sz_card.dart';
import 'package:spiderzone/features/species/application/species_providers.dart';
import 'package:spiderzone/features/species/domain/species.dart';

class SpeciesDetailScreen extends ConsumerWidget {
  const SpeciesDetailScreen({super.key, required this.speciesId});

  final String speciesId;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final speciesAsync = ref.watch(speciesByIdProvider(speciesId));

    return Scaffold(
      backgroundColor: AppColors.background,
      appBar: AppBar(
        backgroundColor: AppColors.background,
        foregroundColor: AppColors.textPrimary,
        title: const Text('Opis gatunku'),
      ),
      body: speciesAsync.when(
        loading: () => const Center(
          child: CircularProgressIndicator(color: AppColors.primary),
        ),
        error: (e, _) => Center(child: Text('Błąd: $e')),
        data: (species) {
          if (species == null) {
            return Center(
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  const Text('Nie znaleziono gatunku.'),
                  const SizedBox(height: 12),
                  TextButton(
                    onPressed: () => context.pop(),
                    child: const Text('Wróć'),
                  ),
                ],
              ),
            );
          }
          return _SpeciesDetailBody(species: species);
        },
      ),
    );
  }
}

class _SpeciesDetailBody extends StatelessWidget {
  const _SpeciesDetailBody({required this.species});

  final Species species;

  @override
  Widget build(BuildContext context) {
    return ListView(
      padding: const EdgeInsets.all(AppTokens.paddingScreen),
      children: [
        SzCard(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                species.latinName,
                style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                      fontWeight: FontWeight.bold,
                      color: AppColors.textPrimary,
                    ),
              ),
              if (species.commonName.isNotEmpty) ...[
                const SizedBox(height: 6),
                Text(
                  species.commonName,
                  style: const TextStyle(
                    fontSize: 16,
                    color: AppColors.textSecondary,
                  ),
                ),
              ],
            ],
          ),
        ),
        const SizedBox(height: 12),
        SzCard(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const _SectionTitle('Taksonomia'),
              _FieldRow(label: 'Gromada', value: species.phylum),
              _FieldRow(label: 'Rząd', value: species.taxonomicClass),
              _FieldRow(label: 'Podrząd', value: species.suborder),
              _FieldRow(label: 'Rodzina', value: species.family),
            ],
          ),
        ),
        const SizedBox(height: 12),
        SzCard(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const _SectionTitle('Parametry hodowlane'),
              const Text(
                'Puste pola (—) będą uzupełniane etapami. Większość danych docelowo generowana z pomocą AI i weryfikowana ręcznie.',
                style: TextStyle(
                  fontSize: 12,
                  color: AppColors.textSecondary,
                ),
              ),
              const SizedBox(height: 8),
              _FieldRow(label: 'Występowanie', value: species.occurrence),
              _FieldRow(label: 'Tryb życia', value: species.lifeMode),
              _FieldRow(label: 'Temperatura (dzień)', value: species.temperatureDay),
              _FieldRow(label: 'Temperatura (noc)', value: species.temperatureNight),
              _FieldRow(label: 'Wilgotność', value: species.humidity),
              _FieldRow(label: 'Wielkość (samiec)', value: species.sizeMale),
              _FieldRow(label: 'Wielkość (samica)', value: species.sizeFemale),
              _FieldRow(label: 'Temperament', value: species.temperament),
              _FieldRow(label: 'Jad', value: species.venom),
              _FieldRow(label: 'Zaawansowanie', value: species.difficulty),
              _FieldRow(label: 'CITES', value: species.cites),
              _FieldRow(label: 'Dieta', value: species.diet),
              _FieldRow(
                label: 'Udokumentowane rozmnażania',
                value: species.documentedBreeding,
              ),
            ],
          ),
        ),
        if (species.funFact.isNotEmpty) ...[
          const SizedBox(height: 12),
          SzCard(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const _SectionTitle('Ciekawostka'),
                Text(species.funFact),
              ],
            ),
          ),
        ],
      ],
    );
  }
}

class _SectionTitle extends StatelessWidget {
  const _SectionTitle(this.text);

  final String text;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 8),
      child: Text(
        text,
        style: const TextStyle(
          fontWeight: FontWeight.bold,
          color: AppColors.textPrimary,
        ),
      ),
    );
  }
}

class _FieldRow extends StatelessWidget {
  const _FieldRow({required this.label, required this.value});

  final String label;
  final String value;

  @override
  Widget build(BuildContext context) {
    final display = value.trim().isEmpty ? '—' : value.trim();
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 3),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(
            width: 150,
            child: Text(
              label,
              style: const TextStyle(color: AppColors.textSecondary),
            ),
          ),
          Expanded(
            child: Text(
              display,
              style: const TextStyle(color: AppColors.textPrimary),
            ),
          ),
        ],
      ),
    );
  }
}
