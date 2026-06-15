// Plik tymczasowy — zastąp komendą: flutterfire configure
// Projekt Firebase: spiderzone-d112d (z .firebaserc w repo)

import 'package:firebase_core/firebase_core.dart' show FirebaseOptions;
import 'package:flutter/foundation.dart'
    show defaultTargetPlatform, kIsWeb, TargetPlatform;

class DefaultFirebaseOptions {
  static FirebaseOptions get currentPlatform {
    if (kIsWeb) {
      throw UnsupportedError('Web nie jest jeszcze skonfigurowany.');
    }
    switch (defaultTargetPlatform) {
      case TargetPlatform.android:
        return android;
      case TargetPlatform.iOS:
        return ios;
      default:
        throw UnsupportedError(
          'Domyslne opcje Firebase nie sa ustawione dla tej platformy.',
        );
    }
  }

  // UZUPELNIJ po: dart pub global activate flutterfire_cli && flutterfire configure
  static const FirebaseOptions android = FirebaseOptions(
    apiKey: 'REPLACE_ME',
    appId: 'REPLACE_ME',
    messagingSenderId: 'REPLACE_ME',
    projectId: 'spiderzone-d112d',
    storageBucket: 'spiderzone-d112d.firebasestorage.app',
  );

  static const FirebaseOptions ios = FirebaseOptions(
    apiKey: 'REPLACE_ME',
    appId: 'REPLACE_ME',
    messagingSenderId: 'REPLACE_ME',
    projectId: 'spiderzone-d112d',
    storageBucket: 'spiderzone-d112d.firebasestorage.app',
    iosBundleId: 'com.example.spiderzone',
  );
}
