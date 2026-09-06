# SpiderZone — Flutter

Fundament aplikacji: **Flutter + Firebase + dark design system**.

## Struktura

```text
lib/
  main.dart
  app.dart
  core/
    theme/          # kolory, tokeny, ThemeData
    firebase/       # init + firebase_options.dart
    routing/        # go_router, 4 zakładki
    widgets/        # SzCard, SzPrimaryButton
  features/
    shell/          # bottom navigation
    home|species|collection|breeding|profile/
```

## Wymagania

- Flutter SDK 3.24+ (`flutter doctor`)
- JDK 17 (Android)
- Konto Firebase (projekt: `spiderzone-d112d`)

## Pierwsze uruchomienie (krok po kroku)

### 1. Wygeneruj foldery platform (tylko raz)

W katalogu `flutter_app`:

```bash
flutter create . --org com.example.spiderzone --project-name spiderzone
flutter pub get
```

### 2. Podłącz Firebase (FlutterFire)

```bash
dart pub global activate flutterfire_cli
flutterfire configure --project=spiderzone-d112d
```

To nadpisze `lib/core/firebase/firebase_options.dart` prawdziwymi kluczami.

### 3. Android — pliki z Firebase Console

- Pobierz `google-services.json` → `android/app/google-services.json`
- W `android/build.gradle` i `android/app/build.gradle` dodaj plugin Google Services (FlutterFire zwykle robi to sam).

### 4. iOS (opcjonalnie)

- Pobierz `GoogleService-Info.plist` → `ios/Runner/`
- `cd ios && pod install`

### 5. Uruchom

```bash
cd flutter_app
flutter run
```

> **Ważne:** Uruchamiaj projekt z folderu `flutter_app`, nie głównego `SpiderZone/app` (Kotlin).
> Jeśli w dolnym menu widzisz zakładki **Gatunki** i **Społeczność** — to stara aplikacja Android, nie Flutter.

## Jak skutecznie odświeżyć kod (Flutter)

| Sytuacja | Co zrobić |
|----------|-----------|
| Zmiana w `.dart` (UI, logika) | **Hot reload** — `r` w terminalu lub ikona błyskawicy |
| Zmiana providerów, routingu, `main()` | **Hot restart** — `R` (wielka litera) lub „Restart” |
| Nowy plik w `assets/`, zmiana `pubspec.yaml` | Zatrzymaj app → `flutter pub get` → `flutter run` (pełny rebuild) |
| Dziwne cache / stary ekran mimo zmian | `flutter clean` → `flutter pub get` → `flutter run` |
| Gradle „Sync” w Android Studio | Dotyczy tylko natywnego Androida (`app/`). **Flutter tego nie używa** do odświeżania UI |

### Szybki reset (gdy nic się nie aktualizuje)

```bash
cd flutter_app
flutter clean
flutter pub get
flutter run
```

### Jak poznać, że masz właściwą aplikację Flutter

- Dolne menu: **Home · Hodowla · Rozmnażanie · Profil** (4 zakładki)
- Na Home: nagłówek **„Wyszukiwarka gatunków”** + info **„Baza lokalna: N gatunków (assets/terrarium_species.csv)”**
- Stara Kotlin: **Home · Gatunki · Hodowla · Społeczność · Rozmnażanie · Profil** (6 zakładek)

### Cursor / VS Code

Otwórz folder `flutter_app` (albo użyj konfiguracji **SpiderZone Flutter** z `.vscode/launch.json`) i uruchom F5.

## Design system — Terrarium Night

| Token | Wartość |
|-------|---------|
| Tło | `#0E1411` |
| Karty | `#17211B` |
| Karty / warstwa 2 | `#203027` |
| Primary | `#6FBF73` |
| Akcent | `#D98E32` |
| Tekst | `#E7EEE9` / `#9DAEA4` |
| Radius kart | 14px |
| Animacja ekranu | 250ms fade + slide |

## Relacja do projektu Android (Kotlin)

W katalogu nadrzędnym (`SpiderZone/`) jest działająca wersja natywna Android.
Od teraz Kotlin Compose traktujemy jako wersję referencyjną/prototyp, a Flutter jako docelowy klient iOS + Android z jednego kodu.
