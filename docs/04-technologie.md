# Technologie

## Używane teraz

| Obszar | Technologia |
|--------|-------------|
| Aplikacja mobilna (referencyjna) | Android — Kotlin + Jetpack Compose |
| Aplikacja mobilna (docelowa) | Flutter w `flutter_app/` (Android + iOS) |
| Backend | Firebase Spark: Auth, Firestore |
| IDE | Android Studio |
| AI | Cursor |
| Git | GitHub, branch `main` |
| Reguły | `firestore.rules`, `storage.rules`, `firebase.json` |

## Rekomendacje docelowe

| Obszar | Propozycja |
|--------|------------|
| Mobile cross-platform | Flutter — analiza: [09-flutter-vs-android.md](09-flutter-vs-android.md) |
| Stan UI (Flutter) | Riverpod lub Bloc |
| Nawigacja (Flutter) | go_router (już w szkielecie) |
| Obrazy | **Cloudinary Free + kompresja przed uploadem**; wyświetlanie przez Coil (Android) / cached_network_image (Flutter) |
| Push | FCM + lokalne notyfikacje |
| CI | GitHub Actions (build APK/AAB) |
| Crashy | Firebase Crashlytics |
| Specyfikacje feature | SDD w `docs/sdd/` (do utworzenia przy większych feature) |

## Wybór Android vs Flutter

**Rozstrzygnięty:** wybór padł na **Flutter** jako klient docelowy (Android + iOS).

Kotlin Compose zostaje jako działający prototyp/referencja funkcji, ale nowe większe moduły planujemy już pod Flutter.

## Task tracking

**Źródło prawdy:** **Notion (SpiderZone Tasks)**.
**Kopia techniczna:** [tasks/TASKS.md](tasks/TASKS.md) dla pracy z Cursor.
**Workflow:** [13-trackowanie-zadan.md](13-trackowanie-zadan.md).
**Reguła Cursor:** `.cursor/rules/spiderzone-tasks.mdc`
