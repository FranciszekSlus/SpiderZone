import 'package:flutter/material.dart';
import 'package:spiderzone/core/theme/app_colors.dart';
import 'package:spiderzone/core/widgets/sz_card.dart';
import 'package:spiderzone/features/species/domain/species.dart';
import 'package:spiderzone/features/species/domain/taxonomy_labels.dart';

/// Karta gatunku na listach wyników i w drzewie taksonomii.
class SpeciesCard extends StatelessWidget {
  const SpeciesCard({
    super.key,
    required this.latinName,
    this.commonName,
    this.meta,
    this.onTap,
  });

  factory SpeciesCard.fromSpecies(
    Species species, {
    Key? key,
    VoidCallback? onTap,
  }) {
    return SpeciesCard(
      key: key,
      latinName: species.latinName,
      commonName: species.commonName.trim().isEmpty ? null : species.commonName,
      meta: species.family.trim().isEmpty
          ? null
          : polishTaxonLabel(species.family),
      onTap: onTap,
    );
  }

  final String latinName;
  final String? commonName;
  final String? meta;
  final VoidCallback? onTap;

  @override
  Widget build(BuildContext context) {
    return SzCard(
      onTap: onTap,
      child: Row(
        children: [
          Container(
            width: 44,
            height: 44,
            decoration: BoxDecoration(
              color: AppColors.surfaceElevated,
              borderRadius: BorderRadius.circular(12),
            ),
            child: const Icon(
              Icons.bug_report_outlined,
              color: AppColors.primary,
            ),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  latinName,
                  style: const TextStyle(
                    fontWeight: FontWeight.w600,
                    color: AppColors.textPrimary,
                  ),
                ),
                if (commonName != null) ...[
                  const SizedBox(height: 2),
                  Text(
                    commonName!,
                    style: const TextStyle(color: AppColors.textSecondary),
                  ),
                ],
                if (meta != null) ...[
                  const SizedBox(height: 2),
                  Text(
                    meta!,
                    style: const TextStyle(
                      fontSize: 12,
                      color: AppColors.textSecondary,
                    ),
                  ),
                ],
              ],
            ),
          ),
          const Icon(Icons.chevron_right, color: AppColors.textSecondary),
        ],
      ),
    );
  }
}
