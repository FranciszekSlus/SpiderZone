import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/foundation.dart';
import 'package:spiderzone/core/firebase/firebase_options.dart';

class FirebaseBootstrap {
  static bool _initialized = false;

  static bool get isReady => _initialized;

  static Future<void> initialize() async {
    if (_initialized) return;

    final options = DefaultFirebaseOptions.currentPlatform;
    if (options.apiKey == 'REPLACE_ME') {
      debugPrint(
        '[SpiderZone] Firebase: uruchom flutterfire configure i podmien firebase_options.dart',
      );
      return;
    }

    await Firebase.initializeApp(options: options);
    _initialized = true;
  }
}
