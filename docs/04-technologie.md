# Technologie

## Używane teraz

| Obszar | Technologia |
|--------|-------------|
| Aplikacja mobilna (aktywna) | Android — Kotlin + Jetpack Compose |
| Aplikacja mobilna (szkielet) | Flutter w `flutter_app/` |
| Backend | Firebase Spark: Auth, Firestore, Storage |
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
| Obrazy | Kompresja przed uploadem + Coil (Android) / cached_network_image (Flutter) |
| Push | FCM + lokalne notyfikacje |
| CI | GitHub Actions (build APK/AAB) |
| Crashy | Firebase Crashlytics |
| Specyfikacje feature | SDD w `docs/sdd/` (do utworzenia przy większych feature) |

## Wybór Android vs Flutter

**Nie rozstrzygnięty** — patrz [09-flutter-vs-android.md](09-flutter-vs-android.md). Decyzja po przeczytaniu za/przeciw.

## Task tracking

**Źródło prawdy:** [tasks/TASKS.md](tasks/TASKS.md) w repo (Cursor ma do niego dostęp).  
**Workflow:** [13-trackowanie-zadan.md](13-trackowanie-zadan.md) — GitHub Issues nieużywane; opcjonalnie Notion/ClickUp jako lustro.  
**Reguła Cursor:** `.cursor/rules/spiderzone-tasks.mdc`
