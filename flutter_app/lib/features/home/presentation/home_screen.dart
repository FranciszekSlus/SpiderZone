import 'package:flutter/material.dart';
import 'package:spiderzone/core/firebase/firebase_bootstrap.dart';
import 'package:spiderzone/core/theme/app_colors.dart';
import 'package:spiderzone/core/theme/app_tokens.dart';
import 'package:spiderzone/core/widgets/sz_card.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
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
          SzCard(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const Text('Status Firebase', fontWeight: FontWeight.w600),
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
          const SizedBox(height: 12),
          const SzCard(
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text('Gatunki w bazie'),
                Text('—', style: TextStyle(fontWeight: FontWeight.bold)),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
