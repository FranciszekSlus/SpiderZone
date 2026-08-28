import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:spiderzone/core/firebase/firebase_bootstrap.dart';
import 'package:spiderzone/core/theme/app_colors.dart';
import 'package:spiderzone/core/theme/app_tokens.dart';
import 'package:spiderzone/core/widgets/sz_card.dart';
import 'package:spiderzone/features/species/application/species_providers.dart';
import 'package:spiderzone/features/species/presentation/widgets/species_search_section.dart';

class HomeScreen extends ConsumerWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final catalogCount = ref.watch(speciesCatalogProvider).valueOrNull?.length;

    return SafeArea(
      child: ListView(
        padding: const EdgeInsets.all(AppTokens.paddingScreen),
        children: [
          Text(
            'SpiderZone',
            style: Theme.of(context).textTheme.headlineMedium?.copyWith(
                  fontWeight: FontWeight.bold,
                  color: AppColors.textPrimary,
                ),
          ),
          const SizedBox(height: 8),
          const Text(
            'Platforma hodowli egzotycznych zwierząt.',
            style: TextStyle(color: AppColors.textSecondary),
          ),
          const SizedBox(height: 20),
          catalogCount == null
              ? const SzCard(
                  child: Row(
                    children: [
                      SizedBox(
                        width: 18,
                        height: 18,
                        child: CircularProgressIndicator(
                          strokeWidth: 2,
                          color: AppColors.primary,
                        ),
                      ),
                      SizedBox(width: 12),
                      Text('Ładowanie bazy gatunków z CSV…'),
                    ],
                  ),
                )
              : SzCard(
                  child: Row(
                    children: [
                      const Icon(Icons.storage_outlined, color: AppColors.primary),
                      const SizedBox(width: 12),
                      Expanded(
                        child: Text(
                          'Baza lokalna: $catalogCount gatunków (assets/terrarium_species.csv)',
                          style: const TextStyle(color: AppColors.textSecondary),
                        ),
                      ),
                    ],
                  ),
                ),
          const SizedBox(height: 16),
          const SpeciesSearchSection(),
          const SizedBox(height: 12),
          SzCard(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const Text(
                  'Status Firebase',
                  style: TextStyle(fontWeight: FontWeight.w600),
                ),
                const SizedBox(height: 6),
                Text(
                  FirebaseBootstrap.isReady
                      ? 'Połączono z Firebase'
                      : 'Skonfiguruj firebase_options.dart (flutterfire configure)',
                  style: const TextStyle(color: AppColors.textSecondary),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
