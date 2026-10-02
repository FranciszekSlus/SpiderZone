import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:spiderzone/core/routing/app_routes.dart';
import 'package:spiderzone/core/theme/app_colors.dart';
import 'package:spiderzone/core/theme/app_tokens.dart';
import 'package:spiderzone/core/widgets/sz_card.dart';
import 'package:spiderzone/features/species/domain/taxonomy_node.dart';
import 'package:spiderzone/features/species/presentation/widgets/species_card.dart';

/// Przeglądarka drzewa taksonomii: drill-down z okruszkami.
class TaxonomyBrowser extends StatefulWidget {
  const TaxonomyBrowser({super.key, required this.root});

  final TaxonomyNode root;

  @override
  State<TaxonomyBrowser> createState() => _TaxonomyBrowserState();
}

class _TaxonomyBrowserState extends State<TaxonomyBrowser> {
  late List<TaxonomyNode> _path;

  @override
  void initState() {
    super.initState();
    _path = [widget.root];
  }

  @override
  void didUpdateWidget(covariant TaxonomyBrowser oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.root != widget.root) {
      _path = [widget.root];
    }
  }

  TaxonomyNode get _current => _path.last;

  void _open(TaxonomyNode node) {
    if (node.isSpecies && node.speciesId != null) {
      context.push(AppRoutes.speciesDetail(node.speciesId!));
      return;
    }
    if (node.children.isEmpty) return;
    setState(() => _path = [..._path, node]);
  }

  void _goTo(int index) {
    setState(() => _path = _path.sublist(0, index + 1));
  }

  @override
  Widget build(BuildContext context) {
    final current = _current;
    final children = current.children;

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          'Przeglądaj bazę',
          style: Theme.of(context).textTheme.titleMedium?.copyWith(
                fontWeight: FontWeight.w600,
                color: AppColors.textPrimary,
              ),
        ),
        const SizedBox(height: 4),
        Text(
          current.subtitle ?? 'Wybierz kategorię, aby zejść głębiej.',
          style: const TextStyle(color: AppColors.textSecondary),
        ),
        const SizedBox(height: 10),
        SingleChildScrollView(
          scrollDirection: Axis.horizontal,
          child: Row(
            children: [
              for (var i = 0; i < _path.length; i++) ...[
                if (i > 0)
                  const Padding(
                    padding: EdgeInsets.symmetric(horizontal: 4),
                    child: Icon(
                      Icons.chevron_right,
                      size: 16,
                      color: AppColors.textSecondary,
                    ),
                  ),
                ActionChip(
                  label: Text(_path[i].title),
                  onPressed: i == _path.length - 1 ? null : () => _goTo(i),
                  backgroundColor: i == _path.length - 1
                      ? AppColors.primary.withValues(alpha: 0.2)
                      : AppColors.surface,
                ),
              ],
            ],
          ),
        ),
        const SizedBox(height: 12),
        if (children.isEmpty)
          const SzCard(
            child: Text(
              'Brak pozycji na tym poziomie.',
              style: TextStyle(color: AppColors.textSecondary),
            ),
          )
        else
          ListView.separated(
            shrinkWrap: true,
            physics: const NeverScrollableScrollPhysics(),
            itemCount: children.length,
            separatorBuilder: (_, __) => const SizedBox(height: 8),
            itemBuilder: (context, index) {
              final node = children[index];
              if (node.isSpecies) {
                return SpeciesCard(
                  latinName: node.title,
                  commonName: node.subtitle,
                  onTap: () => _open(node),
                );
              }
              return SzCard(
                onTap: () => _open(node),
                child: Row(
                  children: [
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            node.title,
                            style: const TextStyle(
                              fontWeight: FontWeight.w600,
                              color: AppColors.textPrimary,
                            ),
                          ),
                          if (node.subtitle != null) ...[
                            const SizedBox(height: 4),
                            Text(
                              node.subtitle!,
                              style: const TextStyle(
                                color: AppColors.textSecondary,
                                fontSize: 13,
                              ),
                            ),
                          ],
                        ],
                      ),
                    ),
                    const Icon(
                      Icons.chevron_right,
                      color: AppColors.textSecondary,
                    ),
                  ],
                ),
              );
            },
          ),
        const SizedBox(height: AppTokens.paddingScreen),
      ],
    );
  }
}
